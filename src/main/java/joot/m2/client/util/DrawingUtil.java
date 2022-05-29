package joot.m2.client.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 用作转换坐标系绘图的一些功能函数
 * 
 * @author 林星
 *
 */
public class DrawingUtil {
	
	/** 矩形框对象 */
	public static class RectI {
		public int x;
		public int y;
		public int width;
		public int height;
	}
	
	/**
	 * 转换坐标系后将纹理绘制到画布上
	 * <br>
	 * OpenGL默认坐标系为左下角，M2是针对Windows操作系统开发的，使用的是DirectX，因此为左上角坐标系。
	 * <br>
	 * 绘制时需要转换
	 * 
	 * @param batch 画笔对象
	 * @param screenWidth 画布宽度
	 * @param screenHeight 画布高度
	 * @param tex 纹理对象
	 * @param cx 左上角坐标系下的画布贴合点左上角x
	 * @param cy 左上角坐标系下的画布贴合点左上角y
	 */
	public static void transDraw(Batch batch, int screenWidth, int screenHeight, Texture tex, int cx, int cy) {
		batch.draw(tex, cx, screenHeight - cy - tex.getHeight());
	}

	/**
	 * 转换坐标系后将纹理区域绘制到画布上
	 * <br>
	 * OpenGL默认坐标系为左下角，M2是针对Windows操作系统开发的，使用的是DirectX，因此为左上角坐标系。
	 * <br>
	 * 绘制时需要转换
	 * 
	 * @param batch 画笔对象
	 * @param screenWidth 画布宽度
	 * @param screenHeight 画布高度
	 * @param tex 纹理区域对象
	 * @param cx 左上角坐标系下的画布贴合点左上角x
	 * @param cy 左上角坐标系下的画布贴合点左上角y
	 */
	public static void transDraw(Batch batch, int screenWidth, int screenHeight, TextureRegion tex, int cx, int cy) {
		batch.draw(tex, cx, screenHeight - cy - tex.getRegionHeight());
	}
}
