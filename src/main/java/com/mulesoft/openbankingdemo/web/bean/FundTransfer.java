package com.mulesoft.openbankingdemo.web.bean;

import java.io.Serializable;

public class FundTransfer implements Serializable {
	
	private String receiverAccountNumber=null;
	private String receiverName=null;
	private double amount=0.0;
	private String transferRemark=null;
	private Account senderAccount=new Account();
	private String senderPhoneNumber=null;
	private boolean notification=false;
	
	public String getReceiverAccountNumber() {
		return receiverAccountNumber;
	}
	public void setReceiverAccountNumber(String receiverAccountNumber) {
		this.receiverAccountNumber = receiverAccountNumber;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getTransferRemark() {
		return transferRemark;
	}
	public void setTransferRemark(String transferRemark) {
		this.transferRemark = transferRemark;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Account getSenderAccount() {
		return senderAccount;
	}
	public void setSenderAccount(Account senderAccount) {
		this.senderAccount = senderAccount;
	}
	public String getSenderPhoneNumber() {
		return senderPhoneNumber;
	}
	public void setSenderPhoneNumber(String senderPhoneNumber) {
		this.senderPhoneNumber = senderPhoneNumber;
	}
	public boolean isNotification() {
		return notification;
	}
	public void setNotification(boolean notification) {
		this.notification = notification;
	}
	
}
