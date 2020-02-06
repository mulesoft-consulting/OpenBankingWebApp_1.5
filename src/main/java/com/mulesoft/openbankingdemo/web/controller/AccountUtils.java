package com.mulesoft.openbankingdemo.web.controller;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mulesoft.openbankingdemo.web.AppPropertiesManagerBean;
import com.mulesoft.openbankingdemo.web.Utils;
import com.mulesoft.openbankingdemo.web.bean.Account;
import com.mulesoft.openbankingdemo.web.bean.Address;
import com.mulesoft.openbankingdemo.web.bean.ApplicationProperties;
import com.mulesoft.openbankingdemo.web.bean.Customer;

public class AccountUtils {
	
	private static Logger LOGGER = LoggerFactory
		      .getLogger(AccountUtils.class);

	
    static Account getAggregateAccount(String username, HttpSession session) {
    	LOGGER.info("**************** Starting Fund Transfer for username="+username);
    	
    	Account senderAccount=new Account();
    	ApplicationProperties appProperties=AppPropertiesManagerBean.getInstance().getProperties();
    	
		RestTemplate restTemplate=Utils.getRestTemplate();		
		UriComponents uriComponent=UriComponentsBuilder.newInstance().scheme(appProperties.getAggregateSFDCTemenosURL().getScheme())
				.host(appProperties.getAggregateSFDCTemenosURL().getHost())
				.path(appProperties.getAggregateSFDCTemenosURL().getPath())
				.queryParam("email", username).encode().build();
		
		LOGGER.info("**************** Temenos Account URL="+ uriComponent.toUriString());
		// The headers..
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate
				  .exchange(uriComponent.toUriString(), HttpMethod.GET, entity, String.class);

		
		LOGGER.info("************** ACCOUNTS DETAILS Response="+response);
		
		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response.getBody());
		if(parsedJson==null || parsedJson.isEmpty()) {
			LOGGER.error("***************** NO ACCOUNT FOUND for CUSTOMER USERNAME="+username);
			return senderAccount;
		}
		LOGGER.info("******************** THE ACCOUNT DATA="+parsedJson.get("accounts"));
		List accounts=(List) parsedJson.get("accounts");
		// populating the account details..
		LinkedHashMap<String, Object> account=null;
		for(Iterator iter=accounts.iterator();iter.hasNext();) {
			account=(LinkedHashMap<String, Object>)iter.next();
			if(account.get("type").equals(appProperties.getTemenosAccountProductType())) {
				break;
			}
		}
		LOGGER.info("*************** ACCOUNT DETAILS="+account);
		senderAccount.setIBAN((String)account.get("IBAN"));
		LinkedHashMap<String, Object> jsonOwers=(LinkedHashMap<String, Object>)((List)account.get("owners")).get(0);
		senderAccount.setAccountName((String)jsonOwers.get("display_name"));
		senderAccount.setAccountNumber((String)account.get("accountId"));
		//senderAccount.setAccountOpeningDate((String)account.get("openingDate"));
		senderAccount.setAccountType((String)account.get("type"));
		LinkedHashMap<String, Object> availableBalance=(LinkedHashMap<String, Object>) account.get("available_balance");
		LinkedHashMap<String, Object> balance=(LinkedHashMap<String, Object>) account.get("balance");
		senderAccount.setBalance(Double.parseDouble(balance.get("amount").toString()));
		senderAccount.setCurrency((String)balance.get("currency"));
		senderAccount.setCustomerID((String)jsonOwers.get("user_id"));
		
		// Now populating the customer object..
		Customer customer=new Customer();
		customer.setCustomerID((String)parsedJson.get("customerId"));
		customer.setFirstName((String)parsedJson.get("firstName"));
		customer.setLastName((String)parsedJson.get("lastName"));
		customer.setPhoneNumber((String)parsedJson.get("phoneNumber"));
		customer.setEmailAddress((String)parsedJson.get("emailAddress"));
		customer.setDateOfBirth((String)parsedJson.get("dateOfBirth"));
		
		// saving the customer data in session
		session.setAttribute("CUSTOMER_DATA", customer);
		List jsonAddresses=(List)parsedJson.get("deliveryAddresses");
		if(!jsonAddresses.isEmpty()) {
			LinkedHashMap<String, Object> jsonaddr=(LinkedHashMap<String, Object>) jsonAddresses.get(0);
			Address address=new Address();
			address.setAddress((String)jsonaddr.get("address"));
			address.setCity((String)jsonaddr.get("city"));
			address.setState((String)jsonaddr.get("state"));
			address.setCountry((String)jsonaddr.get("country"));
			address.setPostalCode((String)jsonaddr.get("postalCode"));
			
			customer.setAddress(address);
		}
		
    	return senderAccount;
    }

    
    /*
     * This method is used for populating from TEMENOS URL
     */
    static Account getTemenosAccount(String username) {
    	LOGGER.info("**************** Starting Fund Transfer for username="+username);
    	
    	Account senderAccount=new Account();
    	ApplicationProperties appProperties=AppPropertiesManagerBean.getInstance().getProperties();
    	String customerID=appProperties.getTemenosCustomerIDDetails().get(username);
    	LOGGER.info("**************** Temenos CUSTOMER ID="+ customerID);
    	
		RestTemplate restTemplate=Utils.getRestTemplate();		
		UriComponents uriComponent=UriComponentsBuilder.newInstance().scheme(appProperties.getTemenosAccountURL().getScheme())
				.host(appProperties.getTemenosAccountURL().getHost())
				.path(appProperties.getTemenosAccountURL().getPath()).buildAndExpand(customerID);
		
		LOGGER.info("**************** Temenos Account URL="+ uriComponent.toUriString());
		LOGGER.info("**************** API KEY="+appProperties.getTemenosAPIKey());
		// The headers..
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("apikey",appProperties.getTemenosAPIKey());
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate
				  .exchange(uriComponent.toUriString(), HttpMethod.GET, entity, String.class);

		
		LOGGER.info("************** ACCOUNTS DETAILS Response="+response);
		
		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response.getBody());
		List body=(List)parsedJson.get("body");
		if(body==null || body.isEmpty()) {
			LOGGER.error("***************** NO ACCOUNT FOUND for CUSTOMER ID="+customerID);
			return senderAccount;
		}
		// populating the account details..
		LinkedHashMap<String, Object> account=null;
		for(Iterator iter=body.iterator();iter.hasNext();) {
			account=(LinkedHashMap<String, Object>)iter.next();
			if(account.get("productName").equals(appProperties.getTemenosAccountProductType())) {
				break;
			}
		}
		LOGGER.info("*************** ACCOUNT DETAILS="+account);
		senderAccount.setIBAN((String)account.get("IBAN"));
		senderAccount.setAccountName((String)account.get("displayName"));
		senderAccount.setAccountNumber((String)account.get("accountId"));
		senderAccount.setAccountOpeningDate((String)account.get("openingDate"));
		senderAccount.setAccountType((String)account.get("productName"));
		if(account.get("availableLimit").equals("NOLIMIT")) {
			senderAccount.setAvailableLimit(100000000.00); // In case of NOLIMIT, Set it to astronomical value.
		} else {
			senderAccount.setAvailableLimit(Double.parseDouble((String)account.get("availableLimit")));
		}	
		senderAccount.setBalance(Double.parseDouble(((Integer)account.get("availableBalance")).toString()));
		senderAccount.setCurrency((String)account.get("currencyId"));
		senderAccount.setCustomerID((String)account.get("customerId"));
		
    	return senderAccount;
    }


}
