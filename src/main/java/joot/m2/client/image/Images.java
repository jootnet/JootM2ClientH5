package joot.m2.client.image;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.github.jootnet.m2.core.NetworkUtil;
import com.github.jootnet.m2.core.actor.Action;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.HumActionInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.typedarrays.shared.ArrayBuffer;

public final class Images {

	/** 微端基址 */
	public static String wdBaseUrl = null;

	/** 用于纹理的异步加载对象 */
	private static Map<String, TexureLoader> textureLoaders = new HashMap<>();
	/** 已加载的纹理 */
	private static Map<String, M2Texture> textures = new ConcurrentHashMap<>();
	private static M2Texture EMPTY;
	private static M2Texture[] EMPTY_ARRAY = new M2Texture[0];
	
	/**
	 * @param wdBaseUrl 微端基址
	 */
	public static void init(String wdBaseUrl) {
		if (!wdBaseUrl.endsWith("/")) wdBaseUrl += "/";
		Images.wdBaseUrl = wdBaseUrl;
	}
	
	/**
	 * 获取已加载资源
	 * 
	 * @param fileNames 资源名称
	 * @return 除非所需资源都已加载完毕，否则返回null
	 */
	public static M2Texture[] get(String... fileNames) {
		List<M2Texture> texs = new ArrayList<>();
		for (String fileName : fileNames) {
			M2Texture tex = textures.get(fileName);
			if (tex == null) {
				load(fileName);
			} else {
				texs.add(tex);
			}
		}
		if (texs.size() != fileNames.length) {
			if (texs.size() == fileNames.length - 1) {
				for (String fileName : fileNames) {
					M2Texture tex = textures.get(fileName);
					if (tex == null) {
						GWT.log("last unload fileName: " + fileName);
					}
				}
			}
			return null;
		}
		return texs.toArray(EMPTY_ARRAY);
	}
	
	/**
	 * 获取人物衣服贴图
	 * 
	 * @param hum 人物
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getDress(ChrBasicInfo hum) {
		int fileIdx = hum.humFileIdx;
		int dressIdx = hum.humIdx;
		HumActionInfo action = hum.action;
		int tick = hum.actionTick;
		if (action.act == Action.Stand && tick > 4) tick -= 4;
		int texIdx = action.frameIdx + tick - 1;
		String dressName = "hum";
		if (fileIdx != 0)
			dressName += fileIdx;
		dressName += "/";
		dressName += ((dressIdx - 1) * 600 + texIdx);
		M2Texture tex = textures.get(dressName);
		if (tex == null) {
			load(dressName);
			return null;
		} else {
			return tex;
		}
	}
	
	/**
	 * 获取人物武器贴图
	 * 
	 * @param hum 人物
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getWeapon(ChrBasicInfo hum) {
		int fileIdx = hum.weaponFileIdx;
		int weaponIdx = hum.weaponIdx;
		if (weaponIdx == 0) return null;
		HumActionInfo action = hum.action;
		int tick = hum.actionTick;
		if (action.act == Action.Stand && tick > 4) tick -= 4;
		int texIdx = action.frameIdx + tick - 1;
		String weaponName = "weapon";
		if (fileIdx != 0)
			weaponName += fileIdx;
		weaponName += "/";
		weaponName += ((weaponIdx - 1) * 600 + texIdx);
		M2Texture tex = textures.get(weaponName);
		if (tex == null) {
			load(weaponName);
			return null;
		} else {
			return tex;
		}
	}
	
	/**
	 * 获取人物衣服特效贴图
	 * 
	 * @param hum 人物
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getHumEffect(ChrBasicInfo hum) {
		int fileIdx = hum.humEffectFileIdx;
		int humeffectIdx = hum.humEffectIdx;
		if (humeffectIdx == 0) return null;
		HumActionInfo action = hum.action;
		int tick = hum.actionTick;
		int texIdx = action.frameIdx + tick - 1;
		String humeffectName = "humeffect";
		if (fileIdx != 0)
			humeffectName += fileIdx;
		humeffectName += "/";
		humeffectName += ((humeffectIdx - 1) * 600 + texIdx);
		M2Texture tex = textures.get(humeffectName);
		if (tex == null) {
			load(humeffectName);
			return null;
		} else {
			return tex;
		}
	}

	private static void load(String fileName) {
		String[] lib_idx = fileName.split("/");
		if (textureLoaders.containsKey(lib_idx[0])) {
			textureLoaders.get(lib_idx[0]).load(Integer.parseInt(lib_idx[1]));
		} else {
			TexureLoader loader = new TexureLoader(lib_idx[0]);
			loader.load(Integer.parseInt(lib_idx[1]));
			textureLoaders.put(lib_idx[0], loader);
		}
	}

	private static class TexureLoader implements NetworkUtil.HttpResponseListener {

		private String ilName;
		private boolean atlasDownloading;
		private Set<String> pngDownloading = new HashSet<>();
		private Map<String, Map<String, Map<String, Integer>>> atlasContent;

		private TexureLoader(String ilName) {
			this.ilName = ilName;
		}

		private void load(int idx) {
			if (atlasContent == null) {
				downloadAtlas();
				return;
			}
			String idxStr = String.valueOf(idx);
			String pngName = null;
			for (Map.Entry<String, Map<String, Map<String, Integer>>> entry : atlasContent.entrySet()) {
				if (entry.getValue().containsKey(idxStr)) {
					if (pngDownloading.contains(entry.getKey())) {
						return;
					}
					download(entry.getKey());
				}
			}
		}

		private void downloadAtlas() {
			if (atlasContent != null || atlasDownloading) return;
			atlasDownloading = true;
			NetworkUtil.sendHttpRequest(new NetworkUtil.HttpRequest(wdBaseUrl + ilName + "/" + ilName + ".atlas").setTimeout(6000), this);
		}

		private void download(final String pngName) {
			if (pngDownloading.contains(pngName)) {
				return;
			}
			pngDownloading.add(pngName);
			Pixmap.downloadFromUrl(wdBaseUrl + ilName + "/" + pngName, new Pixmap.DownloadPixmapResponseListener() {
				@Override
				public void downloadComplete(Pixmap pixmap) {
					pngDownloading.remove(pngName);
					Texture texture = new Texture(pixmap);
					for (Map.Entry<String, Map<String, Integer>> texInfo : atlasContent.get(pngName).entrySet()) {
						textures.put(ilName + "/" + texInfo.getKey(), new M2Texture(texture
								, texInfo.getValue().get("x")
								, texInfo.getValue().get("y")
								, texInfo.getValue().get("w")
								, texInfo.getValue().get("h")
								, texInfo.getValue().get("ox").shortValue()
								, texInfo.getValue().get("oy").shortValue()));
					}
					atlasContent.remove(pngName);
				}

				@Override
				public void downloadFailed(Throwable t) {
					pngDownloading.remove(pngName);
				}
			});
		}

		@Override
		public void recvHeaders(Map<String, String> headers, String url) {

		}

		@Override
		public void onLoad(ArrayBuffer message, String url) {
			atlasDownloading = false;
		}

		@Override
		public void onLoad(String message, String url) {
			atlasDownloading = false;
			atlasContent = new HashMap<>();
			String[] lines = message.split("\r\n");
			Map<String, Map<String, Integer>> png = null;
			Map<String, Integer> tex = null;
			for (String _line : lines) {
				if (_line == null) continue;
				String line = _line.trim();
				if (line.isEmpty()) continue;
				if (!line.contains(":")) {
					if (line.endsWith("png")) {
						png = new HashMap<>();
						tex = null;
						atlasContent.put(line, png);
					} else {
						tex = new HashMap<>();
						png.put(line, tex);
					}
				} else {
					if (line.startsWith("xy")) {
						String[] xy = line.split(":")[1].split(",");
						xy[0] = xy[0].trim();
						xy[1] = xy[1].trim();
						tex.put("x", Integer.parseInt(xy[0]));
						tex.put("y", Integer.parseInt(xy[1]));
					} else if (line.startsWith("size") && tex != null) {
						String[] size = line.split(":")[1].split(",");
						size[0] = size[0].trim();
						size[1] = size[1].trim();
						tex.put("w", Integer.parseInt(size[0]));
						tex.put("h", Integer.parseInt(size[1]));
					} else if (line.startsWith("offset")) {
						String[] offset = line.split(":")[1].split(",");
						offset[0] = offset[0].trim();
						offset[1] = offset[1].trim();
						tex.put("ox", Integer.parseInt(offset[0]));
						tex.put("oy", Integer.parseInt(offset[1]));
					}
				}
			}
		}

		@Override
		public void onError(String url) {
			atlasDownloading = false;
		}

		@Override
		public void onTimeout(String url) {
			atlasDownloading = false;
		}
	}
}
