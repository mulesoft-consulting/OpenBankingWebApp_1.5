package com.mulesoft.openbankingdemo.web.bean;

import java.util.HashMap;
import java.util.Map;

public class ApplicationProperties {
	
	private String deviceToken;
	private String clientID;
	private String clientSecret;
	private String responseType;
	private String grantType;
	private String redirectURL;
	private EndpointProperties authenticationURL;
	private EndpointProperties authorizationURL;
	private EndpointProperties tokenURL;
	private EndpointProperties temenosAccountURL;
	private EndpointProperties tokenValidateURL;
	private EndpointProperties temenosFundTransferURL;
	private EndpointProperties aggregateSFDCTemenosURL;
	private EndpointProperties temenosAccountSAPIURL;
	
	private String temenosAPIKey;
	private String temenosAccountProductType;
	private Map<String, String> temenosCustomerIDDetails=null;
	
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getRedirectURL() {
		return redirectURL;
	}
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	public EndpointProperties getAuthenticationURL() {
		return authenticationURL;
	}
	public void setAuthenticationURL(EndpointProperties authenticationURL) {
		this.authenticationURL = authenticationURL;
	}
	public EndpointProperties getAuthorizationURL() {
		return authorizationURL;
	}
	public void setAuthorizationURL(EndpointProperties authorizationURL) {
		this.authorizationURL = authorizationURL;
	}
	public EndpointProperties getTokenURL() {
		return tokenURL;
	}
	public void setTokenURL(EndpointProperties tokenURL) {
		this.tokenURL = tokenURL;
	}
	public EndpointProperties getTemenosAccountURL() {
		return temenosAccountURL;
	}
	public void setTemenosAccountURL(EndpointProperties temenosAccountURL) {
		this.temenosAccountURL = temenosAccountURL;
	}
	public String getTemenosAPIKey() {
		return temenosAPIKey;
	}
	public void setTemenosAPIKey(String temenosAPIKey) {
		this.temenosAPIKey = temenosAPIKey;
	}
	public Map<String, String> getTemenosCustomerIDDetails() {
		return temenosCustomerIDDetails;
	}
	public void setTemenosCustomerIDDetails(Map<String, String> temenosAccountDetails) {
		this.temenosCustomerIDDetails = temenosAccountDetails;
	}
	public void addTemenosCustomerIDDetails(String username, String customerID) {
		if(temenosCustomerIDDetails==null) {
			temenosCustomerIDDetails=new HashMap<String, String>();
		}
		temenosCustomerIDDetails.put(username, customerID);
	}
	public EndpointProperties getTokenValidateURL() {
		return tokenValidateURL;
	}
	public void setTokenValidateURL(EndpointProperties tokenValidateURL) {
		this.tokenValidateURL = tokenValidateURL;
	}
	public EndpointProperties getTemenosFundTransferURL() {
		return temenosFundTransferURL;
	}
	public void setTemenosFundTransferURL(EndpointProperties temenosFundTransferURL) {
		this.temenosFundTransferURL = temenosFundTransferURL;
	}
	public String getTemenosAccountProductType() {
		return temenosAccountProductType;
	}
	public void setTemenosAccountProductType(String temenosAccountProductType) {
		this.temenosAccountProductType = temenosAccountProductType;
	}
	public EndpointProperties getAggregateSFDCTemenosURL() {
		return aggregateSFDCTemenosURL;
	}
	public void setAggregateSFDCTemenosURL(EndpointProperties aggregateSFDCTemenosURL) {
		this.aggregateSFDCTemenosURL = aggregateSFDCTemenosURL;
	}
	public EndpointProperties getTemenosAccountSAPIURL() {
		return temenosAccountSAPIURL;
	}
	public void setTemenosAccountSAPIURL(EndpointProperties temenosAccountSAPIURL) {
		this.temenosAccountSAPIURL = temenosAccountSAPIURL;
	}
	
}
