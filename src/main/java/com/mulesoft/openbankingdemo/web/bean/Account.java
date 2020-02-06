package com.mulesoft.openbankingdemo.web.bean;

import java.io.Serializable;

public class Account implements Serializable {
	
	private String accountNumber;
	private String customerID;
	private String accountType;
	private double balance=0.0;
	private double availableLimit=0.0;
	private String currency;
	private String accountName;
	private String accountOpeningDate;
	private String IBAN;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getAvailableLimit() {
		return availableLimit;
	}
	public void setAvailableLimit(double availableLimit) {
		this.availableLimit = availableLimit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountOpeningDate() {
		return accountOpeningDate;
	}
	public void setAccountOpeningDate(String accountOpeningDate) {
		this.accountOpeningDate = accountOpeningDate;
	}
	public String getIBAN() {
		return IBAN;
	}
	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}
	
}
