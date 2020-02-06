package com.mulesoft.openbankingdemo.web.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mulesoft.openbankingdemo.web.bean.Customer;
import com.mulesoft.openbankingdemo.web.bean.CustomerDashboard;

@Controller
public class DashboardController {

	private static Logger LOGGER = LoggerFactory
		      .getLogger(DashboardController.class);

	@RequestMapping("/customerdashboard")
	public String getCustomerDashboard(HttpSession session, Model model, Principal principal) {
    	LOGGER.info("**************** Starting Fund Transfer for Principal="+principal);
    	CustomerDashboard dashboard=new CustomerDashboard();
    	dashboard.setAccount(AccountUtils.getAggregateAccount(principal.getName(),session));
		dashboard.setCustomer((Customer)session.getAttribute("CUSTOMER_DATA"));
		
		model.addAttribute(dashboard);
    	
		return "customerdashboard";
	}

}
