package joot.m2.client.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** 地图信息描述对象  */
public final class Map {

	/** 地图宽度 */
	public short width;
	/** 地图高度 */
	public short height;
	
	/** 可移动标记 */
	public boolean[][] canWalk;
	/** 可飞跃标记 */
	public boolean[][] canFly;
	
	/** 地图块大地砖所在文件及索引 */
	public String[][] tilesFileName;
	/** 地图块小地砖所在文件及索引 */
	public String[][] smTilesFileName;
	/** 地图对象层图片所在文件及索引 */
	public String[][] objsFileName;
	
	/** 地图块大地砖纹理 */
	public TextureRegion[][] tilesTexture;
	/** 地图块小地砖纹理 */
	public TextureRegion[][] smTilesTexture;
	
	/** 地图对象层图片纹理 */
	private List<TextureRegion>[] objTextureRegions;
	/** 对象层图片纹理的xy坐标 */
	private java.util.Map<TextureRegion, int[]> objTextureRegions2XY = new HashMap<>();
	
	/**
	 * 使用地图宽高初始化地图对象
	 * 
	 * @param width 地图宽（横向块数量）
	 * @param height 地图高（纵向块数量）
	 */
	@SuppressWarnings("unchecked")
	public Map(short width, short height) {
		this.width = width;
		this.height = height;
		canWalk = new boolean[width + 1][height + 1]; // 避免坐标转换
		canFly = new boolean[width + 1][height + 1];
		tilesFileName = new String[width + 1][height + 1];
		smTilesFileName = new String[width + 1][height + 1];
		objsFileName = new String[width + 1][height + 1];
		tilesTexture = new TextureRegion[width + 1][height + 1];
		smTilesTexture = new TextureRegion[width + 1][height + 1];
		objTextureRegions = new List[height + 1];
		for (int h = 0; h < objTextureRegions.length; ++h) {
			objTextureRegions[h] = new ArrayList<>();
		}
	}
	
	/**
	 * 添加特定地图块的对象层纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param tex 对象层纹理
	 * @param anchorY 纹理起点纵坐标，例如树的根
	 */
	public void addObjTextureRegion(int x, int y, TextureRegion tex, int anchorY) {
		objTextureRegions2XY.put(tex, new int[] {x, y});
		objTextureRegions[anchorY].add(tex);
	}
	
	/**
	 * 获取地图特定行对象纹理
	 * 
	 * @param anchorY 纹理起点纵坐标
	 * @return 地图所有对象纹理
	 */
	public List<TextureRegion> getObjsTextureRegion(int anchorY) {
		return objTextureRegions[anchorY];
	}
	
	/**
	 * 获取地图块层纹理所在块坐标
	 * 
	 * @param tex 层纹理
	 * @return x,y分为在第0，1个元素
	 */
	public int[] getObjTextureRegion(TextureRegion tex) {
		return objTextureRegions2XY.get(tex);
	}
}
