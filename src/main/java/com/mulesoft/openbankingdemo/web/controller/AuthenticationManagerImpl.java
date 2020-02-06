package com.mulesoft.openbankingdemo.web.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mulesoft.openbankingdemo.web.AppPropertiesManagerBean;
import com.mulesoft.openbankingdemo.web.Utils;
import com.mulesoft.openbankingdemo.web.bean.OpenBankingPrincipal;


@Component
public class AuthenticationManagerImpl implements AuthenticationManager {

	private static Logger LOGGER = LoggerFactory
		      .getLogger(AuthenticationManagerImpl.class);

	
	@Autowired
	private AppPropertiesManagerBean managerBean;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		

			String username=null;
			if(authentication.getPrincipal() instanceof OpenBankingPrincipal) {
				OpenBankingPrincipal obprincipal = (OpenBankingPrincipal)authentication.getPrincipal();
				String accessTokenInSession=obprincipal.getAccessToken();
				// Checking the token validity now..
				if(!Utils.isAccessTokenValid(accessTokenInSession)) {
					LOGGER.error("THE ACCESS TOKEN="+accessTokenInSession+" IS NOT VALID.. RETURN TO LOGIN");
					throw new AuthenticationException("THE ACCESS TOKEN="+accessTokenInSession+" IS NOT VALID.. RETURN TO LOGIN") {
						
					};
				} else {
					return authentication;
				}
			} else {
				username=(String)authentication.getPrincipal();
			}
			String password=(String)authentication.getCredentials();
			
			if(managerBean==null) {
				managerBean=AppPropertiesManagerBean.getInstance();
			}
			
			// First doing the authenication and getting the session token
			String ssoToken= getSSOSessionToken(username, password);
			// Then getting the authorization for the user+client combination
			String authorizationCode=getAuthorizationCode(ssoToken,username);
			// Getting the access token..
			String accessToken=getAccessToken(authorizationCode);
			
			// TODO Auto-generated method stub
			SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_USER");
			Principal principle=new OpenBankingPrincipal(username, accessToken);
			
		return new UsernamePasswordAuthenticationToken(principle, Arrays.asList(authority));

	}
	
	private String getSSOSessionToken(String username, String password) {
		RestTemplate restTemplate=Utils.getRestTemplate();
		UriComponentsBuilder uriComponentBuilder=UriComponentsBuilder.newInstance().scheme(managerBean.getProperties().getAuthenticationURL().getScheme())
				.host(managerBean.getProperties().getAuthenticationURL().getHost())
				.path(managerBean.getProperties().getAuthenticationURL().getPath())
				.queryParam("username", username)
				.queryParam("password", password)
				.queryParam("deviceToken", managerBean.getProperties().getDeviceToken());
				
				if(managerBean.getProperties().getAuthenticationURL().getPort()>0) {
						uriComponentBuilder.port(managerBean.getProperties().getAuthenticationURL().getPort());
				}
				
				UriComponents uriComponent=uriComponentBuilder.encode().build();

		System.out.println("************** uriComponent.toUriString()="+uriComponent.toUriString());

		
		String response=restTemplate.getForObject(uriComponent.toUriString(), String.class);
		
		LOGGER.info("************** Authentication Response="+response);
		
		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response);
		String sessionToken=(String)parsedJson.get("sessionToken");
		LOGGER.info("*************** SESSION ID="+sessionToken);
		
		return sessionToken;
	}
	
	private String getAuthorizationCode(String sessionToken,String username) {
		RestTemplate restTemplate=Utils.getRestTemplate();
		UriComponentsBuilder uriComponentBuilder=UriComponentsBuilder.newInstance().scheme(managerBean.getProperties().getAuthorizationURL().getScheme())
				.host(managerBean.getProperties().getAuthorizationURL().getHost())
				.path(managerBean.getProperties().getAuthorizationURL().getPath())
				.queryParam("client_id", managerBean.getProperties().getClientID())
				.queryParam("sessionToken", sessionToken)
				.queryParam("response_type", managerBean.getProperties().getResponseType())
				.queryParam("redirect_uri", managerBean.getProperties().getRedirectURL())
				.queryParam("state", username+System.currentTimeMillis());

		if(managerBean.getProperties().getAuthorizationURL().getPort()>0) {
			uriComponentBuilder.port(managerBean.getProperties().getAuthorizationURL().getPort());
		}

		UriComponents uriComponent=uriComponentBuilder.encode().build();

		LOGGER.info("************** uriComponent.toUriString()="+uriComponent.toUriString());

		
		String response=restTemplate.getForObject(uriComponent.toUriString(), String.class);
		
		LOGGER.info("************** Authorization Response="+response);

		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response);
		String authorizationCode=(String)parsedJson.get("authorization_code");
		LOGGER.info("*************** Authorization Code="+sessionToken);

		return authorizationCode;

	}
	
	private String getAccessToken(String authorizationCode) {
		RestTemplate restTemplate=Utils.getRestTemplate();
		UriComponentsBuilder uriComponentBuilder=UriComponentsBuilder.newInstance().scheme(managerBean.getProperties().getTokenURL().getScheme())
				.host(managerBean.getProperties().getTokenURL().getHost())
				.path(managerBean.getProperties().getTokenURL().getPath());
		
		if(managerBean.getProperties().getTokenURL().getPort()>0) {
			uriComponentBuilder.port(managerBean.getProperties().getTokenURL().getPort());
		}

		UriComponents uriComponent=uriComponentBuilder.encode().build();
		
		LOGGER.info("************** uriComponent.toUriString()="+uriComponent.toUriString());
		// The headers..
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		// The request body..
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("code", authorizationCode);
		map.add("client_id", managerBean.getProperties().getClientID());
		map.add("client_secret", managerBean.getProperties().getClientSecret());
		map.add("grant_type", managerBean.getProperties().getGrantType());
		map.add("redirect_uri", managerBean.getProperties().getRedirectURL());
		
		String response=restTemplate.postForObject(uriComponent.toUriString(), map, String.class);
		
		LOGGER.info("************** Authorization Response="+response);

		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response);

		String accessToken=(String)parsedJson.get("access_token");
		LOGGER.info("*************** Authorization Code="+accessToken);

		return accessToken;

	}

}
