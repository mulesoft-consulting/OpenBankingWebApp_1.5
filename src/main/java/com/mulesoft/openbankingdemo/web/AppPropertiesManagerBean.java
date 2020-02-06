package com.mulesoft.openbankingdemo.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mulesoft.openbankingdemo.web.bean.ApplicationProperties;
import com.mulesoft.openbankingdemo.web.bean.EndpointProperties;

@Configuration
public class AppPropertiesManagerBean {

	private static Logger LOGGER = LoggerFactory
			      .getLogger(AppPropertiesManagerBean.class);


	private String deviceToken;
	
	private String clientId;

	private String clientSecret;
	
	private String responseType;
	
	private String grantType;
	
	private String redirectURL;
		
	private ApplicationProperties properties;
	
	private static AppPropertiesManagerBean instance=null;
	
	public static AppPropertiesManagerBean getInstance() {
		if(instance==null) {
			instance=new AppPropertiesManagerBean();
			instance.getProperties();
		}
		
		return instance;
	}
	
	@Bean(name="appPropertiesBean")
	public ApplicationProperties getProperties() {
		if(this.properties==null) {
			setProperties(new ApplicationProperties());
			loadProperties();
			return properties;
		}
		return properties;
	}
	
	@Resource(name="appPropertiesBean")
	public void setProperties(ApplicationProperties props) {
		this.properties=props;
	}

	private void loadProperties() {
		
		Properties appProps=new Properties();
		InputStream stream=null;
		try {
			stream=this.getClass().getClassLoader().getResourceAsStream("application.properties");
			appProps.load(stream);
		} catch (IOException e) {
			LOGGER.error("Error in loading application.properties", e);
			e.printStackTrace();
		}
		
		if(appProps.size()==0) {
			LOGGER.error("Error in loading application.properties; SIZE ZERO");
			return;
		}
		
		properties.setDeviceToken(appProps.getProperty("oauth.devicetoken"));
		properties.setClientID(appProps.getProperty("oauth.clientid"));
		properties.setClientSecret(appProps.getProperty("oauth.clientsecret"));
		properties.setResponseType(appProps.getProperty("oauth.responsetype"));
		properties.setGrantType(appProps.getProperty("oauth.granttype"));
		properties.setRedirectURL(appProps.getProperty("oauth.redirecturl"));
		properties.setTemenosAPIKey(appProps.getProperty("muleapp.temenos.apikey"));
		properties.setTemenosAccountProductType(appProps.getProperty("temenos.account.producttype"));
		properties.setAuthenticationURL(getEndpointDetails("authentication", appProps));
		properties.setAuthorizationURL(getEndpointDetails("authorization",appProps));
		properties.setTokenURL(getEndpointDetails("token",appProps));
		properties.setTemenosAccountURL(getEndpointDetails("temenosaccounts",appProps));
		properties.setTokenValidateURL(getEndpointDetails("tokenvalidate",appProps));
		properties.setTemenosFundTransferURL(getEndpointDetails("temenosacctransfer",appProps));
		properties.setAggregateSFDCTemenosURL(getEndpointDetails("sfdctemenoscustomer",appProps));
		properties.setTemenosAccountSAPIURL(getEndpointDetails("temenoscustaccounts",appProps));

		
		Set<Object> entries=appProps.keySet();
		
		for(Iterator<Object> iter=entries.iterator();iter.hasNext();) {
			String keyName=(String)iter.next();
			if(keyName.contains("temenos.username.")) {
				String email=keyName.substring("temenos.username.".length(), keyName.length());
				properties.addTemenosCustomerIDDetails(email, appProps.getProperty(keyName));
			}
		}
		
		if(stream!=null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private EndpointProperties getEndpointDetails(String key,Properties appProps) {
		EndpointProperties endpoint=new EndpointProperties();
		
		endpoint.setScheme(appProps.getProperty("muleapp."+key+".scheme"));
		endpoint.setHost(appProps.getProperty("muleapp."+key+".host"));
		if(appProps.containsKey("muleapp."+key+".port")) {
			endpoint.setPort(Integer.parseInt(appProps.getProperty("muleapp."+key+".port")));
		}
		endpoint.setPath(appProps.getProperty("muleapp."+key+".path"));
		
		return endpoint;
	}
}
