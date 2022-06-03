package joot.m2.client.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Base64Coder;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.Occupation;
import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.Messages;
import com.github.jootnet.m2.core.net.messages.DeleteChrReq;
import com.github.jootnet.m2.core.net.messages.EnterReq;
import com.github.jootnet.m2.core.net.messages.KickedOut;
import com.github.jootnet.m2.core.net.messages.LoginReq;
import com.github.jootnet.m2.core.net.messages.LogoutReq;
import com.github.jootnet.m2.core.net.messages.ModifyPswReq;
import com.github.jootnet.m2.core.net.messages.NewChrReq;
import com.github.jootnet.m2.core.net.messages.NewUserReq;
import com.github.jootnet.m2.core.net.messages.OutReq;
import com.github.jootnet.m2.core.net.messages.SysInfo;
import com.google.gwt.typedarrays.client.ArrayBufferNative;
import com.google.gwt.typedarrays.client.Int8ArrayNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Int8Array;

import joot.m2.client.App;

/**
 * 网络交互工具类
 * 
 * @author linxing
 *
 */
public final class NetworkUtil {

    /** 接受到的数据 */
	private static List<Message> recvMsgList = new ArrayList<>();
	/** 启用保活的标志 */
	private static boolean keepAliveFlag;
	/** 上次接收到数据的时间 */
	private static long lastRecvTime;
	/** 上一次发送数据的时间 */
	private static long lastSendTime;
    
    @FunctionalInterface
    public interface MessageConsumer {
    	boolean recv(Message msg);
    }
    
    /**
     * 接受并处理消息
     * <br>
     * 如果不是自己能处理的消息，返回false
     * 
     * @param consumer 消息消费者
     */
    public static void recv(MessageConsumer consumer) {
		synchronized (recvMsgList) {
	    	ArrayList<Message> recvMsgList_ = new ArrayList<Message>();
			recvMsgList_.addAll(recvMsgList);
			recvMsgList.clear();
			for (Message msg : recvMsgList_) {
				if (!consumer.recv(msg)) recvMsgList.add(msg);
			}
		}
		if (keepAliveFlag && System.currentTimeMillis() - lastSendTime > 15 * 1000) {
			if (ws != null) {
				try {
					ws.sendString("PING");
				} catch (Exception ex) { }
			}
		}
		if (System.currentTimeMillis() - lastRecvTime > 60 * 1000) {
			// 一分钟超时
			if (ws != null) {
				ws = null;
				DialogUtil.alert(null, "与服务器的连接断开...", () -> {
					Gdx.app.exit();
				});
			}
		}
    }

    private static String wsUrl = null;
	private static Websocket ws = null;
	/**
	 * 使用服务器URL创建网络交互工具类
	 * 
	 * @param url 服务器路径
	 */
	public static void init(String url) {
		wsUrl = url;
	}
	/**
	 * 启动网络交互
	 */
	public static void start() {
		ws = new Websocket(wsUrl);
		ws.setListener(new WebSocketListenerImpl());
		try {
			ws.connect();
			lastRecvTime = System.currentTimeMillis();
			lastSendTime = System.currentTimeMillis();
		} catch (Exception ex) { }
	}
	
	/**
	 * 停止网络交互
	 */
	public static void shutdown() {
		if (ws == null) return;
		ws.close();
		ws = null;
	}
	
	/**
	 * 是否启用保活
	 * <br>
	 * 一般进入游戏画面之后，开启保活，会在闲时与服务器产生交互避免被服务器断开
	 * 
	 * @param flag 是否启用保活
	 */
	public static void keepAlive(boolean flag) {
		keepAliveFlag = flag;
	}
    
    /**
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public static void sendHumActionChange(ChrBasicInfo hum) {
		if (ws == null) return;
    	try {
			ws.sendBinary(Messages.humActionChange(hum).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送登陆
     * 
     * @param una 账号
     * @param psw 密码
     */
    public static void sendLoginReq(String una, String psw) {
		if (ws == null) return;
    	try {
			ws.sendBinary(new LoginReq(una, new String(Base64Coder.encode(MessageDigest.getInstance("MD5").digest(psw.getBytes())))).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送创建用户
     * @param una
     * @param psw
     * @param name
     * @param q1
     * @param a1
     * @param q2
     * @param a2
     * @param tel
     * @param iPhone
     * @param mail
     */
    public static void sendNewUser(String una, String psw, String name, String q1, String a1, String q2, String a2, String tel,
			String iPhone, String mail) {
		if (ws == null) return;
    	try {
    		ws.sendBinary(new NewUserReq(una, new String(Base64Coder.encode(MessageDigest.getInstance("MD5").digest(psw.getBytes())))
    				, name, q1, a1, q2, a2, tel, iPhone,mail).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送修改密码
     * 
     * @param una 用户名
     * @param oldPsw 旧密码
     * @param newPsw 新密码
     */
    public static void sendModifyPsw(String una, String oldPsw, String newPsw) {
		if (ws == null) return;
    	try {
    		ws.sendBinary(new ModifyPswReq(una, new String(Base64Coder.encode(MessageDigest.getInstance("MD5").digest(oldPsw.getBytes())))
    				, new String(Base64Coder.encode(MessageDigest.getInstance("MD5").digest(newPsw.getBytes())))).pack());
    	} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送删除角色
     * 
     * @param nama 角色名称
     */
    public static void sendDelteChr(String nama) {
		if (ws == null) return;
    	try {
    		ws.sendBinary(new DeleteChrReq(nama).pack());
    	} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送创建角色
     * @param name 昵称
     * @param occupation 职业
     * @param gender 性别
     */
    public static void sendNewChr(String name, Occupation occupation, byte gender) {
		if (ws == null) return;
    	try {
    		ws.sendBinary(new NewChrReq(name, occupation, gender).pack());
    	} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送进入游戏
     * 
     * @param chrName 选择的角色昵称
     */
    public static void sendEnterGame(String chrName) {
		if (ws == null) return;
    	try {
			ws.sendBinary(new EnterReq(chrName).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送登出
     */
    public static void sendLogout() {
		if (ws == null) return;
    	try {
			ws.sendBinary(new LogoutReq().pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送离开游戏世界
     */
    public static void sendOut() {
		if (ws == null) return;
    	try {
			ws.sendBinary(new OutReq().pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    private static class WebSocketListenerImpl implements WebSocketListener {

		@Override
		public boolean onOpen(Websocket webSocket) {
			webSocket.sendString("Hello wrold!");
			lastRecvTime = System.currentTimeMillis();
			lastSendTime = System.currentTimeMillis();
			return true;
		}

		@Override
		public boolean onClose(Websocket webSocket, int closeCode, String reason) {
			if (ws != null) {
				ws = null;
				DialogUtil.alert(null, "与服务器的连接断开...", () -> {
					Gdx.app.exit();
				});
			}
			return true;
		}

		@Override
		public boolean onMessage(Websocket webSocket, String packet) {
			lastRecvTime = System.currentTimeMillis();
			return true;
		}

		@Override
		public boolean onMessage(Websocket webSocket, byte[] packet) {
			if (ws == null) return true;
			lastRecvTime = System.currentTimeMillis();
			try {
				synchronized (recvMsgList) {
					Message msg = Message.unpack(ByteBuffer.wrap(packet));
					if (msg.type() == MessageType.SYS_INFO) {
						SysInfo sysInfo = (SysInfo) msg;
						App.timeDiff = System.currentTimeMillis() / 1000 - sysInfo.time;
						App.MapNames = new HashMap<>();
						App.MapMMaps = new HashMap<>();
						for (int i = 0; i < sysInfo.mapCount; ++i) {
							App.MapNames.put(sysInfo.mapNos[i], sysInfo.mapNames[i]);
							App.MapMMaps.put(sysInfo.mapNos[i], sysInfo.mapMMaps[i]);
						}
						return true;
					} else if (msg.type() == MessageType.KICKED_OUT) {
						KickedOut kickedOut = (KickedOut) msg;
						String tip = null;
						if (kickedOut.reason == 1) {
							tip = "账号在其他地方登陆";
						}
						if (kickedOut.serverTip != null) {
							tip = kickedOut.serverTip;
						}
						ws = null;
						DialogUtil.alert(null, tip, () -> {
							Gdx.app.exit();
						});
						return true;
					}
					recvMsgList.add(msg);
				}
			}catch(Exception ex) { }
			return true;
		}

		@Override
		public boolean onError(Websocket webSocket, Throwable error) {
			if (ws != null) {
				ws = null;
				DialogUtil.alert(null, "与服务器的连接断开...", () -> {
					Gdx.app.exit();
				});
			}
			return true;
		}
    	
    }
    
    private static interface WebSocketListener {
        boolean onOpen(Websocket webSocket);
        boolean onClose(Websocket webSocket, int closeCode, String reason);
        boolean onMessage(Websocket webSocket, String packet);
        boolean onMessage(Websocket webSocket, byte[] packet);
        boolean onError(Websocket webSocket, Throwable error);
    }
    private static class WebSocketException extends RuntimeException {
		private static final long serialVersionUID = -4743444289322218150L;
		private static final String DEFAULT_ERROR_MESSAGE = "Error occured on web socket.";

        public WebSocketException(final String message) {
            super(message);
        }

        public WebSocketException(final Throwable cause) {
            super(DEFAULT_ERROR_MESSAGE, cause);
        }

        public WebSocketException(final String message, final Throwable cause) {
            super(message, cause);
        }

		private WebSocketException() {
		}
	}
    
    private static class Websocket {
    	private String url;
    	private WebSocketListener listener;
    	
    	private Websocket(String url) {
    		this.url = url;
    	}
    	
    	private void setListener(WebSocketListener listener) {
    		this.listener = listener;
    	}
    	
    	private void connect() {
    		try {
                createWebSocket(url);
            } catch (final Throwable exception) {
                if (listener != null)
                	listener.onError(this, new WebSocketException("Unable to connect.", exception));
            }
    	}
    	private native void createWebSocket(String url)/*-{
												        var self = this;
												        if(self.ws) {
												        self.ws.close(1001);
												        }
												        self.ws = new WebSocket(url);
												        self.ws.onopen = function() { self.@joot.m2.client.util.NetworkUtil.Websocket::onOpen()(); };
												        self.ws.binaryType = 'arraybuffer';
												        self.ws.onclose = function(event) { self.@joot.m2.client.util.NetworkUtil.Websocket::onClose(ILjava/lang/String;)(event.code, event.reason); };
												        self.ws.onerror = function(error) { self.@joot.m2.client.util.NetworkUtil.Websocket::onError(Ljava/lang/String;Ljava/lang/String;)(error.type,error.toString()); };
												        self.ws.onmessage = function(msg) {
												        if (typeof(msg.data) == 'string' ) {
												        self.@joot.m2.client.util.NetworkUtil.Websocket::onMessage(Ljava/lang/String;)(msg.data);
												        }else{
												        self.@joot.m2.client.util.NetworkUtil.Websocket::onMessage(Lcom/google/gwt/typedarrays/shared/ArrayBuffer;)(msg.data);
												        }
												        }
												        }-*/;
        private void onOpen() {
            if (listener != null)
            	listener.onOpen(this);
        }

        private void onClose(final int closeCode, final String reason) {
            if (listener != null)
            	listener.onClose(this, closeCode, reason);
        }

        private void onError(final String type, final String message) {
            if (listener != null)
            	listener.onError(this, new WebSocketException("An error occurred. Error type: " + type + ", error event message: " + message));
        }

        private void onMessage(final ArrayBuffer arrayBuffer) {
            if (arrayBuffer != null && arrayBuffer.byteLength() > 0) {
                final byte[] message = toByteArray(arrayBuffer);
                if (message.length > 0) {
                    if (listener != null)
                    	listener.onMessage(this, message);
                }
            }
        }

        private void onMessage(final String message) {
            if (message != null && message.length() > 0) {
                if (listener != null)
                	listener.onMessage(this, message);
            }
        }
        
        private void close() throws WebSocketException {
            try {
                nativeClose(1001, null);
            } catch (final Throwable exception) {
            	exception.printStackTrace();
            }
        }

        private native void nativeClose(int code, String reason)/*-{
															var self = this;
                                                            if(self.ws){
                                                            self.ws.close(code,reason);
                                                            }
                                                            }-*/;

        private void sendBinary(final byte[] message) {
            final ArrayBuffer arrayBuffer = ArrayBufferNative.create(message.length);
            final Int8Array array = Int8ArrayNative.create(arrayBuffer);
            array.set(message);
            try {
                sendArrayBuffer(arrayBuffer);
            } catch (final Throwable exception) {
                throw new WebSocketException(exception);
            }
        }

        private native void sendArrayBuffer(ArrayBuffer message)/*-{
																  var self = this;
																  if(self.ws) {
																	  self.ws.send(message);
																  }
                                                                  }-*/;

        private void sendString(final String message) {
            try {
                sendText(message);
            } catch (final Throwable exception) {
                throw new WebSocketException(exception);
            }
        }

        private native void sendText(String message)/*-{
													  var self = this;
                                                      if(self.ws) {
														  self.ws.send(message);
                                                      }
                                                      }-*/;
        
        private static byte[] toByteArray(final ArrayBuffer arrayBuffer) {
            final Int8Array array = Int8ArrayNative.create(arrayBuffer);
            final int length = array.byteLength();
            final byte[] byteArray = new byte[length];
            for (int index = 0; index < length; index++) {
                byteArray[index] = array.get(index);
            }
            return byteArray;
        }
    }

}
