package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 登陆请求
 * 
 * @author linxing
 *
 */
public final class LoginReq extends Message {
	
	static {
		deSerializers.put(MessageType.LOGIN_REQ, buffer -> {
			if (!buffer.hasRemaining())
				return null;
			String una = unpackString(buffer);
			String psw = unpackString(buffer);
			return new LoginReq(una, psw);
		});
	}

	@Override
	public MessageType type() {
		return MessageType.LOGIN_REQ;
	}
	
	public String una;
	public String psw;
	
	public LoginReq(String una, String psw) {
		this.una = una;
		this.psw = psw;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		// 1.用户名
		packString(una, buffer);
		// 2.密码
		packString(psw, buffer);
	}

}
