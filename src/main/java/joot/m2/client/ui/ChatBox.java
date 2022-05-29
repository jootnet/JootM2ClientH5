package joot.m2.client.ui;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;

import joot.m2.client.App;
import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;

/**
 * 聊天框
 * 
 * @author linxing
 *
 */
public final class ChatBox extends WidgetGroup {	
	/** 文本输入 */
	TextField txtChat;
	/** 逐行显示历史消息的控件 */
	private Table[] linesMsg;
	private String[] strsMsg; // 历史消息内容
	private Drawable[] bgsMsg; // 历史消息背景色
	private Color[] colorsMsg; // 历史消息文字颜色
	private long msgsCount; // 历史消息总数
	/** 历史消息滚动栏 */
	private Slider slrMsg;
	private Button btnMsgUp; // 向上单次滚动按钮
	private Button btnMsgDown; // 向上单次滚动按钮
	
	/**
	 * 追加新消息到消息框
	 * 
	 * @param msg 消息文字内容
	 * @param fontColor 文字颜色，默认为黑色
	 * @param bg 背景色，默认为{@link DrawableUtil#Bg_White}，系统消息可能为{@link DrawableUtil#Bg_Red}
	 */
	public void appendMsg(String msg, @Null Color fontColor, @Null Drawable bg) {
		if (fontColor == null) {
			fontColor = Color.BLACK;
		}
		if (bg == null) {
			bg = DrawableUtil.Bg_White;
		}
		int _writeIdx = (int) (msgsCount++ % 100);
		strsMsg[_writeIdx] = msg;
		colorsMsg[_writeIdx] = fontColor;
		bgsMsg[_writeIdx] = bg;
		slrMsg.setProgrammaticChangeEvents(false);
		if (msgsCount > strsMsg.length) {
			// 超过最大缓存条目后永远显示最后几条
			slrMsg.setValue(0);
		} else {
			// 显示最后几条
			slrMsg.setValue(slrMsg.getMaxValue() - Math.max(0, msgsCount - linesMsg.length));
		}
		slrMsg.setProgrammaticChangeEvents(true);
		refreshMsgShow();
	}
	
	private void refreshMsgShow() {
		int readIdxMsg = (int) (slrMsg.getMaxValue() - slrMsg.getValue());
		for (int i = 0; i < linesMsg.length; ++i) {
			linesMsg[i].background((Drawable) null);
			linesMsg[i].reset();
		}
		for (int i = 0, j = readIdxMsg; i < linesMsg.length; ++i, ++j) {
			if (j >= strsMsg.length) j = 0;
			if (strsMsg[j] == null) break;
			Label lblMsg = new Label(strsMsg[j], new LabelStyle(FontUtil.Song_12_all, colorsMsg[j]));
			linesMsg[i].background(bgsMsg[j])
				.add(lblMsg)
				.left()
				.growX()
				.padTop(1);
			lblMsg.addListener(new ClickListener() {
				
				public void clicked(InputEvent event, float x, float y) {
					txtChat.setText(lblMsg.getText().toString());
				}
				
			});
		}
	}
	
	@Override
	public void act(float delta) {
		initializeComponents();
		super.act(delta);
	}
	
	private boolean inited;
	private boolean initializeComponents() {
		if (inited) return true;
		M2Texture[] texs = Images.get(IntStream.range(500, 510).mapToObj(i -> "ui3/" + i).collect(Collectors.toList()).toArray(new String[0]));
		if (texs == null) return false;
		addActor(txtChat = new TextField("", new TextField.TextFieldStyle(FontUtil.Song_12_all,
				Color.BLACK,
				DrawableUtil.Cursor_DarkGray,
				DrawableUtil.Bg_LightGray,
				null)));
		txtChat.setPosition(16, 6);
		txtChat.setWidth(606);
		txtChat.setMaxLength(100);
		txtChat.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
					String say = txtChat.getText().trim();
					if (say.isEmpty()) return true;
					if (say.equals("@smoothon")) {
						App.SmoothMoving = true;
						appendMsg("enable smooth moving", Color.GREEN, null);
						txtChat.setText("");
						return true;
					}
					if (say.equals("@smoothoff")) {
						App.SmoothMoving = false;
						appendMsg("disable smooth moving", Color.WHITE, DrawableUtil.Bg_Red);
						txtChat.setText("");
						return true;
					}
					appendMsg(say, null, null);
					txtChat.setText("");
					return true;
				}
				return false;
			}
		});
		linesMsg = new Table[8]; // 显示8行文字
		for (int i = 0; i < linesMsg.length; ++i) {
			linesMsg[i] = new Table();
			linesMsg[i].background(DrawableUtil.Bg_White);
			linesMsg[i].setSize(606, 13);
			linesMsg[i].setPosition(16, 117 - i * 13); // 每行13像素，其中12像素文字加1像素padding
			addActor(linesMsg[i]);
		}
		strsMsg = new String[100]; // 最多记录历史数据条目数，可以改动此值来增加历史消息数目
		bgsMsg = new Drawable[strsMsg.length];
		colorsMsg = new Color[strsMsg.length];

		int texIdx = 0;
		SliderStyle slrMsgStyle = new SliderStyle(new TextureRegionDrawable(texs[texIdx++]), null);

		addActor(btnMsgUp = new Button(new ButtonStyle()));
		btnMsgUp.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);
		btnMsgUp.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnMsgUp.getStyle().down = new TextureRegionDrawable(texs[texIdx++]);

		addActor(btnMsgDown = new Button(new ButtonStyle()));
		btnMsgDown.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);
		btnMsgDown.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnMsgDown.getStyle().down = new TextureRegionDrawable(texs[texIdx++]);

		slrMsgStyle.knob = new TextureRegionDrawable(texs[texIdx++]);
		slrMsgStyle.knobOver = new TextureRegionDrawable(texs[texIdx++]);
		slrMsgStyle.knobDown = new TextureRegionDrawable(texs[texIdx++]);
		addActor(slrMsg = new Slider(0, strsMsg.length - 1, 1, true, slrMsgStyle));
		
		// libgdx的slider虽然可以竖直显示，但只能以上为增大，下为减小。因此后文为兼容数据与界面，用“max-”来设置和获取实际数值
		slrMsg.setValue(slrMsg.getMaxValue());
		slrMsg.setSize(16, 100);
		slrMsg.setPosition(622, 28);
		slrMsg.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				refreshMsgShow();
			}
			
		});
		btnMsgUp.setSize(16, 10);
		btnMsgUp.setPosition(622, 128);
		btnMsgUp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				slrMsg.setValue(slrMsg.getValue() + slrMsg.getStepSize());
			}
		});
		btnMsgDown.setSize(16, 10);
		btnMsgDown.setPosition(622, 20);
		btnMsgDown.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				slrMsg.setValue(slrMsg.getValue() - slrMsg.getStepSize());
			}
		});
		inited = true;
		return true;
	}
}
