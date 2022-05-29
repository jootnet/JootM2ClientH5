package joot.m2.client.util;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 背景/光标等资源工具类
 * 
 * @author linxing
 *
 */
public final class DrawableUtil {
	/** 光标<br>深灰 */
	public static Drawable Cursor_DarkGray = null;
	/** 光标<br>白色 */
	public static Drawable Cursor_White = null;
	/** 背景<br>纯白 */
	public static Drawable Bg_White = null;
	/** 背景<br>浅灰 */
	public static Drawable Bg_LightGray = null;
	/** 背景<br>红色 */
	public static Drawable Bg_Red = null;
	
	static {
		byte[] colorBits_LightGray = new byte[] {(byte) (Color.LIGHT_GRAY.r * 255), (byte) (Color.LIGHT_GRAY.g * 255), (byte) (Color.LIGHT_GRAY.b * 255), -1};
		byte[] colorBits_DarkGray = new byte[] {(byte) (Color.DARK_GRAY.r * 255), (byte) (Color.DARK_GRAY.g * 255), (byte) (Color.DARK_GRAY.b * 255), -1};
		byte[] colorBits_Trans = new byte[4];
		byte[] colorBits_White = new byte[] {(byte) (Color.WHITE.r * 255), (byte) (Color.WHITE.g * 255), (byte) (Color.WHITE.b * 255), -1};
		byte[] colorBits_Red = new byte[] {(byte) (Color.RED.r * 255), (byte) (Color.RED.g * 255), (byte) (Color.RED.b * 255), -1};

		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		ByteBuffer buffer = ByteBuffer.allocateDirect(4);
		buffer.put(colorBits_LightGray);
		buffer.flip();
		pm.setPixels(buffer);
		Bg_LightGray = new TextureRegionDrawable(new Texture(pm));

		pm = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
		buffer = ByteBuffer.allocateDirect(36);
		buffer.put(colorBits_DarkGray);
		buffer.put(colorBits_DarkGray);
		buffer.put(colorBits_DarkGray);
		buffer.put(colorBits_Trans);
		buffer.put(colorBits_DarkGray);
		buffer.put(colorBits_Trans);
		buffer.put(colorBits_DarkGray);
		buffer.put(colorBits_DarkGray);
		buffer.put(colorBits_DarkGray);
		buffer.flip();
		pm.setPixels(buffer);
		Cursor_DarkGray = new NinePatchDrawable(new NinePatch(new Texture(pm), 1, 1, 1, 1));

		pm = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
		buffer = ByteBuffer.allocateDirect(36);
		buffer.put(colorBits_White);
		buffer.put(colorBits_White);
		buffer.put(colorBits_White);
		buffer.put(colorBits_Trans);
		buffer.put(colorBits_White);
		buffer.put(colorBits_Trans);
		buffer.put(colorBits_White);
		buffer.put(colorBits_White);
		buffer.put(colorBits_White);
		buffer.flip();
		pm.setPixels(buffer);
		Cursor_White = new NinePatchDrawable(new NinePatch(new Texture(pm), 1, 1, 1, 1));

		pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		buffer = ByteBuffer.allocateDirect(4);
		buffer.put(colorBits_White);
		buffer.flip();
		pm.setPixels(buffer);
		Bg_White = new TextureRegionDrawable(new Texture(pm));

		pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		buffer = ByteBuffer.allocateDirect(4);
		buffer.put(colorBits_Red);
		buffer.flip();
		pm.setPixels(buffer);
		Bg_Red = new TextureRegionDrawable(new Texture(pm));
	}
}
