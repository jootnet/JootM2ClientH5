/*
 * Copyright 2017 JOOTNET Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Support: https://github.com/jootnet/mir2.core
 */
package com.github.jootnet.m2.core.map;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 热血传奇2地图
 * <br>
 * 即地图文件(*.map)到Java中数据结构的描述
 * <br>
 * 即一个MapHeader和一个MapTile二维数组
 * <br>
 * 但实际上不使用MapHeader和MapTile，因为MapHeader和MapTile数据太散，不便于使用
 * <br>
 * 而是将MapHeader中关键地图信息提取出来放到Map里，将MapTile重新解析为{@link MapTileInfo}以方便程序逻辑
 * 
 * @author 云中双月
 */
public final class Map {
	
	/** 地图宽度 */
	private short width;
	/** 地图高度 */
	private short height;
	/** 地图块数据 */
	private MapTileInfo[][] tiles;
	
	public Map() { }
	
	public Map(byte[] mapData) {
		ByteBuffer buffer = ByteBuffer.wrap(mapData).order(ByteOrder.LITTLE_ENDIAN);
		this.setWidth(buffer.getShort());
		this.setHeight(buffer.getShort());
		buffer.position(buffer.position() + 48);
		int tileByteSize = buffer.remaining() / this.getWidth() / this.getHeight();
		MapTileInfo[][] mapTileInfos = new MapTileInfo[this.getWidth()][this.getHeight()];
		for (int width = 0; width < this.getWidth(); ++width)
			for (int height = 0; height < this.getHeight(); ++height) {
				MapTileInfo mi = new MapTileInfo();
				// 读取背景
				short bng = buffer.getShort();
				// 读取中间层
				short mid = buffer.getShort();
				// 读取对象层
				short obj = buffer.getShort();
				// 设置背景
				if ((bng & 0x7fff) > 0) {
					mi.setBngImgIdx((short) ((bng & 0x7fff) - 1));
					mi.setHasBng(true);
				}
				// 设置中间层
				if ((mid & 0x7fff) > 0) {
					mi.setMidImgIdx((short) ((mid & 0x7fff) - 1));
					mi.setHasMid(true);
				}
				// 设置对象层
				if ((obj & 0x7fff) > 0) {
					mi.setObjImgIdx((short) ((obj & 0x7fff) - 1));
					mi.setHasObj(true);
				}
				// 设置是否可站立
				mi.setCanWalk((bng & 0x8000) != 0x8000 && (obj & 0x8000) != 0x8000);
				// 设置是否可飞行
				mi.setCanFly((obj & 0x8000) != 0x8000);

				// 读取门索引(第7个byte)
				byte btTmp = buffer.get();
				if ((btTmp & 0x80) == 0x80) {
					mi.setDoorCanOpen(true);
				}
				mi.setDoorIdx((byte) (btTmp & 0x7F));
				// 读取门偏移(第8个byte)
				btTmp = buffer.get();
				if (btTmp != 0) {
					mi.setHasDoor(true);
				}
				mi.setDoorOffset((short) (btTmp & 0xFF));
				// 读取动画帧数(第9个byte)
				btTmp = buffer.get();
				if ((btTmp & 0x7F) > 0) {
					mi.setAniFrame((byte) (btTmp & 0x7F));
					mi.setHasAni(true);
					mi.setHasObj(false);
					mi.setAniBlendMode((btTmp & 0x80) == 0x80);
				}
				// 读取并设置动画跳帧数(第10个byte)
				mi.setAniTick(buffer.get());
				// 读取资源文件索引(第11个byte)
				mi.setObjFileIdx(buffer.get());
				if (mi.getObjFileIdx() != 0)
					mi.setObjFileIdx((byte) (mi.getObjFileIdx() + 1));
				// 读取光照(第12个byte)
				mi.setLight(buffer.get());
				if (tileByteSize == 14) {
					mi.setBngFileIdx(buffer.get());
					if (mi.getBngFileIdx() != 0)
						mi.setBngFileIdx((byte) (mi.getBngFileIdx() + 1));
					mi.setMidFileIdx(buffer.get());
					if (mi.getMidFileIdx() != 0)
						mi.setMidFileIdx((byte) (mi.getMidFileIdx() + 1));
				} else if (tileByteSize > 14) {
					buffer.position(buffer.position() + tileByteSize - 14);
					System.err.println("unkwon tileByteSize " + tileByteSize);
				}
				if (width % 2 != 0 || height % 2 != 0)
					mi.setHasBng(false);
				mapTileInfos[width][height] = mi;
			}
		this.setMapTiles(mapTileInfos);
	}
	
	/** 获取地图宽度 */
	public short getWidth() {
		return width;
	}
	/** 设置地图宽度 */
	public void setWidth(short width) {
		this.width = width;
	}
	/** 获取地图高度 */
	public short getHeight() {
		return height;
	}
	/** 设置地图高度 */
	public void setHeight(short height) {
		this.height = height;
	}
	/** 获取地图块信息 */
	public MapTileInfo[][] getTiles() {
		return tiles;
	}
	/** 设置地图块信息 */
	public void setMapTiles(MapTileInfo[][] mapTiles) {
		this.tiles = mapTiles;
	}
}
