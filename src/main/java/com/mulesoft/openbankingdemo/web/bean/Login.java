package com.mulesoft.openbankingdemo.web.bean;

import java.io.Serializable;

public class Login implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5375831948367949961L;
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
