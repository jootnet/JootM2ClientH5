package com.github.jootnet.m2.core.actor;

/**
 * 人物衣服贴图信息
 * <br>
 * 表示一个动作在一个衣服贴图（600副）中的位置
 */
public final class HumActionInfo {
    /** 动作类型 */
    public Action act;
    /** 方向 */
    public Direction dir;
    /** 第一幅贴图起始位置（在衣服的600张图中） */
    public short frameIdx;
    /** 动作贴图数量 */
    public short frameCount;
    /** 每张图持续时间（毫秒） */
    public short duration;
    
    @Override
    public boolean equals(Object obj) {
    	HumActionInfo that = (HumActionInfo) obj;
    	if (that == null) return false;
    	if (this.act != that.act) return false;
    	if (this.dir != that.dir) return false;
    	if (this.frameIdx != that.frameIdx) return false;
    	if (this.frameCount != that.frameCount) return false;
    	if (this.duration != that.duration) return false;
    	return true;
    }
}
