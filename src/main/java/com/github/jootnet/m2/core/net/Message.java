package com.github.jootnet.m2.core.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.github.jootnet.m2.core.net.messages.DeleteChrReq;
import com.github.jootnet.m2.core.net.messages.DeleteChrResp;
import com.github.jootnet.m2.core.net.messages.EnterReq;
import com.github.jootnet.m2.core.net.messages.EnterResp;
import com.github.jootnet.m2.core.net.messages.HumActionChange;
import com.github.jootnet.m2.core.net.messages.KickedOut;
import com.github.jootnet.m2.core.net.messages.LoginReq;
import com.github.jootnet.m2.core.net.messages.LoginResp;
import com.github.jootnet.m2.core.net.messages.LogoutReq;
import com.github.jootnet.m2.core.net.messages.LogoutResp;
import com.github.jootnet.m2.core.net.messages.ModifyPswReq;
import com.github.jootnet.m2.core.net.messages.ModifyPswResp;
import com.github.jootnet.m2.core.net.messages.NewChrReq;
import com.github.jootnet.m2.core.net.messages.NewChrResp;
import com.github.jootnet.m2.core.net.messages.NewUserReq;
import com.github.jootnet.m2.core.net.messages.NewUserResp;
import com.github.jootnet.m2.core.net.messages.OutReq;
import com.github.jootnet.m2.core.net.messages.OutResp;
import com.github.jootnet.m2.core.net.messages.SysInfo;

public abstract class Message {
	@FunctionalInterface
	public interface MessageDeSerializer {
		Message unpack(ByteBuffer buffer);
	}
	protected static Map<MessageType, MessageDeSerializer> deSerializers = new HashMap<>();
	
	static {
		new DeleteChrReq(null);
		new DeleteChrReq(null);
		new DeleteChrResp(0, null);
		new EnterReq(null);
		new EnterResp(null, null, null, null);
		new HumActionChange(null, 0, 0, 0, 0, null);
		new KickedOut(0, null);
		new LoginReq(null, null);
		new LoginResp(0, null, null, null);
		new LogoutReq();
		new LogoutResp(0, null);
		new ModifyPswReq(null, null, null);
		new ModifyPswResp(0, null);
		new NewChrReq(null, null, (byte) 0);
		new NewChrResp(0, null, null);
		new NewUserReq(null, null, null, null, null, null, null, null, null, null);
		new NewUserResp(0, null, null);
		new OutReq();
		new OutResp(0, null, null);
		new SysInfo(0, 0, null, null, null);
	}
	
    /**
     * 获取消息类型
     * 
     * @return 消息类型
     * @see MessageType
     */
    public abstract MessageType type();
	
	/**
	 * 将当前对象打包为字节数组
	 * 
	 * @return 序列化后得数据
	 * @throws IOException 
	 */
	public byte[] pack() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(stream);
		// 0.类型
		buffer.writeInt(type().id());
		packCore(buffer);
		buffer.flush();
		return stream.toByteArray();
	}
	protected abstract void packCore(DataOutput buffer) throws IOException;
	
	/**
	 * 从ByteBuffer中反序列化数据包
	 * 
	 * @param buffer 数据缓冲区
	 * @return 解析出得数据包或null
	 */
	public static Message unpack(ByteBuffer buffer) {
		MessageType type = null;

		int typeId = buffer.getInt();

		for (MessageType msgType : MessageType.values()) {
			if (msgType.id() == typeId) {
				type = msgType;
				break;
			}
		}
		if (type == null) return null;

		if (!deSerializers.containsKey(type)) return null;

		return deSerializers.get(type).unpack(buffer);
	}

    protected static void packString(String str, DataOutput buffer) throws IOException {
    	if (str == null) {
    		buffer.writeByte(0);
    		return;
    	}
    	byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
    	buffer.writeByte((byte) bytes.length);
    	buffer.write(bytes);
    }
    protected static String unpackString(ByteBuffer buffer) {
    	byte bytesLen = buffer.get();
		String str = null;
		if (bytesLen > 0) {
			byte[] bytes = new byte[bytesLen];
			buffer.get(bytes);
			str = new String(bytes, StandardCharsets.UTF_8);
		}
		return str;
    }
}
