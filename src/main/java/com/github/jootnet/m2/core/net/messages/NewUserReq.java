package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 创建新用户请求
 * 
 * @author linxing
 *
 */
public class NewUserReq extends Message {
	
	static {
		deSerializers.put(MessageType.NEW_USER_REQ, buffer -> {
			String una = unpackString(buffer);
			String psw = unpackString(buffer);
			String name = unpackString(buffer);
			String q1 = unpackString(buffer);
			String a1 = unpackString(buffer);
			String q2 = unpackString(buffer);
			String a2 = unpackString(buffer);
			String tel = unpackString(buffer);
			String iPhone = unpackString(buffer);
			String mail = unpackString(buffer);
			return new NewUserReq(una, psw, name, q1, a1, q2, a2, tel, iPhone, mail);
		});
	}

	@Override
	public MessageType type() {
		return MessageType.NEW_USER_REQ;
	}
	
	/** 登陆用户名 */
	public String una;
	/** 登陆密码 */
	public String psw;
	/** 姓名 */
	public String name;
	/** 密码找回问题1 */
	public String q1;
	/** 密码找回答案1 */
	public String a1;
	/** 密码找回问题2 */
	public String q2;
	/** 密码找回答案2 */
	public String a2;
	/** 固定电话 */
	public String tel;
	/** 手机号码 */
	public String iPhone;
	/** 邮箱 */
	public String mail;

	public NewUserReq(String una, String psw, String name, String q1, String a1, String q2, String a2, String tel,
			String iPhone, String mail) {
		this.una = una;
		this.psw = psw;
		this.name = name;
		this.q1 = q1;
		this.a1 = a1;
		this.q2 = q2;
		this.a2 = a2;
		this.tel = tel;
		this.iPhone = iPhone;
		this.mail = mail;
	}


	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		packString(una, buffer);
		packString(psw, buffer);
		packString(name, buffer);
		packString(q1, buffer);
		packString(a1, buffer);
		packString(q2, buffer);
		packString(a2, buffer);
		packString(tel, buffer);
		packString(iPhone, buffer);
		packString(mail, buffer);
	}

}
