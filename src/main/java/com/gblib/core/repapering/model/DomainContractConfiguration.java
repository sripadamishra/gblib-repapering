package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DomainContractConfiguration")
public class DomainContractConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int domainContractConfigurationId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "regulatoryEventId")
	private int regulatoryEventId;
	
	@Column(name = "domainContextDictionaryId")
	private String domainContextDictionaryId;
	
	@Column(name = "isContextConfigurationActive")
	private boolean isContextConfigurationActive;
	
	public int getDomainContractConfigurationId() {
		return domainContractConfigurationId;
	}

	public void setDomainContractConfigurationId(int domainContractConfigurationId) {
		this.domainContractConfigurationId = domainContractConfigurationId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getRegulatoryEventId() {
		return regulatoryEventId;
	}

	public void setRegulatoryEventId(int regulatoryEventId) {
		this.regulatoryEventId = regulatoryEventId;
	}

	public String getDomainContextDictionaryId() {
		return domainContextDictionaryId;
	}

	public void setDomainContextDictionaryId(String domainContextDictionaryId) {
		this.domainContextDictionaryId = domainContextDictionaryId;
	}

	public boolean isContextConfigurationActive() {
		return isContextConfigurationActive;
	}

	public void setContextConfigurationActive(boolean isContextConfigurationActive) {
		this.isContextConfigurationActive = isContextConfigurationActive;
	}

}
