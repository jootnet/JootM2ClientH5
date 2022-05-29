package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 被服务器踢出
 * 
 * @author LinXing
 *
 */
public class KickedOut extends Message {
	
	static {
		deSerializers.put(MessageType.KICKED_OUT, buffer -> {
			int reason = buffer.getInt();
			String serverTip = unpackString(buffer);
			return new KickedOut(reason, serverTip);
		});
	}
	
	/**
	 * 原因
	 * <br>
	 * 1 其他地方登录 99 未知原因
	 */
	public int reason;
	/**
	 * 服务端提示信息
	 */
	public String serverTip;

	public KickedOut(int reason, String serverTip) {
		this.reason = reason;
		this.serverTip = serverTip;
	}

	@Override
	public MessageType type() {
		return MessageType.KICKED_OUT;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(reason);
		packString(serverTip, buffer);
	}

}
