package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 退出登录
 * <br>
 * 回到登录界面
 * 
 * @author LinXing
 *
 */
public class LogoutReq extends Message {
	private static LogoutReq instance = new LogoutReq();
	
	static {
		deSerializers.put(MessageType.LOGOUT_REQ, buffer -> {
			return instance;
		});
	}

	@Override
	public MessageType type() {
		return MessageType.LOGOUT_REQ;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {

	}

}
