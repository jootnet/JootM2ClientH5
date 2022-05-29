package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 修改密码返回
 * 
 * @author linxing
 *
 */
public class ModifyPswResp extends Message {
	
	static {
		deSerializers.put(MessageType.MODIFY_PSW_RESP, buffer -> {
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			return new ModifyPswResp(code, serverTip);
		});
	}
	
	/**
	 * 错误码
	 * <br>
	 * 0 成功
	 * 1 用户名或密码错误
	 * 2 用户不存在
	 * 99 其他
	 */
	public int code;
	/** 服务端提示信息 */
	public String serverTip;
	
	public ModifyPswResp(int code, String serverTip) {
		this.code = code;
		this.serverTip = serverTip;
	}

	@Override
	public MessageType type() {
		return MessageType.MODIFY_PSW_RESP;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(code);
		packString(serverTip, buffer);
	}

}
