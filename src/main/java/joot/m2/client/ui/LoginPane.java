package joot.m2.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.LoginResp;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.RootPanel;
import joot.m2.client.App;
import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.DialogUtil;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;
import joot.m2.client.util.NetworkUtil;

/**
 * 登录框
 */
public final class LoginPane extends WidgetGroup {
	private Image bg;
	/** 新用户按钮 */
	private ImageButton btnNew;
	/** 修改密码按钮 */
	private ImageButton btnModifyPsw;
	/** 登录按钮 */
	private ImageButton btnLogin;
	/** 退出按钮 */
	private ImageButton btnExit;
	/** 用户名 */
	private TextField txtUna;
	/** 密码 */
	private TextField txtPsw;
	private OperationConsumer modifyPswOperationConsumer;
	private OperationConsumer newUserOperationConsumer;

	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}

	/**
	 * 
	 * @param newUserOperationConsumer   点击“新用户”按钮进行的操作
	 * @param modifyPswOperationConsumer 点击“修改密码”按钮进行的操作
	 */
	public LoginPane(OperationConsumer newUserOperationConsumer, OperationConsumer modifyPswOperationConsumer) {
		this.newUserOperationConsumer = newUserOperationConsumer;
		this.modifyPswOperationConsumer = modifyPswOperationConsumer;
	}

	@Override
	public void act(float delta) {
		initializeComponents();
		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.LOGIN_RESP) {
				LoginResp loginResp = (LoginResp) msg;
				int code = loginResp.code;
				if (code == 0) {
					App.Roles = loginResp.roles;
					App.LastName = loginResp.lastName;
					App.toChrSel();
				} else {
					String tip = loginResp.serverTip;
					if (tip == null) {
						switch (loginResp.code) {
						case 1:
							tip = "用户名或密码错误";
							break;
						case 2:
							tip = "用户不存在";
							break;
						case 3:
							tip = "用户已在其他地方登陆";
							break;
						}
					}
					DialogUtil.alert(null, tip, null);
				}
				return true;
			}
			return false;
		});

		super.act(delta);
	}

	/**
	 * 将焦点给到输入框
	 */
	public void focusInput() {
		RootPanel.get().getElement().focus();
		txtUna.setText("");
		txtPsw.setText("");
		getStage().setKeyboardFocus(txtUna);
	}

	private boolean inited;
	private boolean initializeComponents() {
		if (inited)
			return true;
		M2Texture[] texs = Images.get("prguse/60", "prguse/61", "prguse/53", "prguse/62", "prguse/64");
		if (texs == null)
			return false;

		int texIdx = 0;
		addActor(bg = new Image(texs[texIdx++]));
		addActor(btnNew = new ImageButton(
				new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]), null, null, null, null, null)));
		addActor(btnModifyPsw = new ImageButton(
				new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]), null, null, null, null, null)));
		addActor(btnLogin = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnExit = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));

		btnNew.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (newUserOperationConsumer != null)
					newUserOperationConsumer.op();
			}

		});
		btnModifyPsw.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (modifyPswOperationConsumer != null)
					modifyPswOperationConsumer.op();
			}

		});
		btnExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}

		});
		btnLogin.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				String una = txtUna.getText().trim();
				String psw = txtPsw.getText().trim();
				if (una.isEmpty() || psw.isEmpty()) {
					return;
				}
				NetworkUtil.sendLoginReq(una, psw);
			}

		});

		addActor(txtUna = new TextField("", new TextFieldStyle(FontUtil.Song_12_all, Color.WHITE,
					DrawableUtil.Cursor_White, DrawableUtil.Bg_LightGray, null)));
		txtUna.setWidth(130);
		txtUna.setMaxLength(20);
		addActor(txtPsw = new TextField("", new TextFieldStyle(FontUtil.Song_12_all, Color.WHITE,
				DrawableUtil.Cursor_White, DrawableUtil.Bg_LightGray, null)));
		txtPsw.setWidth(130);
		txtPsw.setMaxLength(20);
		txtPsw.setPasswordMode(true);
		txtPsw.setPasswordCharacter('*');
		txtPsw.addListener(new InputListener() {

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
					String una = txtUna.getText().trim();
					String psw = txtPsw.getText().trim();
					if (!una.isEmpty() && !psw.isEmpty()) {
						NetworkUtil.sendLoginReq(una, psw);
					}
					return true;
				}
				return false;
			}

		});

		bg.setPosition((getStage().getWidth() - bg.getWidth()) / 2, (getStage().getHeight() - bg.getHeight()) / 2);
		btnNew.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 25, (getStage().getHeight() - bg.getHeight()) / 2 + 17);
		btnModifyPsw.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 130, (getStage().getHeight() - bg.getHeight()) / 2 + 17);
		btnLogin.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 169, (getStage().getHeight() - bg.getHeight()) / 2 + 57);
		btnExit.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 252, (getStage().getHeight() - bg.getHeight()) / 2 + 203);
		txtUna.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 100, (getStage().getHeight() - bg.getHeight()) / 2 + 154);
		txtPsw.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 100, (getStage().getHeight() - bg.getHeight()) / 2 + 122);

		getStage().setKeyboardFocus(txtUna);
		inited = true;
		return true;
	}
}
