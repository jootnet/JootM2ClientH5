package joot.m2.client.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.ui.LoginPane;
import joot.m2.client.ui.ModifyPswPane;
import joot.m2.client.ui.NewUserPane;

/**
 * 登陆框
 * 
 * @author linxing
 *
 */
public final class LoginScene extends BaseScene {
	/** 登录框 */
	private LoginPane loginPane;
	/** 创建账号 */
	private NewUserPane createPane;
	/** 修改密码 */
	private ModifyPswPane modifyPswPane;
	
	public LoginScene() {
		super(new Stage());
	}
	
	@Override
	public void hide() {
		inited = false;
		super.hide();
	}
	
	private boolean inited;
	protected boolean initializeComponents() {
		if (inited) return true;
		M2Texture[] texs = Images.get("ui1/2850");
		if (texs == null)
			return false;
		stage.addActor(new Image(texs[0]));
		
		loginPane = new LoginPane(() -> {
			createPane.setVisible(true);
			loginPane.setVisible(false);
		}, () -> {
			modifyPswPane.setVisible(true);
			loginPane.setVisible(false);
		});
		loginPane.setFillParent(true);
		stage.addActor(loginPane);
		
		createPane = new NewUserPane(() -> {
			loginPane.setVisible(true);
			createPane.setVisible(false);
			loginPane.focusInput();
		});
		createPane.setFillParent(true);
		createPane.setVisible(false);
		stage.addActor(createPane);
		
		modifyPswPane = new ModifyPswPane(() -> {
			loginPane.setVisible(true);
			modifyPswPane.setVisible(false);
			loginPane.focusInput();
		});
		modifyPswPane.setFillParent(true);
		modifyPswPane.setVisible(false);
		stage.addActor(modifyPswPane);
		
		inited = true;
		return true;
	}
}