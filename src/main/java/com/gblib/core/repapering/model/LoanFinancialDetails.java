package com.gblib.core.repapering.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "LoanFinancialDetails")
public class LoanFinancialDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int loanContractFinancialId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "counterPartyId")
	private int counterPartyId;
	
	@Column(name = "loanAmount")
	private double loanAmount;
	
	@Column(name = "loanCurrency")
	private String loanCurrency;
		
	@Column(name = "startDate")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "maturityDate")
	@Temporal(TemporalType.DATE)
	private Date maturityDate;

	@Column(name = "tenorMonths")
	private int tenorMonths;
	
	@Column(name = "rateOfInterest")
	private double rateOfInterest;
	 
	@Column(name = "loanTypeId")
	private int loanTypeId;
	
	@Column(name = "collateralInfo")
	private String collateralInfo;
	
	@Column(name = "paymentSchedule")
	private int paymentSchedule;
	
	@Column(name = "borrowerName")
	private String borrowerName;
	
	@Column(name = "lenderName")
	private String lenderName;
	
	@Column(name = "adminAgentName")
	private String adminAgentName;
	
	@Column(name = "jointLeadArrangerName")
	private String jointLeadArrangerName;
	
	@Column(name = "coSyndicationAgentName")
	private String coSyndicationAgentName;
	
	@Column(name = "coDocumentationAgentName")
	private String coDocumentationAgentName;

	public int getLoanContractFinancialId() {
		return loanContractFinancialId;
	}

	public void setLoanContractFinancialId(int loanContractFinancialId) {
		this.loanContractFinancialId = loanContractFinancialId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(int counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanCurrency() {
		return loanCurrency;
	}

	public void setLoanCurrency(String loanCurrency) {
		this.loanCurrency = loanCurrency;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public int getTenorMonths() {
		return tenorMonths;
	}

	public void setTenorMonths(int tenorMonths) {
		this.tenorMonths = tenorMonths;
	}

	public double getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public int getLoanTypeId() {
		return loanTypeId;
	}

	public void setLoanTypeId(int loanTypeId) {
		this.loanTypeId = loanTypeId;
	}

	public String getCollateralInfo() {
		return collateralInfo;
	}

	public void setCollateralInfo(String collateralInfo) {
		this.collateralInfo = collateralInfo;
	}

	public int getPaymentSchedule() {
		return paymentSchedule;
	}

	public void setPaymentSchedule(int paymentSchedule) {
		this.paymentSchedule = paymentSchedule;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getLenderName() {
		return lenderName;
	}

	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}

	public String getAdminAgentName() {
		return adminAgentName;
	}

	public void setAdminAgentName(String adminAgentName) {
		this.adminAgentName = adminAgentName;
	}

	public String getJointLeadArrangerName() {
		return jointLeadArrangerName;
	}

	public void setJointLeadArrangerName(String jointLeadArrangerName) {
		this.jointLeadArrangerName = jointLeadArrangerName;
	}

	public String getCoSyndicationAgentName() {
		return coSyndicationAgentName;
	}

	public void setCoSyndicationAgentName(String coSyndicationAgentName) {
		this.coSyndicationAgentName = coSyndicationAgentName;
	}

	public String getCoDocumentationAgentName() {
		return coDocumentationAgentName;
	}

	public void setCoDocumentationAgentName(String coDocumentationAgentName) {
		this.coDocumentationAgentName = coDocumentationAgentName;
	}	
}
