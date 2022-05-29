package joot.m2.client.ui;

import java.util.Date;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import joot.m2.client.App;
import joot.m2.client.image.Images;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.DialogUtil;
import joot.m2.client.util.FontUtil;
import joot.m2.client.util.NetworkUtil;

/**
 * 
 * 游戏界面下方的状态栏
 * 
 * @author linxing
 *
 */
public final class StatusBar extends WidgetGroup {
	/** 聊天框 */
	private ChatBox chatBox;
	/** 公共聊天显示开关 */
	private CheckBox chkPublicMsg;
	/** 区域喊话开关 */
	private CheckBox chkAreaMsg;
	/** 私聊开关 */
	private CheckBox chkPrivateMsg;
	/** 行会聊天开关 */
	private CheckBox chkGuildMsg;
	/** 自动喊话开关 */
	private CheckBox chkAutoMsg;
	/** 小地图开关 */
	private ImageButton btnMMap;
	/** 交易按钮 */
	private ImageButton btnTrade;
	/** 行会按钮 */
	private ImageButton btnGuild;
	/** 队伍按钮 */
	private ImageButton btnTeam;
	/** 好友列表 */
	private ImageButton btnFriend;
	/** 聊天记录 */
	private ImageButton btnTalkHistory;
	/** 排行榜 */
	private ImageButton btnRankList;
	/** 小退 */
	private ImageButton btnLogout;
	/** 大退 */
	private ImageButton btnExit;
	/** 打开人物属性界面 */
	private ImageButton btnHum;
	/** 打开背包 */
	private ImageButton btnBag;
	/** 打开技能界面 */
	private ImageButton btnSkill;
	/** 开关声音 */
	private ImageButton btnSound;
	/** 打开商店 */
	private ImageButton btnShop;
	/** 血量 */
	private Label lblHp;
	/** 蓝量 */
	private Label lblMp;
	/** 地图信息 */
	private Label lblMapInfo;
	/** 攻击模式 */
	private Label lblAttackMode;
	/** 等级 */
	private Label lblLevel;
	/** 服务器时间 */
	private Label lblTime;
	/** 经验值进度条 */
	private Image imgProgressExp;
	/** 负重进度条 */
	private Image imgProgressBagWeight;
	/**
	 * 空血量图片<br>
	 * 仅死亡时可见
	 */
	private Image imgEmptyHp;
	/** 血量百分比 */
	private Image imgProgressHp;
	/** 蓝量百分比 */
	private Image imgProgressMp;
	/** 日晷 */
	private Image imgClock;
	private TextureRegionDrawable trdMorning;
	private TextureRegionDrawable trdAFNoon;
	private TextureRegionDrawable trdNight;
	private TextureRegionDrawable trdLANight;

	@SuppressWarnings("deprecation")
	@Override
	public void act(float delta) {
		if (initializeComponents()) {
			
			Date now = new Date(System.currentTimeMillis() - App.timeDiff);

			lblTime.setText(now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds());

			switch (now.getHours()) {
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
				// 上午
				imgClock.setDrawable(trdMorning);
				break;
			case 14:
			case 15:
			case 16:
			case 17:
				// 下午
				imgClock.setDrawable(trdAFNoon);
				break;
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
				// 晚上
				imgClock.setDrawable(trdNight);
				break;
			case 23:
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				// 深夜
				imgClock.setDrawable(trdLANight);
				break;
			default:
				break;
			}
		}

		super.act(delta);
	}

	/**
	 * <pre>
	 * &#64;Override
	 * public Actor hit(float x, float y, boolean touchable) {
	 * 	// TODO 把镂空的地方返回false，鼠标点选之后可以走跑
	 * 	return super.hit(x, y, touchable);
	 * }
	 * </pre>
	 */

	/**
	 * 将焦点给到输入框
	 */
	public void focusInput() {
		getStage().setKeyboardFocus(chatBox.txtChat);
	}

	private boolean inited;
	private boolean initializeComponents() {
		if (inited)
			return true;
		M2Texture[] texs = Images.get("prguse3/690", "prguse3/280", "prguse3/281", "prguse3/282", "prguse3/283", "prguse3/284",
				"prguse3/285", "prguse3/286", "prguse3/287", "prguse3/288", "prguse3/289", "prguse/130", "prguse/131",
				"prguse/132", "prguse/133", "prguse/134", "prguse/135", "prguse/128", "prguse/129", "prguse3/34",
				"prguse3/35", "prguse3/36", "prguse3/37", "prguse3/460", "prguse3/461", "prguse/136", "prguse/137",
				"prguse/138", "prguse/139", "prguse/8", "prguse/9", "prguse/10", "prguse/11", "prguse3/307",
				"prguse3/309", "prguse3/310", "prguse/7", "prguse/5", "prguse/4", "prguse/12", "prguse/13", "prguse/14",
				"prguse/15");
		if (texs == null)
			return false;
		int texIdx = 0;
		addActor(new Image(texs[texIdx++]));
		addActor(chatBox = new ChatBox());
		addActor(chkPublicMsg = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
		addActor(chkAreaMsg = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
		addActor(chkPrivateMsg = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
		addActor(chkGuildMsg = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
		addActor(chkAutoMsg = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
		addActor(btnMMap = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnTrade = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnGuild = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));

		addActor(btnTeam = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnFriend = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnTalkHistory = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnRankList = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnLogout = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnExit = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
				new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		addActor(btnHum = new ImageButton(new ImageButtonStyle()));
		btnHum.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		addActor(btnBag = new ImageButton(new ImageButtonStyle()));
		btnBag.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		addActor(btnSkill = new ImageButton(new ImageButtonStyle()));
		btnSkill.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		addActor(btnSound = new ImageButton(new ImageButtonStyle()));
		btnSound.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		addActor(btnShop = new ImageButton(new ImageButtonStyle()));
		btnShop.getStyle().up = new TextureRegionDrawable(texs[texIdx++]);
		btnShop.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
		btnShop.getStyle().down = new TextureRegionDrawable(texs[texIdx++]);
		addActor(lblHp = new Label(App.ChrBasic.hp + "/" + App.ChrBasic.maxHp,
				new LabelStyle(FontUtil.Song_12_all_outline, Color.WHITE)));
		addActor(lblMp = new Label(App.ChrBasic.mp + "/" + App.ChrBasic.maxMp,
				new LabelStyle(FontUtil.Song_12_all_outline, Color.WHITE)));
		addActor(lblMapInfo = new Label(App.MapNames.get(App.MapNo) + " " + App.ChrBasic.x + "," + App.ChrBasic.y,
				new LabelStyle(FontUtil.Song_12_all_outline, Color.WHITE)));
		App.ChrBasic.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("x")) {
				lblMapInfo.setText(App.MapNames.get(App.MapNo) + " " + e.getNewValue() + "," + App.ChrBasic.y);
			}
			if (e.getPropertyName().equals("y")) {
				lblMapInfo.setText(App.MapNames.get(App.MapNo) + " " + App.ChrBasic.x + "," + e.getNewValue());
			}
			// TODO 血量等改变
		});
		addActor(lblAttackMode = new Label("", new LabelStyle(FontUtil.Song_12_all_outline, Color.WHITE)));
		switch (App.ChrPrivate.attackMode) {
		case All: {
			lblAttackMode.setText("[全体攻击模式]");
			break;
		}
		case Team: {
			lblAttackMode.setText("[队伍攻击模式]");
			break;
		}
		case Guild: {
			lblAttackMode.setText("[行会攻击模式]");
			break;
		}
		case None: {
			lblAttackMode.setText("[和平攻击模式]");
			break;
		}
		case Good: {
			lblAttackMode.setText("[善恶攻击模式]");
			break;
		}
		default:
			break;
		}
		addActor(lblLevel = new Label(String.valueOf(App.ChrBasic.level),
				new LabelStyle(FontUtil.Song_12_all_outline, Color.WHITE)));
		addActor(lblTime = new Label("", new LabelStyle(FontUtil.Song_12_all_outline, Color.WHITE)));
		addActor(imgProgressExp = new Image());
		addActor(imgProgressBagWeight = new Image());
		addActor(imgEmptyHp = new Image());
		addActor(imgProgressHp = new Image());
		addActor(imgProgressMp = new Image());

		chatBox.setBounds(194, 0, 636, 157);
		chkPublicMsg.setPosition(176, 116);
		chkAreaMsg.setPosition(176, 96);
		chkPrivateMsg.setPosition(176, 76);
		chkGuildMsg.setPosition(176, 56);
		chkAutoMsg.setPosition(176, 36);
		btnMMap.setPosition(210, 134);
		btnTrade.setPosition(240, 134);
		btnGuild.setPosition(270, 134);
		btnTeam.setPosition(300, 134);
		btnFriend.setPosition(330, 134);
		btnTalkHistory.setPosition(360, 135);
		btnRankList.setPosition(390, 134);
		btnLogout.setPosition(754, 134);
		btnExit.setPosition(784, 134);
		btnHum.setSize(24, 24);
		btnHum.setPosition(867, 166);
		btnBag.setSize(24, 22);
		btnBag.setPosition(906, 188);
		btnSkill.setSize(24, 23);
		btnSkill.setPosition(946, 207);
		btnSound.setSize(24, 23);
		btnSound.setPosition(988, 217);
		btnShop.setSize(36, 38);
		btnShop.setPosition(977, 19);

		lblHp.setAlignment(Align.center);
		lblMp.setAlignment(Align.center);
		lblAttackMode.setAlignment(Align.center);
		lblTime.setAlignment(Align.center);
		lblHp.setPosition(25, 24);
		lblMp.setPosition(88, 24);
		lblMapInfo.setPosition(9, 4);
		lblAttackMode.setPosition(863, 132);
		lblLevel.setPosition(892, 93);
		lblTime.setPosition(895, 15);
		lblHp.setWidth(60);
		lblMp.setWidth(59);
		lblAttackMode.setWidth(64);
		lblLevel.setWidth(24);
		lblTime.setWidth(52);

		imgProgressExp.setPosition(888, 60);
		imgProgressBagWeight.setPosition(888, 27);

		int texIdx_Prguse7 = texIdx;
		if (App.ChrPrivate.exp == 0) {
			imgProgressExp.setDrawable(null);
		} else if (App.ChrPrivate.exp >= App.ChrPrivate.levelUpExp) {
			imgProgressExp.setDrawable(new TextureRegionDrawable(texs[texIdx_Prguse7]));
			imgProgressExp.setSize(76, 13);
		} else {
			int progressWidth = (int) (76 * ((float) App.ChrPrivate.exp / App.ChrPrivate.levelUpExp));
			imgProgressExp
					.setDrawable(new TextureRegionDrawable(new TextureRegion(texs[texIdx_Prguse7], progressWidth, 13)));
			imgProgressExp.setSize(progressWidth, 13);
		}
		if (App.ChrPrivate.bagWeight == 0) {
			imgProgressBagWeight.setDrawable(null);
		} else if (App.ChrPrivate.bagWeight >= App.ChrPrivate.maxBagWeight) {
			imgProgressBagWeight.setDrawable(new TextureRegionDrawable(texs[texIdx_Prguse7]));
			imgProgressBagWeight.setSize(76, 13);
		} else {
			int progressWidth = (int) (76 * ((float) App.ChrPrivate.bagWeight / App.ChrPrivate.maxBagWeight));
			imgProgressBagWeight
					.setDrawable(new TextureRegionDrawable(new TextureRegion(texs[texIdx_Prguse7], progressWidth, 13)));
			imgProgressBagWeight.setSize(progressWidth, 13);
		}
		texIdx++;
		imgEmptyHp.setPosition(38, 69);
		imgEmptyHp.setSize(96, 92);
		imgEmptyHp.setVisible(false);
		imgEmptyHp.setDrawable(new TextureRegionDrawable(texs[texIdx++]));
		int texIdx_Prguse4 = texIdx;
		imgProgressHp.setPosition(40, 69);
		if (App.ChrBasic.hp == 0) {
			imgProgressHp.setDrawable(null);
		} else if (App.ChrBasic.hp >= App.ChrBasic.maxHp) {
			imgProgressHp.setDrawable(new TextureRegionDrawable(new TextureRegion(texs[texIdx_Prguse4], 42, 90)));
			imgProgressHp.setSize(42, 90);
		} else {
			int progressHeight = (int) (90 * ((float) App.ChrBasic.hp / App.ChrBasic.maxHp));
			imgProgressHp.setDrawable(
					new TextureRegionDrawable(new TextureRegion(texs[texIdx_Prguse4], 42, progressHeight)));
			imgProgressHp.setSize(42, progressHeight);
		}

		imgProgressMp.setPosition(87, 69);
		if (App.ChrBasic.mp == 0) {
			imgProgressMp.setDrawable(null);
		} else if (App.ChrBasic.mp >= App.ChrBasic.maxMp) {
			imgProgressMp
					.setDrawable(new TextureRegionDrawable(new TextureRegion(texs[texIdx_Prguse4], 48, 0, 44, 90)));
			imgProgressMp.setSize(44, 90);
		} else {
			int progressHeight = (int) (90 * ((float) App.ChrBasic.mp / App.ChrBasic.maxMp));
			imgProgressMp.setDrawable(new TextureRegionDrawable(
					new TextureRegion(texs[texIdx_Prguse4], 48, 90 - progressHeight, 44, progressHeight)));
			imgProgressMp.setSize(44, progressHeight);
		}
		texIdx++;
		addActor(imgClock = new Image());
		trdMorning = new TextureRegionDrawable(texs[texIdx++]);
		trdAFNoon = new TextureRegionDrawable(texs[texIdx++]);
		trdNight = new TextureRegionDrawable(texs[texIdx++]);
		trdLANight = new TextureRegionDrawable(texs[texIdx++]);
		imgClock.setSize(32, 54);
		imgClock.setPosition(972, 118);

		btnLogout.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				NetworkUtil.sendOut();
			}

		});

		btnExit.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				DialogUtil.confirm(null, "确定要退出吗？", () -> {
					NetworkUtil.sendLogout();
				});
			}

		});

		inited = true;
		return true;
	}
}
