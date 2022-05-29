package joot.m2.client.image;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * 适用于M2的带偏移量的纹理对象
 * 
 * @author 林星
 *
 */
public final class M2Texture extends Texture {
	
	/** 纹理绘制时自带的横向偏移量 */
	private short offsetX;
	/** 纹理绘制时自带的纵向偏移量 */
	private short offsetY;

	/**
	 * 使用纹理数据与偏移量构建M2纹理对象
	 * 
	 * @param pixmap 纹理数据
	 * @param offsetX 横向偏移量
	 * @param offsetY 纵向偏移量
	 */
	public M2Texture(Pixmap pixmap, short offsetX, short offsetY) {
		super(pixmap);
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
