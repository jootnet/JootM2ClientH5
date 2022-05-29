package com.github.jootnet.m2.core.actor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 角色基础信息
 * <br>
 * 角色属性中的开放部分
 * <br>
 * 这些属性可以被在同一地图的所有其他玩家知晓；而且必须知晓，不然没法绘制其他角色
 * <br>
 * 包括：昵称/性别/职业/等级/血量/蓝量/穿着/动作
 */
public final class ChrBasicInfo {

	/** 昵称 */
	public String name;
	/** 性别 0:男 1:女 */
	public byte gender;
	/** 职业 */
	public Occupation occupation;
	/** 等级 */
	public int level;
	/** 当前血量 */
	public int hp;
	/** 血量上限 */
	public int maxHp;
	/** 当前蓝量 */
	public int mp;
	/** 蓝量上限 */
	public int maxMp;
	
	/** 衣服文件索引 */
	public int humFileIdx;
	/** 衣服文件内编号 */
	public int humIdx;
	/** 翅膀文件索引 */
	public int humEffectFileIdx;
	/** 翅膀文件内编号 */
	public int humEffectIdx;
	/** 武器文件索引 */
	public int weaponFileIdx;
	/** 武器文件内编号 */
	public int weaponIdx;
	/** 武器特效文件索引 */
	public int weaponEffectFileIdx;
	/** 武器特效文件内编号 */
	public int weaponEffectIdx;
	/** 身处地图x坐标 */
	public int x;
	/** 身处地图y坐标 */
	public int y;
	
	/** 当前动作 */
	public HumActionInfo action;
	/** 动作完成后应该更新的地图坐标x */
	public int nextX;
	/** 动作完成后应该更新的地图坐标y */
	public int nextY;
	
	/** 所在行会名称 */
	public String guildName;

	public ChrBasicInfo(String name, byte gender, Occupation occupation, int level, int hp, int maxHp, int mp, int mxMp,
			int humFileIdx, int humIdx, int humEffectFileIdx, int humEffectIdx, int weaponFileIdx, int weaponIdx,
			int weaponEffectFileIdx, int weaponEffectIdx, int x, int y, String guildName) {
		this.name = name;
		this.gender = gender;
		this.occupation = occupation;
		this.level = level;
		this.hp = hp;
		this.maxHp = maxHp;
		this.mp = mp;
		this.maxMp = mxMp;
		this.humFileIdx = humFileIdx;
		this.humIdx = humIdx;
		this.humEffectFileIdx = humEffectFileIdx;
		this.humEffectIdx = humEffectIdx;
		this.weaponFileIdx = weaponFileIdx;
		this.weaponIdx = weaponIdx;
		this.weaponEffectFileIdx = weaponEffectFileIdx;
		this.weaponEffectIdx = weaponEffectIdx;
		this.x = x;
		this.y = y;
		this.nextX = x;
		this.nextY = y;
		this.action = HumActionInfos.StandSouth;
		this.actionStartTime = System.currentTimeMillis();
		this.actionTick = 1;
		this.actionFrameStartTime = this.actionStartTime;
		this.guildName = guildName;
	}
	
	/** 动作开始时间 */
	public long actionStartTime;
	/** 动作帧号 */
	public short actionTick;
	/** 动作帧开始时间 */
	public long actionFrameStartTime;
	/** 动作造成的像素偏移x */
	public int shiftX;
	/** 动作造成的像素偏移y */
	public int shiftY;

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.addPropertyChangeListener(listener); 
	}
    
	public void removePropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.removePropertyChangeListener(listener); 
	}
	/**
	 * 人物移动到地图坐标
	 * 
	 * @param x 身处地图横坐标
	 * @param y 身处地图纵坐标
	 * @return 当前对象
	 */
	public ChrBasicInfo setPosition(int x, int y) {
		int ox = this.x;
		int oy = this.y;
		if (x != this.x)
			propertyChangeSupport.firePropertyChange("x", ox, x);
		if (y != this.y)
			propertyChangeSupport.firePropertyChange("y", oy, y);
		this.x = x;
		this.y = y;
		this.nextX = x;
		this.nextY = y;
		return this;
	}
	/**
	 * 更新角色昵称
	 * 
	 * @param name 新的角色昵称
	 * @return 当前对象
	 */
	public ChrBasicInfo setName(String name) {
		if (!this.name.equals(name))
			propertyChangeSupport.firePropertyChange("name", this.name, name);
		this.name = name;
		return this;
	}
	/**
	 * 更新角色等级
	 * 
	 * @param level 新的人物等级
	 * @return 当前对象
	 */
	public ChrBasicInfo setLevel(int level) {
		if (this.level != level)
			propertyChangeSupport.firePropertyChange("level", this.level, level);
		return this;
	}
	/**
	 * 更新角色血量
	 * 
	 * @param hp 新的角色血量
	 * @return 当前对象
	 */
	public ChrBasicInfo setHP(int hp) {
		if (this.hp != hp)
			propertyChangeSupport.firePropertyChange("hp", this.hp, hp);
		return this;
	}
	/**
	 * 更新角色血量上限
	 * 
	 * @param hp 新的角色血量上限
	 * @return 当前对象
	 */
	public ChrBasicInfo setMaxHP(int maxHp) {
		if (this.maxHp != maxHp)
			propertyChangeSupport.firePropertyChange("maxHp", this.maxHp, maxHp);
		return this;
	}
	/**
	 * 更新角色蓝量
	 * 
	 * @param hp 新的角色蓝量
	 * @return 当前对象
	 */
	public ChrBasicInfo setMP(int mp) {
		if (this.mp != mp)
			propertyChangeSupport.firePropertyChange("mp", this.mp, mp);
		return this;
	}
	/**
	 * 更新角色蓝量上限
	 * 
	 * @param hp 新的角色蓝量上限
	 * @return 当前对象
	 */
	public ChrBasicInfo setMaxMP(int maxMp) {
		if (this.maxMp != maxMp)
			propertyChangeSupport.firePropertyChange("maxMp", this.maxMp, maxMp);
		return this;
	}
	
	/**
	 * 更新玩家动作
	 * 
	 * @param action 玩家当前动作
	 * @return 当前对象
	 */
	public ChrBasicInfo setAction(HumActionInfo action) {
		this.action = action;
		actionTick = 1;
		actionFrameStartTime = System.currentTimeMillis();
		actionStartTime = System.currentTimeMillis();
		return this;
	}
	
	/**
	 * 人物动作渐进
	 * <br>
	 * 计算人物动作是否该前进一帧
	 * 
	 * @param smooth 是否平滑移动
	 * @return 当前对象
	 */
	public ChrBasicInfo act(boolean smooth) {
		// 计算当前动作
		boolean actionDone = false;
		long delta = System.currentTimeMillis() - actionStartTime;
		if (!smooth) {
			if (System.currentTimeMillis() - actionFrameStartTime > action.duration) {
				actionFrameStartTime = System.currentTimeMillis();
				if (++actionTick > action.frameCount) {
					actionDone = true;
				}
			}
		} else {
			if (delta > action.duration * action.frameCount) {
				actionDone = true;
			} else {
				actionTick = (short) Math.min(delta / action.duration + 1, action.frameCount);
			}
		}

		if (actionDone) {
			if (action.act == Action.Walk || action.act == Action.Run) {
				// 走完或跑完了一步，地图中心坐标更新
				int step = 1;
				if (action.act == Action.Run) step++;
	
				int nx = x;
				int ny = y;
				switch (action.dir) {
					case North:
						ny -= step;
						break;
					case NorthEast:
						ny -= step;
						nx += step;
						break;
					case East:
						nx += step;
						break;
					case SouthEast:
						ny += step;
						nx += step;
						break;
					case South:
						ny += step;
						break;
					case SouthWest:
						ny += step;
						nx -= step;
						break;
					case West:
						nx -= step;
						break;
					case NorthWest:
						ny -= step;
						nx -= step;
						break;
	
					default:
						break;
				}
				if (nextX == nx && nextY == ny) { // 允许移动
					setPosition(nx, ny);
				}
			}
			setAction(HumActionInfos.stand(action.dir));
		}

		if (!smooth) {
			shift();
		} else {
			shiftSmooth((int) delta);
		}
		
		return this;
	}
	
	private void shift() {
		// 计算人物偏移
		shiftX = 0;
		shiftY = 0;
		if (action.act == Action.Walk || action.act == Action.Run) {
			int step = action.act == Action.Run ? 2 : 1;

			switch (action.dir) {
			case North:
				shiftY = -32 * actionTick / action.frameCount * step; // 向上偏移
				break;
			case NorthEast:
				shiftY = -32 * actionTick / action.frameCount * step;
				shiftX = 48 * actionTick / action.frameCount * step; // 向右偏移
				break;
			case East:
				shiftX = 48 * actionTick / action.frameCount * step;
				break;
			case SouthEast:
				shiftY = 32 * actionTick / action.frameCount * step;
				shiftX = 48 * actionTick / action.frameCount * step;
				break;
			case South:
				shiftY = 32 * actionTick / action.frameCount * step;
				break;
			case SouthWest:
				shiftY = 32 * actionTick / action.frameCount * step;
				shiftX = -48 * actionTick / action.frameCount * step;
				break;
			case West:
				shiftX = -48 * actionTick / action.frameCount * step;
				break;
			case NorthWest:
				shiftY = -32 * actionTick / action.frameCount * step;
				shiftX = -48 * actionTick / action.frameCount * step;
				break;
			
			default:
				break;
			}
		}
	}
	
	private void shiftSmooth(int delta) {
		// 计算人物偏移（平滑）
		shiftX = 0;
		shiftY = 0;
		if (action.act == Action.Walk || action.act == Action.Run) {
			int step = action.act == Action.Run ? 2 : 1;

			switch (action.dir) {
			case North:
				shiftY = -32 * delta * step / action.frameCount / action.duration; // 向上偏移
				break;
			case NorthEast:
				shiftY = -32 * delta * step / action.frameCount / action.duration;
				shiftX = 48 * delta * step / action.frameCount / action.duration; // 向右偏移
				break;
			case East:
				shiftX = 48 * delta * step / action.frameCount / action.duration;
				break;
			case SouthEast:
				shiftY = 32 * delta * step / action.frameCount / action.duration;
				shiftX = 48 * delta * step / action.frameCount / action.duration;
				break;
			case South:
				shiftY = 32 * delta * step / action.frameCount / action.duration;
				break;
			case SouthWest:
				shiftY = 32 * delta * step / action.frameCount / action.duration;
				shiftX = -48 * delta * step / action.frameCount / action.duration;
				break;
			case West:
				shiftX = -48 * delta * step / action.frameCount / action.duration;
				break;
			case NorthWest:
				shiftY = -32 * delta * step / action.frameCount / action.duration;
				shiftX = -48 * delta * step / action.frameCount / action.duration;
				break;
			
			default:
				break;
			}
		}
	}
}
