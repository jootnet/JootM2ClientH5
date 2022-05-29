package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

public class LoginResp extends Message {
	
	static {
		deSerializers.put(MessageType.LOGIN_RESP, buffer -> {
			if (!buffer.hasRemaining())
				return null;
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			int roleCount = buffer.get();
			LoginResp.Role[] roles = new LoginResp.Role[roleCount];
			for (int i = 0; i < roleCount; ++i) {
				roles[i] = new LoginResp.Role();
				roles[i].type = buffer.getInt();
				roles[i].gender = buffer.get();
				roles[i].level = buffer.getInt();
				roles[i].name = unpackString(buffer);
				roles[i].mapNo = unpackString(buffer);
				roles[i].x = buffer.getShort();
				roles[i].y = buffer.getShort();
			}
			String lastName = unpackString(buffer);
			return new LoginResp(code, serverTip, roles, lastName);
		});
	}
	
	/**
	 * 角色
	 */
	public static class Role {
		public int type; // 0:战士 1:法师 2:道士 3:刺客
		public byte gender; // 0:男 1:女
		public int level; // 等级 1-1000
		public String name; // 昵称
		public String mapNo; // 挂机地图
		public short x; // 挂机x坐标
		public short y; // 挂机x坐标
	}

	@Override
	public MessageType type() {
		return MessageType.LOGIN_RESP;
	}
	/**
	 * 登陆相应错误码
	 * <br>
	 * 0 登陆成功
	 * 1 用户名或密码错误
	 * 2 用户不存在
	 * 3 用户已在其他地方登陆
	 * 99 其他
	 */
	public int code;
	/** 服务端提示信息 */
	public String serverTip;
	/** 账号已有角色列表 */
	public Role[] roles;
	/** 上次进入游戏的昵称 */
	public String lastName;
	
	public LoginResp(int code, String serverTip, Role[] roles, String lastName) {
		this.code = code;
		this.serverTip = serverTip;
		this.roles = roles;
		this.lastName = lastName;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		// 1.错误码
		buffer.writeInt(code);
		// 2.服务端消息
		packString(serverTip, buffer);
		// 3.角色列表
		if (roles != null) {
			buffer.writeByte((byte) roles.length);
			for (LoginResp.Role r : roles) {
				buffer.writeInt(r.type);
				buffer.writeByte(r.gender);
				buffer.writeInt(r.level);
				packString(r.name, buffer);
				packString(r.mapNo, buffer);
				buffer.writeShort(r.x);
				buffer.writeShort(r.y);
			}
		} else {
			buffer.writeByte(0);
		}
		// 4.上次选择的昵称
		packString(lastName, buffer);
	}
}
