package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.actor.Occupation;
import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 创建新角色请求
 * 
 * @author linxing
 *
 */
public class NewChrReq extends Message {
	
	static {
		deSerializers.put(MessageType.NEW_CHR_REQ, buffer -> {
			String name = unpackString(buffer);
			int typeId = buffer.getInt();
			Occupation occupation = Occupation.warrior;
			for (Occupation item : Occupation.values()) {
				if (item.ordinal() == typeId) {
					occupation = item;
					break;
				}
			}
			byte gender = buffer.get();
			return new NewChrReq(name, occupation, gender);
		});
	}

	@Override
	public MessageType type() {
		return MessageType.NEW_CHR_REQ;
	}
	
	/** 昵称 */
	public String name;
	/** 职业 */
	public Occupation occupation;
	/** 性别<br>0:男 1:女 */
	public byte gender;

	public NewChrReq(String name, Occupation occupation, byte gender) {
		this.name = name;
		this.occupation = occupation;
		this.gender = gender;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		packString(name, buffer);
		buffer.writeInt(occupation.ordinal());
		buffer.writeByte(gender);
	}

}
