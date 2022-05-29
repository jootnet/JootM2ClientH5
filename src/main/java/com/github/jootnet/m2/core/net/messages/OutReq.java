package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 离开游戏世界
 * <br>
 * 回到选角界面
 * 
 * @author LinXing
 *
 */
public class OutReq extends Message {
	private static OutReq instance = new OutReq();
	
	static {
		deSerializers.put(MessageType.OUT_REQ, buffer -> {
			return instance;
		});
	}

	@Override
	public MessageType type() {
		return MessageType.OUT_REQ;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {

	}

}
