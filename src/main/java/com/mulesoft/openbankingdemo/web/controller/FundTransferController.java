package com.mulesoft.openbankingdemo.web.controller;

import java.security.Principal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mulesoft.openbankingdemo.web.AppPropertiesManagerBean;
import com.mulesoft.openbankingdemo.web.Utils;
import com.mulesoft.openbankingdemo.web.bean.Account;
import com.mulesoft.openbankingdemo.web.bean.Address;
import com.mulesoft.openbankingdemo.web.bean.ApplicationProperties;
import com.mulesoft.openbankingdemo.web.bean.Customer;
import com.mulesoft.openbankingdemo.web.bean.FundTransfer;

@Controller
public class FundTransferController {

	private static Logger LOGGER = LoggerFactory
		      .getLogger(FundTransferController.class);

    @RequestMapping("/transfer")
    public String preTransfer(HttpSession session, Model model, Principal principal) {
    	LOGGER.info("**************** Starting Fund Transfer for Principal="+principal);
    	FundTransfer ft=new FundTransfer();
    	ft.setSenderAccount(AccountUtils.getAggregateAccount(principal.getName(),session));    	
    	ft.setSenderPhoneNumber(((Customer)session.getAttribute("CUSTOMER_DATA")).getPhoneNumber());
    	model.addAttribute(ft);
    	
        return "fundtransfer";
    }
    
    @RequestMapping("/fundtransfer")
    public String executeTransfer(HttpSession session, Model model,FundTransfer fundTransfer) {
    	LOGGER.info("**************** Fund Transfer happening for sender account number="+fundTransfer.getSenderAccount().getAccountNumber());
    	LOGGER.info("**************** Transfering Amount="+fundTransfer.getAmount()+ " to Receiver Account Number="+fundTransfer.getReceiverAccountNumber());
    	ApplicationProperties appProperties=AppPropertiesManagerBean.getInstance().getProperties();
		RestTemplate restTemplate=Utils.getRestTemplate();		
		UriComponents uriComponent=UriComponentsBuilder.newInstance().scheme(appProperties.getTemenosFundTransferURL().getScheme())
				.host(appProperties.getTemenosFundTransferURL().getHost())
				.path(appProperties.getTemenosFundTransferURL().getPath())
				.queryParam("notify", String.valueOf(fundTransfer.isNotification()))
				.buildAndExpand(fundTransfer.getSenderAccount().getAccountNumber());
		
		// The request body..
		JSONObject bodyPayload=new JSONObject();
		JSONObject amountPayload=new JSONObject();
		amountPayload.put("currency", fundTransfer.getSenderAccount().getCurrency());
		amountPayload.put("amount", Double.toString(fundTransfer.getAmount()));
		bodyPayload.put("amount", amountPayload);
		bodyPayload.put("label", fundTransfer.getTransferRemark());
		bodyPayload.put("receiver_AccountId", fundTransfer.getReceiverAccountNumber());
		bodyPayload.put("description", fundTransfer.getTransferRemark());
		if(fundTransfer.isNotification()) {
			LOGGER.info("************** Customer will get SMS notification at "+fundTransfer.getSenderPhoneNumber());
			bodyPayload.put("sender_PhoneNumber", fundTransfer.getSenderPhoneNumber());
		}
		
		// The headers..
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);		
		HttpEntity entities=new HttpEntity<String>(bodyPayload.toString(), headers);
		
		LOGGER.info("*************** The PAYLOAD="+entities.getBody());
		LOGGER.info("*************** THE FUND TRANSFER EXECUTION URL="+uriComponent.toUriString());
		
		ResponseEntity<String> response=restTemplate.exchange(uriComponent.encode().toUriString(), HttpMethod.POST, entities, String.class);
		
		//String response=restTemplate.postForObject(uriComponent.toUriString(), map, String.class);
		LOGGER.info("****************** The Response from Payment="+response);
		JsonParser jsonStringParser= JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJson=jsonStringParser.parseMap(response.getBody());
		String paymentOrderId=(String)parsedJson.get("paymentOrderId");
		String paymentStatus=(String)parsedJson.get("status");
		
		model.addAttribute("paymentOrderID", paymentOrderId);
		model.addAttribute("paymentStatus", paymentStatus);
		model.addAttribute("receiverAccountNumber", fundTransfer.getReceiverAccountNumber());
		model.addAttribute("transferAmount", fundTransfer.getAmount());
		model.addAttribute("selfAccountNumber", fundTransfer.getSenderAccount().getAccountNumber());
		// Getting the updated account balance after transfer with a fresh call to Temenos (Mule Apps) -- NEW ADDITION to the confirmation page..
		restTemplate=Utils.getRestTemplate();
		UriComponentsBuilder uriComponentBuilder=UriComponentsBuilder.newInstance().scheme(appProperties.getTemenosAccountSAPIURL().getScheme())
				.host(appProperties.getTemenosAccountSAPIURL().getHost())
				.path(appProperties.getTemenosAccountSAPIURL().getPath());
				if(appProperties.getTemenosAccountSAPIURL().getPort()>0) {
						uriComponentBuilder.port(appProperties.getTemenosAccountSAPIURL().getPort());
				}
				
				uriComponent=uriComponentBuilder.encode().buildAndExpand(fundTransfer.getSenderAccount().getCustomerID());

		System.out.println("************** uriComponent.toUriString() for Account Details port Transfer="+uriComponent.toUriString());
		HttpEntity<String> entity = new HttpEntity<>(headers);

		response = restTemplate
				  .exchange(uriComponent.toUriString(), HttpMethod.GET, entity, String.class);

		
		LOGGER.info("************** ACCOUNTS DETAILS Response="+response);
	    JSONArray object=new JSONArray(response.getBody());
	    for(int i=0;i<object.length();i++) {
	    	JSONObject accountJson=object.getJSONObject(i);
			 if(accountJson.getString("type").equals(appProperties.getTemenosAccountProductType())) {
				 
				 	LOGGER.info("ACCOUNT for PRODUCT="+appProperties.getTemenosAccountProductType()+"  DETAILS="+accountJson);
					// finally populating the updated account balance post transfer..
				 	JSONObject balance=accountJson.getJSONObject("balance");
				 	model.addAttribute("currentAccountBalance",balance.getDouble("amount"));
				 	model.addAttribute("currency",balance.getString("currency"));
					break;
			 }
	    }		
    	
        return "fundtransferconfirmation";
    }

}
