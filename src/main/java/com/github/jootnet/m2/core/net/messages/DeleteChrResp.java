package com.github.jootnet.m2.core.net.messages;

import java.io.DataOutput;
import java.io.IOException;

import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;

/**
 * 删除角色返回
 * 
 * @author linxing
 *
 */
public class DeleteChrResp extends Message {
	
	static {
		deSerializers.put(MessageType.DELETE_CHR_RESP, buffer -> {
			int code = buffer.getInt();
			String serverTip = unpackString(buffer);
			return new DeleteChrResp(code, serverTip);
		});
	}
	
	/**
	 * 错误码
	 * <br>
	 * 0 成功
	 * 1 角色不存在
	 * 2 角色被锁定
	 * 99 其他
	 */
	public int code;
	/** 服务端提示信息 */
	public String serverTip;
	
	public DeleteChrResp(int code, String serverTip) {
		this.code = code;
		this.serverTip = serverTip;
	}

	@Override
	public MessageType type() {
		return MessageType.DELETE_CHR_RESP;
	}

	@Override
	protected void packCore(DataOutput buffer) throws IOException {
		buffer.writeInt(code);
		packString(serverTip, buffer);
	}

}
