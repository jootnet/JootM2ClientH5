package com.github.jootnet.m2.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.typedarrays.client.Int8ArrayNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Int8Array;

public final class NetworkUtil {
	/** 正在执行的http请求集合 */
	private static List<XmlHttpRequest> xmlHttpRequests = new ArrayList<>();

	
	/**
	 * 发送http请求
	 */
	public static void sendHttpRequest(HttpRequest req, HttpResponseListener responseCallback) {
		xmlHttpRequests.add(new XmlHttpRequest(req, responseCallback));
	}
    
    public static class HttpRequest {
    	private Map<String, String> headers = new HashMap<>();
    	private String url;
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
    
    public static interface HttpResponseListener {
    	void recvHeaders(Map<String, String> headers);
    	
    	void onLoad(ArrayBuffer message);
    	void onLoad(String message);
    	
    	void onError();
    	void onTimeout();
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
			    								self.xhr.ontimeout = function(event) { self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onTimeout()(); };
			    								self.xhr.onerror = function(event) { self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onError()(); };
			    								self.xhr.onload = function(event) {
                                                    if (self.xhr.status == 404) {
                                                        console.log("404");
													}
			    									self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::recvHeaders(Ljava/lang/String;)(self.xhr.getAllResponseHeaders());
			    									if (self.xhr.responseType == 'arraybuffer') {
			    										self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onLoad(Lcom/google/gwt/typedarrays/shared/ArrayBuffer;)(self.xhr.response);
			    									} else if (self.xhr.responseType == 'blob') {
														console.log("blob");
			    									} else {
														self.@com.github.jootnet.m2.core.NetworkUtil.XmlHttpRequest::onLoad(Ljava/lang/String;)(self.xhr.response);
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
    	
    	private void onTimeout() {
    		xmlHttpRequests.remove(this);
    		if (respListener != null)
    			respListener.onTimeout();
    	}
    	private void onLoad(String message) {
    		xmlHttpRequests.remove(this);
    		if (respListener != null)
    			respListener.onLoad(message);
    	}
    	private void onLoad(ArrayBuffer arrayBuffer) {
    		xmlHttpRequests.remove(this);
    		if (arrayBuffer != null && arrayBuffer.byteLength() > 0) {
	    		if (respListener != null)
	    			respListener.onLoad(arrayBuffer);
    		}
    	}
    	private void onError() {
    		xmlHttpRequests.remove(this);
    		if (respListener != null)
    			respListener.onError();
    	}
    	private void recvHeaders(String headers) {
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
    	    			respListener.recvHeaders(recvedHeaders);
    			}
    		}
    	}
    }
}
