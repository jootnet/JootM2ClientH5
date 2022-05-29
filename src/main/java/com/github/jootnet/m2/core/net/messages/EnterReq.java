package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 进入游戏
 */
public class EnterReq extends Message {
	
	static {
		deSerializers.put(MessageType.ENTER_REQ, buffer -> {
			if (!buffer.hasRemaining())
				return null;
			String chrName = unpackString(buffer);
			return new EnterReq(chrName);
		});
	}

	@Override
	public MessageType type() {
		return MessageType.ENTER_REQ;
	}

	/** 选择进入游戏的角色昵称 */
	public String chrName;
	
	public EnterReq(String chrName) {
		this.chrName = chrName;
	}
	
	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		packString(chrName, buffer);
	}
}
