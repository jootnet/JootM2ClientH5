package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 创建新用户响应
 * 
 * @author linxing
 *
 */
public class NewUserResp extends Message {
	
	static {
		deSerializers.put(MessageType.NEW_USER_RESP, buffer -> {
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			String errorField = unpackString(buffer);
			return new NewUserResp(code, serverTip, errorField);
		});
	}

	@Override
	public MessageType type() {
		return MessageType.NEW_USER_RESP;
	}
	
	/**
	 * 错误码
	 * <br>
	 * 0:成功 1:用户名已️存在 2:字段有错误的值 99:其他
	 */
	public int code;
	/** 服务端返回的提示文字 */
	public String serverTip;
	/** 错误字段名称 */
	public String errorField;
	

	public NewUserResp(int code, String serverTip, String errorField) {
		this.code = code;
		this.serverTip = serverTip;
		this.errorField = errorField;
	}



	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(code);
		packString(serverTip, buffer);
		packString(errorField, buffer);
	}

}
