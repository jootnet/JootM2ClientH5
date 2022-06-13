package joot.m2.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.github.jootnet.m2.core.actor.Occupation;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.LoginResp;
import com.github.jootnet.m2.core.net.messages.LoginResp.Role;
import com.github.jootnet.m2.core.net.messages.NewChrResp;

import com.google.gwt.dom.client.InputElement;
import joot.m2.client.App;
import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.*;

/**
 * 创建角色
 * 
 * @author linxing
 *
 */
public class NewChrPane extends WidgetGroup {
	private Image bg;
	/** 昵称 */
	private InputElement txtName;
	/** 选择战士 */
	private ImageButton btnWarrior;
	/** 选择法师 */
	private ImageButton btnMaster;
	/** 选择道士 */
	private ImageButton btnTaoist;
	/** 选择男 */
	private ImageButton btnMan;
	/** 选择女 */
	private ImageButton btnWoman;
	/** 提交 */
	private ImageButton btnCommit;
	/** 叉叉 */
	private ImageButton btnClose;
	/** 角色动画 */
	private Animation<M2Texture> aniChr1;
	private float deltaAniChr1;
	private Image imgChr1;
	private Occupation occu = Occupation.warrior;
	private byte gender = 0;
	private OperationConsumer closeConsumer;

	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}

	/**
	 * 
	 * @param closeConsumer 面板关闭
	 */
	public NewChrPane(OperationConsumer closeConsumer) {
		this.closeConsumer = closeConsumer;
	}

	private boolean lastVisible = true;

	@Override
	public void act(float delta) {
		if (!initializeComponents()) return;
		if (isVisible() && !lastVisible) {
			InputUtil.show("newChr-txtName");
			txtName.focus();
		}
		lastVisible = isVisible();

		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.NEW_CHR_RESP) {
				NewChrResp newChrResp = (NewChrResp) msg;
				String tip = "未知错误";
				switch (newChrResp.code) {
				case 0:
					if (App.Roles != null && App.Roles.length > 0) {
						Role[] roles = new LoginResp.Role[2];
						roles[0] = App.Roles[0];
						roles[1] = newChrResp.role;
						App.Roles = roles;
					}
					if (App.Roles == null || App.Roles.length == 0) {
						App.Roles = new LoginResp.Role[1];
						App.Roles[0] = newChrResp.role;
					}
					tip = "角色创建成功";
					break;
				case 1:
					tip = "角色已满";
					break;
				case 2:
					tip = "昵称已存在";
					break;
				case 3:
					tip = "昵称不合法";
					break;
				default:
					break;
				}
				DialogUtil.alert(null, tip, null);
				return true;
			}

			return false;
		});

		if (aniChr1 != null) {
			deltaAniChr1 += delta;
			imgChr1.setDrawable(new TextureRegionDrawable(aniChr1.getKeyFrame(deltaAniChr1)));
		}

		super.act(delta);
	}

	// 选中状态的动画序列帧
	private static String[][][] selectAniTexs = new String[][][] {
			// 男
			new String[][] {
					// 战
					IntStream.range(40, 56).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(80, 96).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(120, 136).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]) },
			new String[][] {
					IntStream.range(160, 176).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(200, 216).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(240, 256).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]) } };

	private void select() {
		deltaAniChr1 = 0;
		aniChr1 = null;
		imgChr1.setDrawable(null);

		aniChr1 = new Animation<M2Texture>(0.15f, Images.get(selectAniTexs[gender][occu.ordinal()]));
		aniChr1.setPlayMode(PlayMode.LOOP);
	}

	private boolean inited;
	private boolean initializeComponents() {
		if (inited)
			return true;
		List<String> texsFileNames = new ArrayList<>();
		Stream.of("prguse/73", "prguse/74", "prguse/75", "prguse/76", "prguse/77", "prguse/78",
				"prguse/62", "prguse/64").forEach(fn -> texsFileNames.add(fn));
		IntStream.range(40, 56).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(80, 96).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(120, 136).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(160, 176).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(200, 216).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(240, 256).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		M2Texture[] texs = Images.get(texsFileNames.toArray(new String[0]));
		if (texs == null)
			return false;

		int texIdx = 0;
		addActor(bg = new Image(texs[texIdx++]));

		addActor(btnWarrior = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnMaster = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnTaoist = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnMan = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnWoman = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnCommit = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnClose = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));

		txtName = InputUtil.newInput("newChr-txtName", 646, 198, 133, 1, "white", "transparent");
		txtName.setMaxLength(10);
		addActor(imgChr1 = new Image());
		imgChr1.setPosition(152, 300);
		imgChr1.setSize(300, 360);
		imgChr1.setScaling(Scaling.none);

		btnWarrior.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Occupation occuO = occu;
				occu = Occupation.warrior;
				if (occuO != occu)
					select();
			}

		});
		btnMaster.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Occupation occuO = occu;
				occu = Occupation.master;
				if (occuO != occu)
					select();
			}

		});
		btnTaoist.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Occupation occuO = occu;
				occu = Occupation.taoist;
				if (occuO != occu)
					select();
			}

		});
		btnMan.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				byte genderO = gender;
				gender = 0;
				if (gender != genderO)
					select();
			}

		});
		btnWoman.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				byte genderO = gender;
				gender = 1;
				if (gender != genderO)
					select();
			}

		});
		btnClose.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				setVisible(false);
				InputUtil.hide("newChr-txtName");
				if (closeConsumer != null)
					closeConsumer.op();
			}

		});
		btnCommit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!txtName.getValue().trim().isEmpty())
					NetworkUtil.sendNewChr(txtName.getValue(), occu, gender);
			}

		});

		select();

		bg.setPosition(573, 259);
		btnWarrior.setPosition(621, 483);
		btnMaster.setPosition(666, 483);
		btnTaoist.setPosition(711, 483);
		btnMan.setPosition(666, 410);
		btnWoman.setPosition(711, 410);
		btnCommit.setPosition(675, 283);
		btnClose.setPosition(820, 623);

		inited = true;
		return true;
	}
}
