package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 退出登录结果
 * 
 * @author LinXing
 *
 */
public class LogoutResp extends Message {
	
	static {
		deSerializers.put(MessageType.LOGOUT_RESP, buffer -> {
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			return new LogoutResp(code, serverTip);
		});
	}
	
	/**
	 * 错误码
	 * <br>
	 * 0 成功 99未知错误
	 */
	public int code;
	/**
	 * 服务端提示信息
	 */
	public String serverTip;

	public LogoutResp(int code, String serverTip) {
		this.code = code;
		this.serverTip = serverTip;
	}

	@Override
	public MessageType type() {
		return MessageType.LOGOUT_RESP;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(code);
		packString(serverTip, buffer);
	}

}
