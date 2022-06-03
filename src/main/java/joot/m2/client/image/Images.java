package joot.m2.client.image;

import java.nio.ByteBufferUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.graphics.Pixmap;
import com.github.jootnet.m2.core.actor.Action;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.HumActionInfo;
import com.github.jootnet.m2.core.image.WZL;

public final class Images {

	/** 微端基址 */
	public static String wdBaseUrl = null;

	/** 用于纹理的异步加载对象 */
	private static Map<String, WZL> WZLs = new HashMap<>();
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
		if (texs.size() != fileNames.length) return null;
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
		if (WZLs.containsKey(lib_idx[0])) {
			WZLs.get(lib_idx[0]).load(Integer.parseInt(lib_idx[1]));
		} else {
			WZL wzl = new WZL(lib_idx[0], wdBaseUrl);
			wzl.onTextureLoaded((fno, no, tex) -> {
				if (tex.isEmpty) {
					if (EMPTY == null) {
						Pixmap pm = new Pixmap(tex.width, tex.height, Pixmap.Format.RGBA8888);
						pm.setPixels(ByteBufferUtil.of(tex.pixels.buffer()));
						EMPTY = new M2Texture(pm, (short) tex.offsetX, (short) tex.offsetY);
					}
					textures.put(fno + "/" + no, EMPTY);
				} else {
					Pixmap pm = new Pixmap(tex.width, tex.height, Pixmap.Format.RGBA8888);
					pm.setPixels(ByteBufferUtil.of(tex.pixels.buffer()));
					textures.put(fno + "/" + no, new M2Texture(pm, (short) tex.offsetX, (short) tex.offsetY));
				}
			});
			wzl.load(Integer.parseInt(lib_idx[1]));
			WZLs.put(lib_idx[0], wzl);
		}
	}
}
