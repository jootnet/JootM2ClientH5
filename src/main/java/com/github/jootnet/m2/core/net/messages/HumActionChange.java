package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.github.jootnet.m2.core.actor.Action;
import com.github.jootnet.m2.core.actor.Direction;
import com.github.jootnet.m2.core.actor.HumActionInfo;
import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 告知玩家动作更改的消息
 */
public final class HumActionChange extends Message {
	
	static {
		deSerializers.put(MessageType.HUM_ACTION_CHANGE, buffer -> {
			if (!buffer.hasRemaining())
				return null;
			String name = unpackString(buffer);
			short x = buffer.getShort();
			short y = buffer.getShort();
			short nx = buffer.getShort();
			short ny = buffer.getShort();
			HumActionInfo humActionInfo = new HumActionInfo();
			unpackHumActionInfo(humActionInfo, buffer);
			return new HumActionChange(name, x, y, nx, ny, humActionInfo);
		});
	}

    @Override
    public MessageType type() {
        return MessageType.HUM_ACTION_CHANGE;
    }
    
    /** 动作发生的玩家名称*/
    public String name;
    /** 获取动作发生时身处的横坐标 */
    public int x;
    /** 获取动作发生时身处的纵坐标 */
    public int y;
    /** 角色当前动作完成之后应该到达的横坐标 */
    public int nextX;
    /** 角色当前动作完成之后应该到达的纵坐标 */
    public int nextY;
    /** 玩家动作 */
    public HumActionInfo action;

    public HumActionChange(String name, int x, int y, int nextX, int nextY, HumActionInfo action) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.nextX = nextX;
        this.nextY = nextY;
        this.action = action;
    }

	@Override
	protected void packCore(DataOutput buffer) throws IOException {// 1.人物姓名
		packString(name, buffer);
		// 2.当前坐标以及动作完成后的坐标
		buffer.writeShort((short) x);
		buffer.writeShort((short) y);
		buffer.writeShort((short) nextX);
		buffer.writeShort((short) nextY);
		// 3.动作
		pack(action, buffer);
	}
	
	
    private static void pack(HumActionInfo info, DataOutput buffer) throws IOException {
    	buffer.writeByte((byte) info.act.ordinal());
    	buffer.writeByte((byte) info.dir.ordinal());
    	buffer.writeShort(info.frameIdx);
    	buffer.writeShort(info.frameCount);
    	buffer.writeShort(info.duration);
    }
    private static void unpackHumActionInfo(HumActionInfo info, ByteBuffer buffer) {
    	byte actOrdinal = buffer.get();
    	for (Action act : Action.values()) {
    		if (act.ordinal() == actOrdinal) {
    			info.act = act;
    			break;
    		}
    	}
    	byte dirOrdinal = buffer.get();
    	for (Direction dir : Direction.values()) {
    		if (dir.ordinal() == dirOrdinal) {
    			info.dir = dir;
    			break;
    		}
    	}
    	info.frameIdx = buffer.getShort();
    	info.frameCount = buffer.getShort();
    	info.duration = buffer.getShort();
    }
}
