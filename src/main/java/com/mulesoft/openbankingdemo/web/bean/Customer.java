package com.mulesoft.openbankingdemo.web.bean;

import java.io.Serializable;

public class Customer implements Serializable {
	
	private String customerSFIdentifier;
	private String customerID;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	private String dateOfBirth;
	
	private Address address;

	public String getCustomerSFIdentifier() {
		return customerSFIdentifier;
	}

	public void setCustomerSFIdentifier(String customerSFIdentifier) {
		this.customerSFIdentifier = customerSFIdentifier;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
}
