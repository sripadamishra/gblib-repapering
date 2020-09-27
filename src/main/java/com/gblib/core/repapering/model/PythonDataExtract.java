package com.gblib.core.repapering.model;

import java.util.Date;

public class PythonDataExtract {

	private String entity1;
	private String entity2;
	private Date startdate;
	private Date terminationdate;
	private String currency;
	private String amount;
	public String getEntity1() {
		return entity1;
	}
	public void setEntity1(String entity1) {
		this.entity1 = entity1;
	}
	public String getEntity2() {
		return entity2;
	}
	public void setEntity2(String entity2) {
		this.entity2 = entity2;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getTerminationdate() {
		return terminationdate;
	}
	public void setTerminationdate(Date terminationdate) {
		this.terminationdate = terminationdate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
