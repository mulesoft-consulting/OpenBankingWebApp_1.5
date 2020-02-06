package com.mulesoft.openbankingdemo.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;


public class TestJSONParser {
	
	public static void main(String args[]) throws Exception {
	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines(Paths.get(Class.class.getClassLoader().getSystemResource("examples/account_details.json").toURI())))
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    String jsonString= contentBuilder.toString();
	    
	    System.out.println(jsonString);
	    
	    JSONArray object=new JSONArray(jsonString);
	    JSONObject accountJson=null;
	    for(int i=0;i<object.length();i++) {
	    	 accountJson=object.getJSONObject(i);
			 if(accountJson.getString("type").equals("Current Account")) {
				 
				 	JSONObject balance=accountJson.getJSONObject("balance");
				 	System.out.println("BALANCE="+balance.getDouble("amount"));
				 	System.out.println("CURRENCY="+balance.getString("currency"));
					break;
			 }
	    }

	    System.out.println("DATA="+object);
	}

}
