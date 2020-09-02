package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CounterPartyDetails")
public class CounterParty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int counterPartyId;
	
	@Column(name = "customerId")
	private int customerId;
	
	@Column(name = "counterPartyName")
	private String counterPartyName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "stateCode")
	private String stateCode;
	
	@Column(name = "zipCode")
	private String zipCode;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "fax")
	private String fax;

	public int getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(int counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCounterPartyName() {
		return counterPartyName;
	}

	public void setCounterPartyName(String counterPartyName) {
		this.counterPartyName = counterPartyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	

}
