package joot.m2.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 文字工具类
 * 
 * @author linxing
 *
 */
public final class FontUtil {
	/** 默认字体 */
	public static BitmapFont Default = new BitmapFont();
	
	/** bmp文字12像素宋体<br>所有文字28522<br>边缘平滑 */
	public static BitmapFont Song_12_all_outline = null;
	
	static {
		Song_12_all_outline = new BitmapFont(Gdx.files.internal("fonts/song12/all_outline.fnt"));
		Song_12_all_outline.getData().lineHeight = 14;
	}
}
