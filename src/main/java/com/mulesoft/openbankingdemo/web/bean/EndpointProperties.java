package com.mulesoft.openbankingdemo.web.bean;

public class EndpointProperties {
	
	private String scheme=null; // this can be HTTP or HTTPS
	private int port=0;
	private String host=null;
	private String path=null;
	private boolean encode=false;
	
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isEncode() {
		return encode;
	}
	public void setEncode(boolean encode) {
		this.encode = encode;
	}
}
