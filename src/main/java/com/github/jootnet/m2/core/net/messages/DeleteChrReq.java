package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 删除角色
 * 
 * @author linxing
 *
 */
public class DeleteChrReq extends Message {
	
	static {
		deSerializers.put(MessageType.DELETE_CHR_REQ, buffer -> {
			String name = unpackString(buffer);
			return new DeleteChrReq(name);
		});
	}
	
	/** 角色名称 */
	public String name;

	public DeleteChrReq(String name) {
		this.name = name;
	}

	@Override
	public MessageType type() {
		return MessageType.DELETE_CHR_REQ;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		packString(name, buffer);
	}

}
