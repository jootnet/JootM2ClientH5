package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 创建角色响应
 * 
 * @author linxing
 *
 */
public class NewChrResp extends Message {
	
	static {
		deSerializers.put(MessageType.NEW_CHR_RESP, buffer -> {
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			LoginResp.Role role = null;
			if (buffer.hasRemaining()) {
				role = new LoginResp.Role();
				role.type = buffer.getInt();
				role.gender = buffer.get();
				role.level = buffer.getInt();
				role.name = unpackString(buffer);
				role.mapNo = unpackString(buffer);
				role.x = buffer.getShort();
				role.y = buffer.getShort();
			}
			return new NewChrResp(code, serverTip, role);
		});
	}

	@Override
	public MessageType type() {
		return MessageType.NEW_CHR_RESP;
	}
	
	/**
	 * 错误码
	 * <br>
	 * 0:成功 1:角色已满 2:昵称已存在 3:昵称不合法 99:其他
	 */
	public int code;
	/** 服务端提示信息 */
	public String serverTip;
	/** 创建成功后的角色 */
	public LoginResp.Role role;

	public NewChrResp(int code, String serverTip, LoginResp.Role role) {
		this.code = code;
		this.serverTip = serverTip;
		this.role = role;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(code);
		packString(serverTip, buffer);
		if (role == null) return;
		buffer.writeInt(role.type);
		buffer.writeByte(role.gender);
		buffer.writeInt(role.level);
		packString(role.name, buffer);
		packString(role.mapNo, buffer);
		buffer.writeShort(role.x);
		buffer.writeShort(role.y);
	}

}
