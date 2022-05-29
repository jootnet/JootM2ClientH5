package com.github.jootnet.m2.core.image;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import com.github.jootnet.m2.core.NetworkUtil;
import com.github.jootnet.m2.core.NetworkUtil.HttpRequest;
import com.github.jootnet.m2.core.NetworkUtil.HttpResponseListener;
import com.github.jootnet.m2.core.SDK;
import com.google.gwt.core.client.JavaScriptException;

/**
 * WZL文件解析类
 * <br>
 * wzl文件由wzx和wzl组成
 * <br>
 * wzx是索引文件，前44字节是文件固定头（可能有版权/修改时间等信息）
 * <br>  然后是一个int，表示图集中纹理总数（记为n），紧接着是n个int，为wzl文件中每个图片的起始数据偏移
 * <br>
 * wzl文件前64字节是描述数据，但0x2C处是一个int，也是纹理总数（n），0x28处可能是修改时间
 * <br>  然后是根据wzx中的偏移来解析每一张纹理，如果偏移是0，则表示纹理为空
 * <br>  每个纹理数据由16个固定头部和变长色彩数据组成
 * <br>  色彩数据可能使用zlib压缩过
 * 
 * @author LinXing
 *
 */
public final class WZL implements HttpResponseListener {
	/** 库内图片总数 */
	private int imageCount;
	/** 纹理数据起始偏移 */
	private int[] offsetList;
	/** 纹理加载标志 */
	private volatile boolean[] loadedFlag;
	/** 纹理消费者 */
	private TextureConsumer textureConsumer;
	/** wzx网络路径 */
	private String wzxUrl;
	/** wzl网络路径 */
	private String wzlUrl;
	/** 待加载的纹理编号 */
	private Queue<Integer> seizes;
	/** 文件名 */
	private String fno;
	/** 单次加载最大数据量（从磁盘或网络下载） */
	private int maxLoadSizePer = 256 * 1024; // 默认256K
	/** 是否处于下载过程中 */
	private boolean downloading;
	/** 当前加载纹理编号 */
	private int startNo;
	/** wzl文件总长 */
	private long fLen = -1;

	/**
	 * 
	 * @param wzlName   纹理库名称
	 * @param wdBaseUrl 微端基址
	 */
	public WZL(String wzlName, String wdBaseUrl) {
		seizes = new ArrayDeque<>();

		fno = wzlName.toLowerCase();
		wzxUrl = wdBaseUrl + "data/" + fno + ".wzx";
		wzlUrl = wdBaseUrl + "data/" + fno + ".wzl";
	}

	/**
	 * 设置纹理加载完成回调
	 * 
	 * @param consumer 事件处理函数
	 * @return 当前对象
	 */
	public WZL onTextureLoaded(TextureConsumer consumer) {
		textureConsumer = consumer;
		return this;
	}

	/**
	 * 加载特定编号纹理 <br>
	 * 当这些编号纹理加载完毕之后，仍会在后台继续加载库内其他纹理，并通过{@link TextureConsumer#recv(Texture)}向外告知
	 * <br>
	 * 此函数可多次调用，以打断后台顺序加载其他纹理 <br>
	 * 库内所有纹理加载完毕后会触发{@link LoadCompleted#op()}向外告知 <br>
	 * 如果wzl文件来自微端，则会复制到wzx同级目录
	 * 
	 * @param seizes 需要优先加载的纹理编号
	 * @return 当前对象
	 */
	public WZL load(int... seizes) {
		for (int i : seizes) {
			this.seizes.offer(i);
		}
		doDownload();
		return this;
	}
	
	private void doDownload() {
		if (downloading) return;
		if (offsetList == null) {
			downloading = true;
			NetworkUtil.sendHttpRequest(new HttpRequest(wzxUrl).setBinary(), this);
			return;
		}
		if (fLen == -1) {
			downloading = true;
			NetworkUtil.sendHttpRequest(new HttpRequest(wzlUrl).setMethod("HEAD"), this);
			return;
		}
		startNo = -1;
		while(true) {
			Integer seize = seizes.poll();
			if (seize == null) break;
			if (loadedFlag[seize]) {
				continue;
			}
			// 如果是空图片
			if (offsetList[seize] == 0) {
				loadedFlag[seize] = true;
				if (textureConsumer != null)
					textureConsumer.recv(fno, seize, EMPTY);
				continue;
			}
			startNo = seize;
			break;
		}
		if (startNo != -1) {
			int startOffset = offsetList[startNo];
			int texDataLen = 0;
			for (int i = startNo + 1; i < imageCount + 1; ++i) {
				if (offsetList[i] != 0) {
					texDataLen = offsetList[i] - offsetList[startNo];
					break;
				}
			}
			long rangeEnd = Math.min(startOffset + Math.max(maxLoadSizePer, texDataLen), fLen - 1);
			downloading = true;
			NetworkUtil.sendHttpRequest(new HttpRequest(wzlUrl).setBinary().addHeader("Range", "bytes=" + startOffset + "-" + rangeEnd), this);
		}
	}

	private void unpackTextures(byte[] data) throws IOException {
		if (startNo == -1) return;
		ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		for (int no = startNo; no < imageCount; ++no) {
			if (offsetList[no] == 0) {
				if (!loadedFlag[no]) {
					loadedFlag[no] = true;
					if (textureConsumer != null)
						textureConsumer.recv(fno, no, EMPTY);
				}
				continue;
			}
			if (byteBuffer.remaining() < 16)
				break;
			byte colorBit = byteBuffer.get();
			boolean compressFlag = byteBuffer.get() != 0;
			byteBuffer.position(byteBuffer.position() + 2); // 2字节未知数据
			short width = byteBuffer.getShort();
			short height = byteBuffer.getShort();
			short offsetX = byteBuffer.getShort();
			short offsetY = byteBuffer.getShort();
			int dataLen = byteBuffer.getInt();
			if (dataLen == 0) {
				// 这里可能是一个bug，或者是其他引擎作者没有说清楚
				// 本来一直以为是compressFlag作为是否zlib压缩的标志位
				// 后来发现如果这里的长度是0，则表示没有压缩，后面是图片尺寸的裸数据
				dataLen = width * height;
				if (colorBit == 5) dataLen *= 2;
				compressFlag = false;
			}
			if (byteBuffer.remaining() < dataLen)
				break;
			if (loadedFlag[no]) {
				byteBuffer.position(byteBuffer.position() + dataLen);
				continue;
			}
			byte[] pixels = new byte[dataLen];
			byteBuffer.get(pixels);
			if (compressFlag) {
				try {
					pixels = SDK.unzip(pixels);
				} catch (JavaScriptException e) {
					e.printStackTrace();
				}
			}
			byte[] sRGBA = new byte[width * height * 4];
			if (colorBit != 5) { // 8位
				int p_index = 0;
				for (int h = height - 1; h >= 0; --h)
					for (int w = 0; w < width; ++w) {
						// 跳过填充字节
						if (w == 0)
							p_index += SDK.skipBytes(8, width);
						byte[] pallete = SDK.palletes[pixels[p_index++] & 0xff];
						int _idx = (w + h * width) * 4;
						sRGBA[_idx] = pallete[1];
						sRGBA[_idx + 1] = pallete[2];
						sRGBA[_idx + 2] = pallete[3];
						sRGBA[_idx + 3] = pallete[0];
					}
			} else { // 16位
				ByteBuffer bb = ByteBuffer.wrap(pixels).order(ByteOrder.LITTLE_ENDIAN);
				int p_index = 0;
				for (int h = height - 1; h >= 0; --h)
					for (int w = 0; w < width; ++w, p_index += 2) {
						// 跳过填充字节
						if (w == 0)
							p_index += SDK.skipBytes(16, width);
						short pdata = bb.getShort(p_index);
						byte r = (byte) ((pdata & 0xf800) >> 8);// 由于是与16位做与操作，所以多出了后面8位
						byte g = (byte) ((pdata & 0x7e0) >> 3);// 多出了3位，在强转时前8位会自动丢失
						byte b = (byte) ((pdata & 0x1f) << 3);// 少了3位
						int _idx = (w + h * width) * 4;
						sRGBA[_idx] = r;
						sRGBA[_idx + 1] = g;
						sRGBA[_idx + 2] = b;
						if (r == 0 && g == 0 && b == 0) {
							sRGBA[_idx + 3] = 0;
						} else {
							sRGBA[_idx + 3] = -1;
						}
					}
			}
			loadedFlag[no] = true;
			if (textureConsumer != null) {
				textureConsumer.recv(fno, no, new Texture(false, width, height, offsetX, offsetY, sRGBA));
			}
		}
	}

	@FunctionalInterface
	public interface TextureConsumer {
		/**
		 * 单个纹理加载完毕时触发
		 * 
		 * @param fno 文件编号
		 * @param no  纹理编号，从0开始
		 * @param tex 纹理对象
		 */
		void recv(String fno, int no, Texture tex);
	}

	@FunctionalInterface
	public interface LoadCompletedEventHandler {
		/**
		 * 所有纹理加载完毕后触发
		 * 
		 * @param fno 文件编号
		 */
		void loadCompleted(String fno);
	}

	/** 空图片 */
	private static Texture EMPTY;
	
	static {
		EMPTY = new Texture(true, 1, 1, 0, 0, new byte[] { SDK.palletes[0][1], SDK.palletes[0][2], SDK.palletes[0][3], SDK.palletes[0][0] });
	}

	@Override
	public void onLoad(byte[] message) {
		downloading = false;
		if (offsetList == null) {
			byte[] dData = message;
			try {
				dData = SDK.unzip(dData);
			} catch (JavaScriptException ex) {
				ex.printStackTrace();
			}
			ByteBuffer byteBuffer = ByteBuffer.wrap(dData).order(ByteOrder.LITTLE_ENDIAN);
			byteBuffer.position(44);
			imageCount = byteBuffer.getInt();
			offsetList = new int[imageCount + 1];
			loadedFlag = new boolean[imageCount];
			for (int i = 0; i < imageCount; ++i) {
				offsetList[i] = byteBuffer.getInt();// UnsignedInt
				if (offsetList[i] < 64) offsetList[i] = 0;
			}
		} else if (fLen != -1) {
			try {
				unpackTextures(message);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		doDownload();
	}

	@Override
	public void onLoad(String message) {
		downloading = false;
	}

	@Override
	public void onError() {
		downloading = false;
		doDownload();
	}

	@Override
	public void onTimeout() {
		downloading = false;
		doDownload();
	}

	@Override
	public void recvHeaders(Map<String, String> headers) {
		if (offsetList != null && fLen == -1) {
			String lenStr = headers.get("content-length");
			String rangeStr = headers.get("content-range");
			if (rangeStr != null && !rangeStr.trim().isEmpty()) {
				fLen = Integer.parseInt(rangeStr.substring(rangeStr.lastIndexOf("/") + 1));
			} else if (lenStr != null && !lenStr.trim().isEmpty()) {
				fLen = Integer.parseInt(lenStr);
			}
		}
	}
}
