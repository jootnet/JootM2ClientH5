package com.github.jootnet.m2.core.image;

import com.google.gwt.typedarrays.shared.ArrayBufferView;

import java.nio.HasArrayBufferView;

public final class Texture {

	/** 是否为空 */
	public boolean isEmpty;
	/** 像素宽度 */
	public int width;
	/** 像素高度 */
	public int height;
	/** 绘制横向像素偏移 */
	public int offsetX;
	/** 绘制纵向像素偏移 */
	public int offsetY;
	/** 像素色彩值 */
	public ArrayBufferView pixels;

	public Texture(boolean isEmpty, int width, int height, int offsetX, int offsetY, ArrayBufferView pixels) {
		this.isEmpty = isEmpty;
		this.width = width;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.pixels = pixels;
	}
}
