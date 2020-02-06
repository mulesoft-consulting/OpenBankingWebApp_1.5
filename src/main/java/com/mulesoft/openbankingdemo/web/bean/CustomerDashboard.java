package com.mulesoft.openbankingdemo.web.bean;

import java.io.Serializable;

public class CustomerDashboard implements Serializable {
	
	private Customer customer=null;
	private Account account=null;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

}
