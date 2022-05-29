package com.github.jootnet.m2.core.actor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 人物公开属性
 * <br>
 * 这些属性可以被近身玩家通过ctrl+右键方式查看
 * 
 * @author linxing
 *
 */
public final class ChrPublicInfo {

	/** 攻击力 */
	public int attackPoint;
	/** 攻击力上限 */
	public int maxAttackPoint;
	/** 魔法攻击力 */
	public int magicAttackPoint;
	/** 魔法攻击力上限 */
	public int maxMagicAttackPoint;
	/** 道术攻击力 */
	public int taositAttackPoint;
	/** 道术攻击力上限 */
	public int maxTaositAttackPoint;
	/** 防御力 */
	public int defensePoint;
	/** 防御力上限 */
	public int maxDefensePoint;
	/** 魔法防御力 */
	public int magicDefensePoint;
	/** 魔法防御力上限 */
	public int maxMagicDefensePoint;
	
	public ChrPublicInfo(int attackPoint, int maxAttackPoint, int magicAttackPoint, int maxMagicAttackPoint,
			int taositAttackPoint, int maxTaositAttackPoint, int defensePoint, int maxDefensePoint,
			int magicDefensePoint, int maxMagicDefensePoint) {
		this.attackPoint = attackPoint;
		this.maxAttackPoint = maxAttackPoint;
		this.magicAttackPoint = magicAttackPoint;
		this.maxMagicAttackPoint = maxMagicAttackPoint;
		this.taositAttackPoint = taositAttackPoint;
		this.maxTaositAttackPoint = maxTaositAttackPoint;
		this.defensePoint = defensePoint;
		this.maxDefensePoint = maxDefensePoint;
		this.magicDefensePoint = magicDefensePoint;
		this.maxMagicDefensePoint = maxMagicDefensePoint;
	}

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.addPropertyChangeListener(listener); 
	}
    
	public void removePropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.removePropertyChangeListener(listener); 
	}

	public ChrPublicInfo setAttackPoint(int attackPoint) {
		if (this.attackPoint != attackPoint)
			propertyChangeSupport.firePropertyChange("attackPoint", this.attackPoint, attackPoint);
		this.attackPoint = attackPoint;
		return this;
	}

	public ChrPublicInfo setMaxAttackPoint(int maxAttackPoint) {
		if (this.maxAttackPoint != maxAttackPoint)
			propertyChangeSupport.firePropertyChange("maxAttackPoint", this.maxAttackPoint, maxAttackPoint);
		this.maxAttackPoint = maxAttackPoint;
		return this;
	}

	public ChrPublicInfo setMagicAttackPoint(int magicAttackPoint) {
		if (this.magicAttackPoint != magicAttackPoint)
			propertyChangeSupport.firePropertyChange("magicAttackPoint", this.magicAttackPoint, magicAttackPoint);
		this.magicAttackPoint = magicAttackPoint;
		return this;
	}

	public ChrPublicInfo setMaxMagicAttackPoint(int maxMagicAttackPoint) {
		if (this.maxMagicAttackPoint != maxMagicAttackPoint)
			propertyChangeSupport.firePropertyChange("maxMagicAttackPoint", this.maxMagicAttackPoint, maxMagicAttackPoint);
		this.maxMagicAttackPoint = maxMagicAttackPoint;
		return this;
	}

	public ChrPublicInfo setTaositAttackPoint(int taositAttackPoint) {
		if (this.taositAttackPoint != taositAttackPoint)
			propertyChangeSupport.firePropertyChange("taositAttackPoint", this.taositAttackPoint, taositAttackPoint);
		this.taositAttackPoint = taositAttackPoint;
		return this;
	}

	public ChrPublicInfo setMaxTaositAttackPoint(int maxTaositAttackPoint) {
		if (this.maxTaositAttackPoint != maxTaositAttackPoint)
			propertyChangeSupport.firePropertyChange("maxTaositAttackPoint", this.maxTaositAttackPoint, maxTaositAttackPoint);
		this.maxTaositAttackPoint = maxTaositAttackPoint;
		return this;
	}

	public ChrPublicInfo setDefensePoint(int defensePoint) {
		if (this.defensePoint != defensePoint)
			propertyChangeSupport.firePropertyChange("defensePoint", this.defensePoint, defensePoint);
		this.defensePoint = defensePoint;
		return this;
	}

	public ChrPublicInfo setMaxDefensePoint(int maxDefensePoint) {
		if (this.maxDefensePoint != maxDefensePoint)
			propertyChangeSupport.firePropertyChange("maxDefensePoint", this.maxDefensePoint, maxDefensePoint);
		this.maxDefensePoint = maxDefensePoint;
		return this;
	}

	public ChrPublicInfo setMagicDefensePoint(int magicDefensePoint) {
		if (this.magicDefensePoint != magicDefensePoint)
			propertyChangeSupport.firePropertyChange("magicDefensePoint", this.magicDefensePoint, magicDefensePoint);
		this.magicDefensePoint = magicDefensePoint;
		return this;
	}

	public ChrPublicInfo setMaxMagicDefensePoint(int maxMagicDefensePoint) {
		if (this.maxMagicDefensePoint != maxMagicDefensePoint)
			propertyChangeSupport.firePropertyChange("maxMagicDefensePoint", this.maxMagicDefensePoint, maxMagicDefensePoint);
		this.maxMagicDefensePoint = maxMagicDefensePoint;
		return this;
	}
	
	
}
