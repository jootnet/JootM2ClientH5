package joot.m2.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.NewUserResp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.*;

/**
 * 创建用户
 * 
 * @author linxing
 *
 */
public class NewUserPane extends WidgetGroup {

	private Image bg;
	/** 提交 */
	private ImageButton btnCommit;
	/** 取消 */
	private ImageButton btnCancel;
	/** 叉叉 */
	private ImageButton btnClose;
	/** 用户名 */
	private InputElement txtUna;
	/** 密码 */
	private InputElement txtPsw;
	/** 确认密码 */
	private InputElement txtPsw1;
	/** 君の名は。 */
	private InputElement txtName;
	/** 安全问题1 */
	private InputElement txtQ1;
	/** 安全问题答案1 */
	private InputElement txtA1;
	/** 安全问题2 */
	private InputElement txtQ2;
	/** 安全问题答案2 */
	private InputElement txtA2;
	/** 固定电话 */
	private InputElement txtTelPhone;
	/** 移动电话 */
	private InputElement txtiPhone;
	/** 邮箱 */
	private InputElement txtMail;
	/** 提示信息 */
	private Label lblTips;
	private OperationConsumer closeConsumer;

	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}

	/**
	 * 
	 * @param closeConsumer 关闭新用户创建界面的操作
	 */
	public NewUserPane(OperationConsumer closeConsumer) {
		this.closeConsumer = closeConsumer;
	}

	private boolean lastVisible = true;

	@Override
	public void act(float delta) {
		if (!initializeComponents()) return;
		if (isVisible() && !lastVisible) {
			InputUtil.show("login-newUser-txtUna");
			InputUtil.show("login-newUser-txtPsw");
			InputUtil.show("login-newUser-txtPsw1");
			InputUtil.show("login-newUser-txtName");
			InputUtil.show("login-newUser-txtQ1");
			InputUtil.show("login-newUser-txtA1");
			InputUtil.show("login-newUser-txtQ2");
			InputUtil.show("login-newUser-txtA2");
			InputUtil.show("login-newUser-txtTelPhone");
			InputUtil.show("login-newUser-txtiPhone");
			InputUtil.show("login-newUser-txtMail");
			txtUna.focus();
		}
		lastVisible = isVisible();

		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.NEW_USER_RESP) {
				NewUserResp newUserResp = (NewUserResp) msg;
				String tip = "未知错误";
				switch (newUserResp.code) {
				case 0:
					tip = "账号创建成功\n请牢记您的密码找回问题以及答案";
					break;
				case 1:
					tip = "用户名已存在";
					break;
				// TODO
				default:
					break;
				}
				DialogUtil.alert(null, tip, null);
				return true;
			}
			return false;
		});

		super.act(delta);
	}

	private boolean inited;
	private boolean initializeComponents() {
		if (inited)
			return true;
		M2Texture[] texs = Images.get("prguse/63", "prguse/62", "prguse/52", "prguse/64");
		if (texs == null)
			return false;
		addActor(bg = new Image(texs[0]));

		addActor(btnCommit = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[1]), null, null, null, null)));
		addActor(btnCancel = new ImageButton(
				new ImageButtonStyle(new TextureRegionDrawable(texs[2]), null, null, null, null, null)));
		addActor(btnClose = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[3]), null, null, null, null)));

		txtUna = InputUtil.newInput("login-newUser-txtUna", 356, 263, 112, 1, "white", "transparent");
		txtUna.setMaxLength(18);
		InputUtil.setTextFieldFilter(txtUna, Character::isLetterOrDigit);
		InputUtil.onFocusChange(txtUna, focused -> {
			if (focused) {
				lblTips.setText("登陆用户名\n可以输入字母（大小写）数字\n最长18位");
			} else {
				lblTips.setText("");
			}
		});
		txtPsw = InputUtil.newInput("login-newUser-txtPsw", 356, 284, 112, 2, "white", "transparent");
		txtPsw.setMaxLength(20);
		InputUtil.setTextFieldFilter(txtPsw, c -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.' || c == '_'
				|| c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!' || c == '~'
				|| c == '`');
		InputUtil.onFocusChange(txtPsw, focused -> {
			if (focused) {
				lblTips.setText("登陆密码\n可以输入字母（大小写）数字\n以及“~`@#$%^&*_-”等符号\n最长20位");
			} else {
				lblTips.setText("");
			}
		});
		txtPsw1 = InputUtil.newInput("login-newUser-txtPsw1", 356, 305, 112, 3, "white", "transparent");
		txtPsw1.setMaxLength(20);
		InputUtil.setTextFieldFilter(txtPsw1, c -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.'
				|| c == '_' || c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!'
				|| c == '~' || c == '`');
		InputUtil.onFocusChange(txtPsw1, focused -> {
			if (focused) {
				lblTips.setText("再次输入密码以确认");
			} else {
				lblTips.setText("");
			}
		});
		txtName = InputUtil.newInput("login-newUser-txtName", 356, 334, 112, 4, "white", "transparent");
		txtName.setMaxLength(10);
		InputUtil.setTextFieldFilter(txtName, c -> (c >= 0x4e00) && (c <= 0x9fbb));
		InputUtil.onFocusChange(txtName, focused -> {
			if (focused) {
				lblTips.setText("姓名\n只能输入中文\n最长10位");
			} else {
				lblTips.setText("");
			}
		});
		txtQ1 = InputUtil.newInput("login-newUser-txtQ1", 356, 403, 160, 5, "white", "transparent");
		txtQ1.setMaxLength(20);
		InputUtil.onFocusChange(txtQ1, focused -> {
			if (focused) {
				lblTips.setText("密码找回问题1\n请认真填写\n最长20位");
			} else {
				lblTips.setText("");
			}
		});
		txtA1 = InputUtil.newInput("login-newUser-txtA1", 356, 423, 160, 6, "white", "transparent");
		txtA1.setMaxLength(20);
		InputUtil.onFocusChange(txtA1, focused -> {
			if (focused) {
				lblTips.setText("密码找回答案1\n最长20位");
			} else {
				lblTips.setText("");
			}
		});
		txtQ2 = InputUtil.newInput("login-newUser-txtQ2", 356, 443, 160, 7, "white", "transparent");
		txtQ2.setMaxLength(20);
		InputUtil.onFocusChange(txtQ2, focused -> {
			if (focused) {
				lblTips.setText("密码找回问题2\n最长20位");
			} else {
				lblTips.setText("");
			}
		});
		txtA2 = InputUtil.newInput("login-newUser-txtA2", 356, 463, 160, 8, "white", "transparent");
		txtA2.setMaxLength(20);
		InputUtil.onFocusChange(txtA2, focused -> {
			if (focused) {
				lblTips.setText("密码找回答案2\n最长20位");
			} else {
				lblTips.setText("");
			}
		});
		txtTelPhone = InputUtil.newInput("login-newUser-txtTelPhone", 356, 493, 112, 9, "white", "transparent");
		txtTelPhone.setMaxLength(18);
		InputUtil.setTextFieldFilter(txtTelPhone, c -> (c >= '0') && (c <= '9'));
		InputUtil.onFocusChange(txtTelPhone, focused -> {
			if (focused) {
				lblTips.setText("固定电话号码\n只能输入数字\n最长18位");
			} else {
				lblTips.setText("");
			}
		});
		txtiPhone = InputUtil.newInput("login-newUser-txtiPhone", 356, 514, 112, 10, "white", "transparent");
		txtiPhone.setMaxLength(18);
		InputUtil.setTextFieldFilter(txtiPhone, c -> (c >= '0') && (c <= '9'));
		InputUtil.onFocusChange(txtiPhone, focused -> {
			if (focused) {
				lblTips.setText("手机号码\n只能输入数字\n最长18位");
			} else {
				lblTips.setText("");
			}
		});
		txtMail = InputUtil.newInput("login-newUser-txtMail", 356, 535, 160, 11, "white", "transparent");
		txtMail.setMaxLength(20);
		InputUtil.setTextFieldFilter(txtMail, c -> Character.isLetterOrDigit(c) || c == '-' || c == '.' || c == '@');
		InputUtil.onFocusChange(txtMail, focused -> {
			if (focused) {
				lblTips.setText("电子邮箱地址\n最长20位");
			} else {
				lblTips.setText("");
			}
		});
		addActor(lblTips = new Label("", new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
		lblTips.setAlignment(Align.top | Align.left, Align.left);
		lblTips.setSize(200, 280);

		btnCancel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				InputUtil.hideAll();
				if (closeConsumer != null)
					closeConsumer.op();
			}
		});
		btnClose.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				InputUtil.hideAll();
				if (closeConsumer != null)
					closeConsumer.op();
			}
		});
		btnCommit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				NetworkUtil.sendNewUser(txtUna.getValue(), txtPsw.getValue(), txtName.getValue(), txtQ1.getValue(),
						txtA1.getValue(), txtQ2.getValue(), txtA2.getValue(), txtTelPhone.getValue(), txtiPhone.getValue(),
						txtMail.getValue());
			}

		});

		bg.setPosition((getStage().getWidth() - bg.getWidth()) / 2, (getStage().getHeight() - bg.getHeight()) / 2);
		btnCommit.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 158, (getStage().getHeight() - bg.getHeight()) / 2 + 24.5f);
		btnCancel.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 446, (getStage().getHeight() - bg.getHeight()) / 2 + 21.5f);
		btnClose.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 587, (getStage().getHeight() - bg.getHeight()) / 2 + 417.5f);
		/*txtUna.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 344);
		txtPsw.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 323);
		txtPsw1.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 302);
		txtName.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 272);
		txtQ1.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 204);
		txtA1.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 183);
		txtQ2.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 162);
		txtA2.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 143);
		txtTelPhone.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 113);
		txtiPhone.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 92);
		txtMail.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 164, (getStage().getHeight() - bg.getHeight()) / 2 + 71);*/
		lblTips.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 386, (getStage().getHeight() - bg.getHeight()) / 2 + 73);

		inited = true;
		return true;
	}
}
