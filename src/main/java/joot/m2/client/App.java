package joot.m2.client;

import java.util.Map;

import com.badlogic.gdx.Game;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.ChrPrivateInfo;
import com.github.jootnet.m2.core.actor.ChrPublicInfo;
import com.github.jootnet.m2.core.net.messages.LoginResp;

import joot.m2.client.image.Images;
import joot.m2.client.map.Maps;
import joot.m2.client.scene.ChrSelScene;
import joot.m2.client.scene.GameScene;
import joot.m2.client.scene.LoginScene;
import joot.m2.client.util.NetworkUtil;

/**
 * 游戏入口
 * 
 * @author linxing
 *
 */
public class App extends Game {
	private static App me = null;
	/** 账号已有的角色列表 */
	public static LoginResp.Role[] Roles;
	/** 上次登录的昵称 */
	public static String LastName;
	/** 地图编号 */
	public static String MapNo;
	/** 与服务器时间差（秒） */
	public static long timeDiff;
	// 地图信息
	public static Map<String, String> MapNames;
	public static Map<String, Integer> MapMMaps;
	// 当前进入游戏的角色信息
	public static ChrBasicInfo ChrBasic;
	public static ChrPublicInfo ChrPublic;
	public static ChrPrivateInfo ChrPrivate;
	/**
	 * 是否平滑移动
	 */
	public static boolean SmoothMoving = false;
	
	// 登陆场景
	private LoginScene loginScene;
	// 角色选择场景
	private ChrSelScene chrSelScene;
	// 游戏场景
	private GameScene gameScene;

	public App() {
		me = this;
	}
	
	@Override
	public void create() {
		NetworkUtil.init("ws://127.0.0.1:55842/m2");
		NetworkUtil.start();
		Maps.init("http://127.0.0.1:8080/");
		Images.init("http://127.0.0.1:8080/");
		loginScene = new LoginScene();
		chrSelScene = new ChrSelScene();
		gameScene = new GameScene();
		setScreen(loginScene);
	}

	/**
	 * 去到登陆界面
	 */
	public static void toLogin() {
		Roles = null;
		LastName = null;
		MapNo = null;
		ChrBasic = null;
		ChrPublic = null;
		ChrPrivate = null;
		NetworkUtil.keepAlive(false);
		me.setScreen(me.loginScene);
	}
	
	/**
	 * 去到角色选择场景
	 */
	public static void toChrSel() {
		MapNo = null;
		ChrBasic = null;
		ChrPublic = null;
		ChrPrivate = null;
		NetworkUtil.keepAlive(false);
		me.setScreen(me.chrSelScene);
	}
	
	/**
	 * 去到游戏场景
	 */
	public static void toGame() {
		NetworkUtil.keepAlive(true);
		me.setScreen(me.gameScene);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		NetworkUtil.shutdown();
	}
}
