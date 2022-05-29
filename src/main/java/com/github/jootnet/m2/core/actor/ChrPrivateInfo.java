package com.github.jootnet.m2.core.actor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 人物私有属性
 * <br>
 * 这些信息自能自己知晓
 * 
 * @author linxing
 *
 */
public final class ChrPrivateInfo {

	/** 当前经验值 */
	public int exp;
	/** 最大经验值 */
	public int levelUpExp;
	/** 背包重量 */
	public int bagWeight;
	/** 最大背包负重 */
	public int maxBagWeight;
	/** 穿戴重量 */
	public int wearWeight;
	/** 最大可穿戴重量 */
	public int maxWearWeight;
	/** 腕力 */
	public int handWeight;
	/** 最大腕力 */
	public int maxHandWeight;
	/** 攻击模式 */
	public AttackMode attackMode;
	
	public ChrPrivateInfo(int exp, int levelUpExp, int bagWeight, int maxBagWeight, int wearWeight, int maxWearWeight,
			int handWeight, int maxHandWeight, AttackMode attackMode) {
		this.exp = exp;
		this.levelUpExp = levelUpExp;
		this.bagWeight = bagWeight;
		this.maxBagWeight = maxBagWeight;
		this.wearWeight = wearWeight;
		this.maxWearWeight = maxWearWeight;
		this.handWeight = handWeight;
		this.maxHandWeight = maxHandWeight;
		this.attackMode = attackMode;
	}

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.addPropertyChangeListener(listener); 
	}
    
	public void removePropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.removePropertyChangeListener(listener); 
	}
	
	/**
	 * 设置新的经验值
	 * 
	 * @param exp 经验值
	 * @return 当前对象
	 */
	public ChrPrivateInfo setExp(int exp) {
		if (this.exp != exp)
			propertyChangeSupport.firePropertyChange("exp", this.exp, exp);
		this.exp = exp;
		return this;
	}
	/**
	 * 设置新的升级经验值
	 * 
	 * @param levelUpExp 升级经验值
	 * @return 当前对象
	 */
	public ChrPrivateInfo setLevelUpExp(int levelUpExp) {
		if (this.levelUpExp != levelUpExp)
			propertyChangeSupport.firePropertyChange("levelUpExp", this.levelUpExp, levelUpExp);
		this.levelUpExp = levelUpExp;
		return this;
	}
	/**
	 * 设置新的负重
	 * 
	 * @param bagWeight 负重
	 * @return 当前对象
	 */
	public ChrPrivateInfo setBagWeight(int bagWeight) {
		if (this.bagWeight != bagWeight)
			propertyChangeSupport.firePropertyChange("bagWeight", this.bagWeight, bagWeight);
		this.bagWeight = bagWeight;
		return this;
	}
	/**
	 * 设置新的负重上限
	 * 
	 * @param maxBagWeight 负重上限
	 * @return 当前对象
	 */
	public ChrPrivateInfo setMaxBagWeight(int maxBagWeight) {
		if (this.maxBagWeight != maxBagWeight)
			propertyChangeSupport.firePropertyChange("maxBagWeight", this.maxBagWeight, maxBagWeight);
		this.maxBagWeight = maxBagWeight;
		return this;
	}
	/**
	 * 设置新的穿戴重量
	 * 
	 * @param wearWeight 穿戴重量
	 * @return 当前对象
	 */
	public ChrPrivateInfo setWearWeight(int wearWeight) {
		if (this.wearWeight != wearWeight)
			propertyChangeSupport.firePropertyChange("wearWeight", this.wearWeight, wearWeight);
		this.wearWeight = wearWeight;
		return this;
	}
	/**
	 * 设置新的穿戴重量上限
	 * 
	 * @param maxWearWeight 穿戴重量上限
	 * @return 当前对象
	 */
	public ChrPrivateInfo setMaxWearWeight(int maxWearWeight) {
		if (this.maxWearWeight != maxWearWeight)
			propertyChangeSupport.firePropertyChange("maxWearWeight", this.maxWearWeight, maxWearWeight);
		this.maxWearWeight = maxWearWeight;
		return this;
	}
	/**
	 * 设置新的持握重量
	 * 
	 * @param handWeight 持握重量
	 * @return 当前对象
	 */
	public ChrPrivateInfo setHandWeight(int handWeight) {
		if (this.handWeight != handWeight)
			propertyChangeSupport.firePropertyChange("handWeight", this.handWeight, handWeight);
		this.handWeight = handWeight;
		return this;
	}
	/**
	 * 设置新的最大腕力
	 * 
	 * @param maxHandWeight 最大腕力
	 * @return 当前对象
	 */
	public ChrPrivateInfo setMaxHandWeight(int maxHandWeight) {
		if (this.maxHandWeight != maxHandWeight)
			propertyChangeSupport.firePropertyChange("maxHandWeight", this.maxHandWeight, maxHandWeight);
		this.maxHandWeight = maxHandWeight;
		return this;
	}
	/**
	 * 设置新的攻击模式
	 * 
	 * @param attackMode 攻击模式
	 * @return 当前对象
	 */
	public ChrPrivateInfo setAttackMode(AttackMode attackMode) {
		if (this.attackMode != attackMode)
			propertyChangeSupport.firePropertyChange("attackMode", this.attackMode, attackMode);
		this.attackMode = attackMode;
		return this;
	}
}
