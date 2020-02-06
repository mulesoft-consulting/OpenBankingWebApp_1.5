package com.mulesoft.openbankingdemo.web.bean;

import java.security.Principal;

public class OpenBankingPrincipal implements Principal {
	
	private String username=null;
	private String accessToken=null;
	
	public OpenBankingPrincipal(String username, String accessToken) {
		this.username=username;
		this.accessToken=accessToken;
	}

	@Override
	public String getName() {
		return getUsername();
	}

	public String getUsername() {
		return username;
	}

	public String getAccessToken() {
		return accessToken;
	}

}
