package com.mulesoft.openbankingdemo.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mulesoft.openbankingdemo.web.controller.CustomClientHttpRequestInterceptor;

public class Utils {
	private static Logger LOGGER = LoggerFactory
		      .getLogger(Utils.class);

	
	public static RestTemplate getRestTemplate() {
		
		RestTemplateBuilder restTemplateBuilder=new RestTemplateBuilder(new RestTemplateCustomizer() {
			@Override
			public void customize(RestTemplate restTemplate) {
				restTemplate.getInterceptors().add(new CustomClientHttpRequestInterceptor());
			}
		});
		
		return restTemplateBuilder.build();
	}
	
	public static boolean isAccessTokenValid(String accessToken) {
		AppPropertiesManagerBean managerBean=AppPropertiesManagerBean.getInstance();
		RestTemplate restTemplate=Utils.getRestTemplate();
		UriComponentsBuilder uriComponentBuilder=UriComponentsBuilder.newInstance().scheme(managerBean.getProperties().getTokenValidateURL().getScheme())
				.host(managerBean.getProperties().getTokenValidateURL().getHost())
				.path(managerBean.getProperties().getTokenValidateURL().getPath())
				.queryParam("access_token", accessToken);
		
		if(managerBean.getProperties().getTokenValidateURL().getPort()>0) {
			uriComponentBuilder.port(managerBean.getProperties().getTokenValidateURL().getPort());
		}
	
		UriComponents uriComponent=uriComponentBuilder.encode().build();

		LOGGER.info("************** uriComponent.toUriString()="+uriComponent.toUriString());

		
		String response=restTemplate.getForObject(uriComponent.toUriString(), String.class);
		
		LOGGER.info("************** Authentication Response="+response);
		
		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response);
		Boolean tokenValidity=(Boolean)parsedJson.get("active");
		 
		LOGGER.info("*************** TOKEN VALIDITY="+tokenValidity);
		
		return tokenValidity.booleanValue();
	}

}
