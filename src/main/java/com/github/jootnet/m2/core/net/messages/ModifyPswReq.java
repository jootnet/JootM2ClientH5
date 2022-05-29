package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 修改密码请求
 * 
 * @author linxing
 *
 */
public class ModifyPswReq extends Message {
	
	static {
		deSerializers.put(MessageType.MODIFY_PSW_REQ, buffer -> {
			String una = unpackString(buffer);
			String oldPsw = unpackString(buffer);
			String newPsw = unpackString(buffer);
			return new ModifyPswReq(una, oldPsw, newPsw);
		});
	}

	/** 用户名 */
	public String una;
	/** 旧密码 */
	public String oldPsw;
	/** 新密码 */
	public String newPsw;
	
	public ModifyPswReq(String una, String oldPsw, String newPsw) {
		this.una = una;
		this.oldPsw = oldPsw;
		this.newPsw = newPsw;
	}

	@Override
	public MessageType type() {
		return MessageType.MODIFY_PSW_REQ;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		packString(una, buffer);
		packString(oldPsw, buffer);
		packString(newPsw, buffer);
	}

}
