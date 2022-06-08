package joot.m2.client.image;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 适用于M2的带偏移量的纹理对象
 * 
 * @author 林星
 *
 */
public final class M2Texture extends TextureRegion {
	
	/** 纹理绘制时自带的横向偏移量 */
	private short offsetX;
	/** 纹理绘制时自带的纵向偏移量 */
	private short offsetY;

	public M2Texture(Texture texture, int x, int y, int width, int height, short offsetX, short offsetY) {
		super(texture, x, y, width, height);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}


	/**
	 * 获取图片横向偏移量
	 * 
	 * @return 图片横向偏移量,单位为像素
	 */
	public short getOffsetX() {
		return offsetX;
	}
	/**
	 * 获取图片纵向偏移量
	 * 
	 * @return 图片纵向偏移量,单位为像素
	 */
	public short getOffsetY() {
		return offsetY;
	}

}
