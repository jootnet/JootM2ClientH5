package com.github.jootnet.m2.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.typedarrays.shared.ArrayBuffer;

public final class NetworkUtil {
	/** 正在执行的http请求集合 */
	private static final List<XmlHttpRequest> xmlHttpRequests = new ArrayList<>();

	
	/**
	 * 发送http请求
	 */
	public static void sendHttpRequest(HttpRequest req, HttpResponseListener responseCallback) {
		xmlHttpRequests.add(new XmlHttpRequest(req, responseCallback));
	}
    
    public static class HttpRequest {
    	private final Map<String, String> headers = new HashMap<>();
    	private final String url;
    	private String method = "GET";
    	private Integer rangeStart;
    	private Integer rangeEnd;
    	private boolean isBinary;
    	private int timeoutMilli = 3000;
    	
    	public HttpRequest(String url) {
    		this.url = url;
    	}
    	
    	public HttpRequest setMethod(String method) {
    		this.method = method;
    		return this;
    	}
    	
    	public HttpRequest setBinary() {
    		isBinary = true;
    		return this;
    	}
    	
    	public HttpRequest setTimeout(int milli) {
    		this.timeoutMilli = milli;
    		return this;
    	}
    	
    	public HttpRequest setRange(int rangeStart, int rangeEnd) {
    		this.rangeStart = rangeStart;
    		this.rangeEnd = rangeEnd;
    		return this;
    	}
    	
    	public HttpRequest addHeader(String name, String value) {
    		headers.put(name, value);
    		return this;
    	}
    }
    
    public interface HttpResponseListener {
    	void recvHeaders(Map<String, String> headers, String url);
    	
    	void onLoad(ArrayBuffer message, String url);
    	void onLoad(String message, String url);
    	
    	void onError(String url);
    	void onTimeout(String url);
    }
    
    private static class XmlHttpRequest {
    	private HttpRequest req;
    	private HttpResponseListener respListener;
    	
    	private XmlHttpRequest(HttpRequest req, HttpResponseListener respListener) {
    		this.req = req;
    		this.respListener = respListener;
    		_new(req.isBinary, req.timeoutMilli);
    		_open(req.method, req.url);
    		if (!req.headers.isEmpty()) {
    			for (Map.Entry<String, String> kv : req.headers.entrySet()) {
    				_addHeader(kv.getKey(), kv.getValue());
    			}
    		}
    		if (req.rangeStart != null && req.rangeEnd != null) {
    			_addHeader("Range", "bytes=" + req.rangeStart + "-" + req.rangeEnd);
    		}
    		_send();
    	}
    	
    	private native void _new(boolean binary, int timeout)/*-{
			    								var self = this;
			    								self.xhr = new XMLHttpRequest();
			    								self.xhr.ontimeout = function(event) { self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onTimeout(Ljava/lang/String;)(self.xhr.responseURL); };
			    								self.xhr.onerror = function(event) { self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onError(Ljava/lang/String;)(self.xhr.responseURL); };
			    								self.xhr.onload = function(event) {
			    									self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::recvHeaders(Ljava/lang/String;Ljava/lang/String;)(self.xhr.getAllResponseHeaders(),self.xhr.responseURL);
			    									if (self.xhr.responseType === 'arraybuffer') {
			    										self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onLoad(Lcom/google/gwt/typedarrays/shared/ArrayBuffer;Ljava/lang/String;)(self.xhr.response,self.xhr.responseURL);
			    									} else {
														self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onLoad(Ljava/lang/String;Ljava/lang/String;)(self.xhr.response,self.xhr.responseURL);
													}
												};
												if (binary) {
													self.xhr.responseType = 'arraybuffer';
												}
												self.xhr.timeout = timeout;
			    								}-*/;
    	private native void _addHeader(String name, String value)/*-{
    															var self = this;
    															self.xhr.setRequestHeader(name, value);
    															}-*/;
    	private native void _open(String method, String url)/*-{
    														var self = this;
    														self.xhr.open(method, url);
    														}-*/;
    	private native void _send()/*-{
    								var self = this;
    								self.xhr.send();
    								}-*/;
    	
    	private void onTimeout(String url) {
    		xmlHttpRequests.remove(this);
    		if (respListener != null)
    			respListener.onTimeout(url);
    	}
    	private void onLoad(String message, String url) {
    		xmlHttpRequests.remove(this);
    		if (respListener != null)
    			respListener.onLoad(message,url);
    	}
    	private void onLoad(ArrayBuffer arrayBuffer, String url) {
    		xmlHttpRequests.remove(this);
    		if (arrayBuffer != null && arrayBuffer.byteLength() > 0) {
	    		if (respListener != null)
	    			respListener.onLoad(arrayBuffer,url);
    		}
    	}
    	private void onError(String url) {
    		xmlHttpRequests.remove(this);
    		if (respListener != null)
    			respListener.onError(url);
    	}
    	private void recvHeaders(String headers, String url) {
    		if (!headers.trim().isEmpty()) {
    			Map<String, String> recvedHeaders = new HashMap<>();
    			String[] lines = headers.split("\\r\\n");
    			for (String line : lines) {
    				if (!line.trim().isEmpty()) {
    					String[] kv = line.split(": ");
    					if (kv.length == 2)
    						recvedHeaders.put(kv[0], kv[1]);
    				}
    			}
    			if (!recvedHeaders.isEmpty()) {
    	    		if (respListener != null)
    	    			respListener.recvHeaders(recvedHeaders,url);
    			}
    		}
    	}
    }
}
