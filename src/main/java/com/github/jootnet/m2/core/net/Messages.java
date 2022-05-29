package com.github.jootnet.m2.core.net;

import com.github.jootnet.m2.core.actor.Action;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.net.messages.HumActionChange;

/**
 * 消息工具类
 */
public final class Messages {
    
    /**
     * 为人物新动作创建消息
     * 
     * @param hum 人物
     * @return 人物动作更新消息
     */
    public static Message humActionChange(ChrBasicInfo hum) {
        int step = 1;
		if (hum.action.act == Action.Run) step++;
        int nx = hum.x;
        int ny = hum.y;
        switch (hum.action.dir) {
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
        return new HumActionChange(hum.name, hum.x, hum.y, nx, ny, hum.action);
    }
}
