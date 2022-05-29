package joot.m2.client.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.DeleteChrResp;
import com.github.jootnet.m2.core.net.messages.EnterResp;
import com.github.jootnet.m2.core.net.messages.LoginResp;
import com.github.jootnet.m2.core.net.messages.LogoutResp;

import joot.m2.client.App;
import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.image.M2TextureRegionDrawable;
import joot.m2.client.ui.NewChrPane;
import joot.m2.client.util.DialogUtil;
import joot.m2.client.util.FontUtil;
import joot.m2.client.util.NetworkUtil;

/**
 * 角色选择场景
 * 
 * @author linxing
 *
 */
public final class ChrSelScene extends BaseScene {
	/** 选择第一个角色 */
	private ImageButton btnSelectChr1;
	/** 选择第二个角色 */
	private ImageButton btnSelectChr2;
	/** 开始 */
	private ImageButton btnEnter;
	/** 创建人物 */
	private ImageButton btnCreate;
	/** 恢复人物 */
	private ImageButton btnReback;
	/** 删除人物 */
	private ImageButton btnRemove;
	/** 退出 */
	private ImageButton btnExit;
	/** 标题 */
	private Label lblTitle;
	/** 第一个角色的昵称 */
	private Label lblName1;
	/** 第一个角色的等级 */
	private Label lblLevel1;
	/** 第一个角色的职业 */
	private Label lblOccu1;
	/** 第二个角色的昵称 */
	private Label lblName2;
	/** 第二个角色的等级 */
	private Label lblLevel2;
	/** 第二个角色的职业 */
	private Label lblOccu2;
	/** 开门动画 */
	private Animation<M2Texture> aniOpenDoor;
	private float deltaAniOpenDoor;
	private Image imgOpenDoor;
	/** 第一个角色动画 */
	private Animation<M2Texture> aniChr1;
	private float deltaAniChr1;
	private Image imgChr1;
	private Animation<M2Texture> aniChr1Effect; // 光效
	private float deltaAniChr1Effect;
	private Image imgChr1Effect;
	/** 第二个角色动画 */
	private Animation<M2Texture> aniChr2;
	private float deltaAniChr2;
	private Image imgChr2;
	private Animation<M2Texture> aniChr2Effect;
	private float deltaAniChr2Effect;
	private Image imgChr2Effect;
	/** 创建角色 */
	private NewChrPane newChrPane;

	public ChrSelScene() {
		super(new Stage());
	}
	
	@Override
	public void hide() {
		inited = false;
		super.hide();
	}

	private boolean inited;

	@Override
	protected boolean initializeComponents() {
		if (inited)
			return true;
		List<String> texsFileNames = new ArrayList<>();
		Stream.of("ui1/2851", "ui1/2854", "ui1/2856", "ui1/2852", "ui1/2858", "ui1/2859", "ui1/2860",
				"ui1/2861", "ui1/2862", "ui1/2863", "ui1/2864", "ui1/2865", "ui1/2850").forEach(fn -> texsFileNames.add(fn));
		IntStream.range(60, 73).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(100, 113).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(140, 153).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(180, 193).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(220, 233).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(260, 273).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(40, 56).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(80, 96).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(120, 136).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(160, 176).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(200, 216).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(240, 256).mapToObj(i -> "chrsel/" + i).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(4, 18).mapToObj(j -> "chrsel/" + j).forEach(fn -> texsFileNames.add(fn));
		IntStream.range(23, 33).mapToObj(j -> "chrsel/" + j).forEach(fn -> texsFileNames.add(fn));
		M2Texture[] texs = Images.get(texsFileNames.toArray(new String[0]));
		if (texs == null) {
			return false;
		}
		int texIdx = 0;
		stage.addActor(new Image(texs[texIdx++]));
		stage.addActor(btnSelectChr1 = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnSelectChr2 = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnEnter = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnCreate = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnReback = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnRemove = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnExit = new ImageButton(new ImageButtonStyle()));
		stage.addActor(lblTitle = new Label("将唐传奇", new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
		stage.addActor(imgChr1 = new Image());
		stage.addActor(imgChr1Effect = new Image());
		stage.addActor(imgChr2 = new Image());
		stage.addActor(imgChr2Effect = new Image());

		btnSelectChr1.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnSelectChr2.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnEnter.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnCreate.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnCreate.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);
		btnReback.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnReback.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);
		btnRemove.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnRemove.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);
		btnExit.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnExit.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);

		btnSelectChr1.setPosition(176, 134);
		btnSelectChr2.setPosition(887, 136);
		btnEnter.setPosition(476, 138);
		btnCreate.setPosition(448, 106);
		btnReback.setPosition(448, 77);
		btnRemove.setPosition(448, 49);
		btnExit.setPosition(487, 21);
		lblTitle.setPosition(464, 744);
		imgChr1.setPosition(152, 300);
		imgChr1Effect.setPosition(156, 300);
		imgChr2.setPosition(586, 300);
		imgChr2Effect.setPosition(590, 300);

		btnSelectChr1.setSize(102, 45);
		btnSelectChr2.setSize(102, 46);
		btnEnter.setSize(88, 41);
		btnCreate.setSize(145, 25);
		btnReback.setSize(145, 25);
		btnRemove.setSize(145, 25);
		btnExit.setSize(67, 24);
		lblTitle.setWidth(108);
		lblTitle.setAlignment(Align.center);
		imgChr1.setSize(300, 360);
		imgChr1Effect.setSize(292, 350);
		imgChr2.setSize(300, 360);
		imgChr2Effect.setSize(292, 350);
		imgChr1.setScaling(Scaling.none);
		imgChr1Effect.setScaling(Scaling.none);
		imgChr2.setScaling(Scaling.none);
		imgChr2Effect.setScaling(Scaling.none);

		loadRoles();

		btnEnter.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.LastName != null) {
					NetworkUtil.sendEnterGame(App.LastName);
				}
			}

		});
		btnSelectChr1.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.Roles != null && App.Roles.length > 0 && !App.Roles[0].name.equals(App.LastName)) {
					App.LastName = App.Roles[0].name;
					select(0, true);
				}
			}

		});
		btnSelectChr2.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.Roles != null && App.Roles.length > 1 && !App.Roles[1].name.equals(App.LastName)) {
					App.LastName = App.Roles[1].name;
					select(1, true);
				}
			}

		});
		
		btnRemove.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.LastName == null || App.Roles == null) return;
				DialogUtil.confirm(null, "是否删除角色[" + App.LastName + "]？", () -> {
					NetworkUtil.sendDelteChr(App.LastName);
				});
			}
			
		});

		stage.addActor(newChrPane = new NewChrPane(() -> {
			imgChr1.setVisible(true);
			loadRoles();
		}));
		newChrPane.setFillParent(true);
		newChrPane.setVisible(false);

		btnCreate.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.Roles != null && App.Roles.length > 1)
					return;
				imgChr1.setVisible(false);
				newChrPane.setVisible(true);
			}

		});
		
		btnExit.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				NetworkUtil.sendLogout();
				App.toLogin();
			}
			
		});

		inited = true;
		return true;
	}

	// 解除石化的动画序列帧
	private static String[][][] reliveAniTexs = new String[][][] {
			// 男
			new String[][] {
					// 战
					IntStream.range(60, 73).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(100, 113).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(140, 153).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]) },
			new String[][] {
					IntStream.range(180, 193).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(220, 233).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]),
					IntStream.range(260, 273).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList())
							.toArray(new String[0]) } };
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

	private void loadRoles() {
		if (lblName1 != null)
			lblName1.setText("");
		if (lblLevel1 != null)
			lblLevel1.setText("");
		if (lblOccu1 != null)
			lblOccu1.setText("");
		if (lblName2 != null)
			lblName2.setText("");
		if (lblLevel2 != null)
			lblLevel2.setText("");
		if (lblOccu2 != null)
			lblOccu2.setText("");
		if (App.Roles == null || App.Roles.length == 0) {
			select(-1, false);
			return;
		}
		if (App.Roles != null && App.Roles.length > 0) {
			if (lblName1 != null)
				stage.getActors().removeValue(lblName1, true);
			stage.addActor(
					lblName1 = new Label(App.Roles[0].name, new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
			if (lblLevel1 != null)
				stage.getActors().removeValue(lblLevel1, true);
			stage.addActor(lblLevel1 = new Label(String.valueOf(App.Roles[0].level),
					new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
			String occuString = App.Roles[0].type == 0 ? "战士"
					: App.Roles[0].type == 1 ? "法师" : App.Roles[0].type == 2 ? "道士" : "刺客";
			if (lblOccu1 != null)
				stage.getActors().removeValue(lblOccu1, true);
			stage.addActor(lblOccu1 = new Label(occuString, new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
			if (App.LastName == null) {
				App.LastName = App.Roles[0].name;
			}
		}
		if (App.Roles != null && App.Roles.length > 1) {
			if (lblName2 != null)
				stage.getActors().removeValue(lblName2, true);
			stage.addActor(
					lblName2 = new Label(App.Roles[1].name, new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
			if (lblLevel2 != null)
				stage.getActors().removeValue(lblLevel2, true);
			stage.addActor(lblLevel2 = new Label(String.valueOf(App.Roles[1].level),
					new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
			String occuString = App.Roles[1].type == 0 ? "战士"
					: App.Roles[1].type == 1 ? "法师" : App.Roles[1].type == 2 ? "道士" : "刺客";
			if (lblOccu2 != null)
				stage.getActors().removeValue(lblOccu2, true);
			stage.addActor(lblOccu2 = new Label(occuString, new LabelStyle(FontUtil.Song_12_all, Color.WHITE)));
		}
		if (App.LastName != null) {
			if (App.Roles != null && App.Roles.length > 1 && App.Roles[1].name.equals(App.LastName)) {
				select(1, false);
			} else if (App.Roles != null && App.Roles.length > 0) {
				select(0, false);
			}
		}
		if (App.Roles != null && App.Roles.length > 0) {
			lblName1.setPosition(154, 112);
			lblLevel1.setPosition(154, 74);
			lblOccu1.setPosition(154, 36);
		}
		if (App.Roles != null && App.Roles.length > 1) {
			lblName2.setPosition(866, 108);
			lblLevel2.setPosition(866, 70);
			lblOccu2.setPosition(866, 32);
		}
	}

	private void select(int i, boolean effect) {
		deltaAniChr1 = 0;
		deltaAniChr1Effect = 0;
		deltaAniChr2 = 0;
		deltaAniChr2Effect = 0;
		aniChr1 = null;
		aniChr2 = null;
		aniChr1Effect = null;
		aniChr2Effect = null;
		imgChr1.setDrawable(null);
		imgChr1Effect.setDrawable(null);
		imgChr2.setDrawable(null);
		imgChr2Effect.setDrawable(null);
		if (App.Roles != null && App.Roles.length > 0) {
			if (App.Roles[0].gender == 0) {
				// 男
				if (App.Roles[0].type == 0) {
					// 战士
					imgChr1.setDrawable(new TextureRegionDrawable(Images.get("chrsel/60")[0]));
				} else if (App.Roles[0].type == 1) {
					imgChr1.setDrawable(new TextureRegionDrawable(Images.get("chrsel/100")[0]));
				} else if (App.Roles[0].type == 2) {
					imgChr1.setDrawable(new TextureRegionDrawable(Images.get("chrsel/140")[0]));
				}
			} else {
				if (App.Roles[0].type == 0) {
					imgChr1.setDrawable(new TextureRegionDrawable(Images.get("chrsel/180")[0]));
				} else if (App.Roles[0].type == 1) {
					imgChr1.setDrawable(new TextureRegionDrawable(Images.get("chrsel/220")[0]));
				} else if (App.Roles[0].type == 2) {
					imgChr1.setDrawable(new TextureRegionDrawable(Images.get("chrsel/260")[0]));
				}
			}
		}
		if (App.Roles != null && App.Roles.length > 1) {
			if (App.Roles[1].gender == 0) {
				// 男
				if (App.Roles[1].type == 0) {
					// 战士
					imgChr2.setDrawable(new TextureRegionDrawable(Images.get("chrsel/60")[0]));
				} else if (App.Roles[1].type == 1) {
					imgChr2.setDrawable(new TextureRegionDrawable(Images.get("chrsel/100")[0]));
				} else if (App.Roles[1].type == 2) {
					imgChr2.setDrawable(new TextureRegionDrawable(Images.get("chrsel/140")[0]));
				}
			} else {
				if (App.Roles[1].type == 0) {
					imgChr2.setDrawable(new TextureRegionDrawable(Images.get("chrsel/180")[0]));
				} else if (App.Roles[1].type == 1) {
					imgChr2.setDrawable(new TextureRegionDrawable(Images.get("chrsel/220")[0]));
				} else if (App.Roles[1].type == 2) {
					imgChr2.setDrawable(new TextureRegionDrawable(Images.get("chrsel/260")[0]));
				}
			}
		}
		if (i == 0) {
			if (effect) {
				aniChr1 = new Animation<M2Texture>(0.15f,
						Images.get(reliveAniTexs[App.Roles[0].gender][App.Roles[0].type]));
				aniChr1Effect = new Animation<M2Texture>(0.15f, Images.get(IntStream.range(4, 18)
						.mapToObj(j -> "chrsel/" + j).collect(Collectors.toList()).toArray(new String[0])));
			} else {
				aniChr1 = new Animation<M2Texture>(0.15f,
						Images.get(selectAniTexs[App.Roles[0].gender][App.Roles[0].type]));
				aniChr1.setPlayMode(PlayMode.LOOP);
			}
		} else if (i == 1) {
			if (effect) {
				aniChr2 = new Animation<M2Texture>(0.15f,
						Images.get(reliveAniTexs[App.Roles[1].gender][App.Roles[1].type]));
				aniChr2Effect = new Animation<M2Texture>(0.15f, Images.get(IntStream.range(4, 18)
						.mapToObj(j -> "chrsel/" + j).collect(Collectors.toList()).toArray(new String[0])));
			} else {
				aniChr2 = new Animation<M2Texture>(0.15f,
						Images.get(selectAniTexs[App.Roles[1].gender][App.Roles[1].type]));
				aniChr2.setPlayMode(PlayMode.LOOP);
			}
		}
	}

	@Override
	public void render(float delta) {
		if (!initializeComponents()) return;

		if (aniOpenDoor != null) {
			deltaAniOpenDoor += delta;
			if (aniOpenDoor.isAnimationFinished(deltaAniOpenDoor)) {
				for (int i = 0; i < App.Roles.length; ++i) {
					if (App.Roles[i].name.equals(App.LastName)) {
						App.MapNo = App.Roles[i].mapNo;
						break;
					}
				}
				aniOpenDoor = null;
				App.toGame();
				return;
			}
			imgOpenDoor.setDrawable(new TextureRegionDrawable(aniOpenDoor.getKeyFrame(deltaAniOpenDoor)));
		}
		if (aniChr1 != null) {
			deltaAniChr1 += delta;
			imgChr1.setDrawable(new TextureRegionDrawable(aniChr1.getKeyFrame(deltaAniChr1)));
		}
		if (aniChr2 != null) {
			deltaAniChr2 += delta;
			imgChr2.setDrawable(new TextureRegionDrawable(aniChr2.getKeyFrame(deltaAniChr2)));
		}
		if (aniChr1Effect != null) {
			deltaAniChr1Effect += delta;
			if (aniChr1Effect.isAnimationFinished(deltaAniChr1Effect)) {
				select(0, false);
				return;
			}
			imgChr1Effect.setDrawable(new M2TextureRegionDrawable(aniChr1Effect.getKeyFrame(deltaAniChr1Effect), true));
		}
		if (aniChr2Effect != null) {
			deltaAniChr2Effect += delta;
			if (aniChr2Effect.isAnimationFinished(deltaAniChr2Effect)) {
				select(1, false);
				return;
			}
			imgChr2Effect.setDrawable(new M2TextureRegionDrawable(aniChr2Effect.getKeyFrame(deltaAniChr2Effect), true));
		}

		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.ENTER_RESP) {
				EnterResp enterResp = (EnterResp) msg;
				if (enterResp.forbidTip != null) {
					DialogUtil.alert(null, enterResp.forbidTip, null);
				} else {
					if (!enterResp.cBasic.name.equals(App.LastName))
						return false; // ??
					App.ChrBasic = enterResp.cBasic;
					App.ChrPublic = enterResp.cPublic;
					App.ChrPrivate = enterResp.cPrivate;

					stage.clear();
					stage.addActor(new Image(Images.get("ui1/2850")[0]));

					imgOpenDoor = new Image();
					imgOpenDoor.setPosition(239, 236);
					imgOpenDoor.setSize(496, 361);
					stage.addActor(imgOpenDoor);

					deltaAniOpenDoor = 0;
					aniOpenDoor = new Animation<M2Texture>(0.15f, Images.get(IntStream.range(23, 33)
							.mapToObj(j -> "chrsel/" + j).collect(Collectors.toList()).toArray(new String[0])));
				}
				return true;
			} else if (msg.type() == MessageType.LOGOUT_RESP) {
				LogoutResp logoutResp = (LogoutResp) msg;
				if (logoutResp.code != 0) {
					DialogUtil.alert(null, logoutResp.serverTip, null);
					return true;
				}
				App.toLogin();
				return true;
			} else if (msg.type() == MessageType.DELETE_CHR_RESP) {
				DeleteChrResp deleteChrResp = (DeleteChrResp) msg;
				String tip = null;
				if (deleteChrResp.code == 0) {
					LoginResp.Role role0 = null;
					LoginResp.Role role1 = null;
					if (App.Roles != null && App.Roles.length > 0 && !App.Roles[0].name.equals(App.LastName)) {
						role0 = App.Roles[0];
					}
					if (App.Roles != null && App.Roles.length > 1 && !App.Roles[1].name.equals(App.LastName)) {
						role1 = App.Roles[1];
					}
					App.Roles = null;
					App.LastName = null;
					if (role0 != null) {
						App.Roles = new LoginResp.Role[1];
						App.Roles[0] = role0;
						App.LastName = role0.name;
					}
					if (role1 != null) {
						App.Roles = new LoginResp.Role[1];
						App.Roles[0] = role1;
						App.LastName = role1.name;
					}
					loadRoles();
				} else if (deleteChrResp.code == 1) {
					tip = "角色不存在";
				} else if (deleteChrResp.code == 2) {
					tip = "角色被锁定";
				}
				if (deleteChrResp.serverTip != null)
					tip = deleteChrResp.serverTip;
				if (tip != null) {
					DialogUtil.alert(null, tip, null);
				}
				return true;
			}
			return false;
		});

		//super.render(delta);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

}