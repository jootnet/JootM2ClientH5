package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 离开游戏世界结果
 * 
 * @author LinXing
 *
 */
public class OutResp extends Message {
	
	static {
		deSerializers.put(MessageType.OUT_RESP, buffer -> {
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			LoginResp.Role role = new LoginResp.Role();
			role = new LoginResp.Role();
			role.type = buffer.getInt();
			role.gender = buffer.get();
			role.level = buffer.getInt();
			role.name = unpackString(buffer);
			role.mapNo = unpackString(buffer);
			role.x = buffer.getShort();
			role.y = buffer.getShort();
			return new OutResp(code, serverTip, role);
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
	/**
	 * 角色最新状态
	 */
	public LoginResp.Role role;

	public OutResp(int code, String serverTip, LoginResp.Role role) {
		this.code = code;
		this.serverTip = serverTip;
		this.role = role;
	}

	@Override
	public MessageType type() {
		return MessageType.OUT_RESP;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(code);
		packString(serverTip, buffer);
		buffer.writeInt(role.type);
		buffer.writeByte(role.gender);
		buffer.writeInt(role.level);
		packString(role.name, buffer);
		packString(role.mapNo, buffer);
		buffer.writeShort(role.x);
		buffer.writeShort(role.y);
	}

}
