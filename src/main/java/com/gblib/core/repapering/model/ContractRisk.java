package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ContractRiskDetails")
public class ContractRisk {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contractRiskId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "riskId")
	private String riskId;
	
	@Column(name = "riskDesc")
	private String riskDesc;
	
	@Column(name = "resolutionStatus")
	private int resolutionStatus;

	public int getContractRiskId() {
		return contractRiskId;
	}

	public void setContractRiskId(int contractRiskId) {
		this.contractRiskId = contractRiskId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public String getRiskId() {
		return riskId;
	}

	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	public String getRiskDesc() {
		return riskDesc;
	}

	public void setRiskDesc(String riskDesc) {
		this.riskDesc = riskDesc;
	}

	public int getResolutionStatus() {
		return resolutionStatus;
	}

	public void setResolutionStatus(int resolutionStatus) {
		this.resolutionStatus = resolutionStatus;
	}	 
}
