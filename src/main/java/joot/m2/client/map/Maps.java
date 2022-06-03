package joot.m2.client.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.jootnet.m2.core.NetworkUtil;
import com.github.jootnet.m2.core.NetworkUtil.HttpRequest;
import com.github.jootnet.m2.core.NetworkUtil.HttpResponseListener;
import com.github.jootnet.m2.core.SDK;
import com.github.jootnet.m2.core.map.MapTileInfo;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.typedarrays.client.DataViewNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.DataView;
import com.google.gwt.typedarrays.shared.Uint8Array;

/** 地图异步加载对象 */
public final class Maps {
	/** 微端基址 */
	public static String wdBaseUrl = null;

	/** 地图缓存 */
	private static java.util.Map<String, Map> maps = new HashMap<>();
	/** 正在加载的地图 */
	private static Set<String> downloadingMaps = new HashSet<>();
	
	/**
	 * @param wdBaseUrl 微端基址
	 */
	public static void init(String wdBaseUrl) {
		if (!wdBaseUrl.endsWith("/")) wdBaseUrl += "/";
		Maps.wdBaseUrl = wdBaseUrl;
	}

	/**
	 * 通过编号获取地图对象
	 * 
	 * @param mapNo 地图编号
	 * @return 地图对象或null
	 */
	public static Map get(String mapNo) {
		if (maps.containsKey(mapNo))
			return maps.get(mapNo);
		if (downloadingMaps.contains(mapNo)) return null;
		downloadingMaps.add(mapNo);
		NetworkUtil.sendHttpRequest(new HttpRequest(wdBaseUrl + "map/" + mapNo + ".map").setTimeout(5000).setBinary(), new HttpResponseListener() {
			
			@Override
			public void onTimeout() {
				downloadingMaps.remove(mapNo);
			}
			
			@Override
			public void onLoad(String message) {
				downloadingMaps.remove(mapNo);
			}
			
			@Override
			public void onLoad(ArrayBuffer message) {
				DataView buffer = DataViewNative.create(message);
				try {
					Uint8Array tmpArr = SDK.unzip(message);
					buffer = DataViewNative.create(tmpArr.buffer(), tmpArr.byteOffset(), tmpArr.byteLength());
				} catch (JavaScriptException ex) {
					ex.printStackTrace();
				}
				com.github.jootnet.m2.core.map.Map m2map = new com.github.jootnet.m2.core.map.Map(buffer);
				Map map = new Map(m2map.getWidth(), m2map.getHeight());
				IntStream.range(0, m2map.getWidth()).parallel().forEach(_w -> {
					IntStream.range(0, m2map.getHeight()).parallel().forEach(_h -> {
						MapTileInfo mti = m2map.getTiles()[_w][_h];
						int w = _w + 1; // 转换为游戏坐标
						int h = _h + 1;
						map.canFly[w][h] = mti.isCanFly();
						map.canWalk[w][h] = mti.isCanWalk();
						if (mti.isHasBng()) {
							String tileFileName = "tiles";
							if (mti.getBngFileIdx() != 0) {
								tileFileName += mti.getBngFileIdx();
							}
							tileFileName += "/" + mti.getBngImgIdx();
							map.tilesFileName[w][h] = tileFileName;
						}
						if (mti.isHasMid()) {
							String smTileFileName = "smtiles";
							if (mti.getMidFileIdx() != 0) {
								smTileFileName += mti.getMidFileIdx();
							}
							smTileFileName += "/" + mti.getMidImgIdx();
							map.smTilesFileName[w][h] = smTileFileName;
						}
						if (!mti.isHasAni() && mti.isHasObj()) {
							String objFileName = "objects";
							if (mti.getObjFileIdx() != 0) {
								objFileName += mti.getObjFileIdx();
							}
							objFileName += "/" + mti.getObjImgIdx();
							map.objsFileName[w][h] = objFileName;
						}
					});
				});
				maps.put(mapNo, map);
				downloadingMaps.remove(mapNo);
			}
			
			@Override
			public void onError() {
				downloadingMaps.remove(mapNo);
			}

			@Override
			public void recvHeaders(java.util.Map<String, String> headers) {
				
			}
		});
		return null;
	}

}
