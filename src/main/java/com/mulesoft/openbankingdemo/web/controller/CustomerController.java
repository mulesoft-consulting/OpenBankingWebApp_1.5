package com.mulesoft.openbankingdemo.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mulesoft.openbankingdemo.web.bean.Customer;

@Controller
public class CustomerController {
	
	@RequestMapping("/customer")
	public String processCustomer(HttpSession session, Model model, Customer customer) {
		
		Customer customerData=(Customer) session.getAttribute("CUSTOMER_DATA");
		model.addAttribute(customerData);
		
		return "customer";
	}

}
