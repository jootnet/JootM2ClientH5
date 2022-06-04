package com.github.jootnet.m2.core.image;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import com.github.jootnet.m2.core.NetworkUtil;
import com.github.jootnet.m2.core.NetworkUtil.HttpRequest;
import com.github.jootnet.m2.core.NetworkUtil.HttpResponseListener;
import com.github.jootnet.m2.core.SDK;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.typedarrays.client.ArrayBufferNative;
import com.google.gwt.typedarrays.client.DataViewNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.DataView;
import com.google.gwt.typedarrays.shared.Uint8Array;

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
	private final String wzxUrl;
	/** wzl网络路径 */
	private final String wzlUrl;
	/** 待加载的纹理编号 */
	private final Queue<Integer> seizes;
	/** 文件名 */
	private final String fno;
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
	 */
	public void onTextureLoaded(TextureConsumer consumer) {
		textureConsumer = consumer;
	}

	/**
	 * 加载特定编号纹理 <br>
	 * 当这些编号纹理加载完毕之后，仍会在后台继续加载库内其他纹理，并通过{@link TextureConsumer#recv(String, int, Texture)}向外告知
	 *
	 * @param seizes 需要加载的纹理编号
	 */
	public void load(int... seizes) {
		for (int i : seizes) {
			this.seizes.offer(i);
		}
		doDownload();
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
			// 单次加载最大数据量（从磁盘或网络下载）
			// 默认256K
			int maxLoadSizePer = 256 * 1024;
			long rangeEnd = Math.min(startOffset + Math.max(maxLoadSizePer, texDataLen), fLen - 1);
			downloading = true;
			NetworkUtil.sendHttpRequest(new HttpRequest(wzlUrl).setBinary().setRange(startOffset, (int) rangeEnd), this);
		}
	}

	private void unpackTextures(ArrayBuffer data) {
		if (startNo == -1) return;
		int idx = 0;
		DataView byteBuffer = DataViewNative.create(data);
		for (int no = startNo; no < imageCount; ++no) {
			if (offsetList[no] == 0) {
				if (!loadedFlag[no]) {
					loadedFlag[no] = true;
					if (textureConsumer != null)
						textureConsumer.recv(fno, no, EMPTY);
				}
				continue;
			}
			if ((byteBuffer.byteLength() - idx) < 16)
				break;
			byte colorBit = byteBuffer.getInt8(idx);
			idx += 1;
			boolean compressFlag = byteBuffer.getInt8(idx) != 0;
			idx += 1;
			idx += 2; // 2字节未知数据
			short width = byteBuffer.getInt16(idx, true);
			idx += 2;
			short height = byteBuffer.getInt16(idx, true);
			idx += 2;
			short offsetX = byteBuffer.getInt16(idx, true);
			idx += 2;
			short offsetY = byteBuffer.getInt16(idx, true);
			idx += 2;
			int dataLen = byteBuffer.getInt32(idx, true);
			idx += 4;
			if (dataLen == 0) {
				// 这里可能是一个bug，或者是其他引擎作者没有说清楚
				// 本来一直以为是compressFlag作为是否zlib压缩的标志位
				// 后来发现如果这里的长度是0，则表示没有压缩，后面是图片尺寸的裸数据
				dataLen = width * height;
				if (colorBit == 5) dataLen *= 2;
				compressFlag = false;
			}
			if ((byteBuffer.byteLength() - idx) < dataLen)
				break;
			if (loadedFlag[no]) {
				idx += dataLen;
				continue;
			}
			DataView pixels = DataViewNative.create(data, idx);
			if (compressFlag) {
				try {
					Uint8Array tmpArr = SDK.unzip(data, idx);
					pixels = DataViewNative.create(tmpArr.buffer(), tmpArr.byteOffset(), tmpArr.byteLength());
				} catch (JavaScriptException e) {
					e.printStackTrace();
				}
			}
			idx += dataLen;
			DataView sRGBA = DataViewNative.create(ArrayBufferNative.create(width * height * 4));
			int p_index = 0;
			if (colorBit != 5) { // 8位
				for (int h = height - 1; h >= 0; --h)
					for (int w = 0; w < width; ++w) {
						// 跳过填充字节
						if (w == 0)
							p_index += SDK.skipBytes(8, width);
						byte[] pallete = SDK.palletes[pixels.getInt8(p_index++) & 0xff];
						int _idx = (w + h * width) * 4;
						sRGBA.setInt8(_idx, pallete[1]);
						sRGBA.setInt8(_idx + 1, pallete[2]);
						sRGBA.setInt8(_idx + 2, pallete[3]);
						sRGBA.setInt8(_idx + 3, pallete[0]);
					}
			} else { // 16位
				for (int h = height - 1; h >= 0; --h)
					for (int w = 0; w < width; ++w, p_index += 2) {
						// 跳过填充字节
						if (w == 0)
							p_index += SDK.skipBytes(16, width);
						short pdata = pixels.getInt16(p_index, true);
						byte r = (byte) ((pdata & 0xf800) >> 8);// 由于是与16位做与操作，所以多出了后面8位
						byte g = (byte) ((pdata & 0x7e0) >> 3);// 多出了3位，在强转时前8位会自动丢失
						byte b = (byte) ((pdata & 0x1f) << 3);// 少了3位
						int _idx = (w + h * width) * 4;
						sRGBA.setInt8(_idx, r);
						sRGBA.setInt8(_idx + 1, g);
						sRGBA.setInt8(_idx + 2, b);
						if (r == 0 && g == 0 && b == 0) {
							sRGBA.setInt8(_idx + 3, 0);
						} else {
							sRGBA.setInt8(_idx + 3, -1);
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

	/** 空图片 */
	private final static Texture EMPTY;
	
	static {
		DataView dv = DataViewNative.create(ArrayBufferNative.create(4));
		dv.setInt8(0, SDK.palletes[0][1]);
		dv.setInt8(1, SDK.palletes[0][2]);
		dv.setInt8(2, SDK.palletes[0][3]);
		dv.setInt8(3, SDK.palletes[0][0]);
		EMPTY = new Texture(true, 1, 1, 0, 0, dv);
	}

	@Override
	public void onLoad(ArrayBuffer message) {
		downloading = false;
		if (offsetList == null) {
			DataView byteBuffer = DataViewNative.create(message);
			try {
				Uint8Array tmpArr = SDK.unzip(message);
				byteBuffer = DataViewNative.create(tmpArr.buffer(), tmpArr.byteOffset(), tmpArr.byteLength());
			} catch (JavaScriptException ex) {
				ex.printStackTrace();
			}
			int idx = 0;
			idx += 44;
			imageCount = byteBuffer.getInt32(idx, true);
			idx += 4;
			offsetList = new int[imageCount + 1];
			loadedFlag = new boolean[imageCount];
			for (int i = 0; i < imageCount; ++i) {
				offsetList[i] = byteBuffer.getInt32(idx, true);//Uint32
				idx += 4;
				if (offsetList[i] < 64) offsetList[i] = 0;
			}
		} else if (fLen != -1) {
			unpackTextures(message);
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
