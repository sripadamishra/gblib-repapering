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
@Table(name = "ContractDetails")
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "parentContractId")
	private int parentContractId;
	
	@Column(name = "customContractId")
	private String customContractId;
	
	@Column(name = "documentFileName")
	private String documentFileName;
	
	@Column(name = "contractName")
	private String contractName;
	
	@Column(name = "legalEntityId")
	private int legalEntityId;
	
	@Column(name = "legalEntityName")
	private String legalEntityName;
	
	@Column(name = "counterPartyId")
	private int counterPartyId;
	
	@Column(name = "counterPartyName")
	private String counterPartyName;
	
	@Column(name = "contractTemplate")
	private String contractTemplate;
	
	@Column(name = "contractStartDate")
	@Temporal(TemporalType.DATE)
	private Date contractStartDate;
	
	@Column(name = "contractExpiryDate")
	@Temporal(TemporalType.DATE)
	private Date contractExpiryDate;
	
	@Column(name = "contractTypeId")
	private int contractTypeId;
	
	@Column(name = "contractSubTypeId")
	private int contractSubTypeId;
	
	@Column(name = "isLIBOR")
	private boolean isLIBOR;
	
	@Column(name = "isAmendmentDoc")
	private boolean isAmendmentDoc;
	
	@Column(name = "currStatusId")
	private int currStatusId;
	
	@Column(name = "createdOn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
	
	@Column(name = "createdBy")
	private String createdBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getParentContractId() {
		return parentContractId;
	}

	public void setParentContractId(int parentContractId) {
		this.parentContractId = parentContractId;
	}
	
	public String getCustomContractId() {
		return customContractId;
	}

	public void setCustomContractId(String customContractId) {
		this.customContractId = customContractId;
	}
	
	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public int getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(int legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public int getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(int counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public String getCounterPartyName() {
		return counterPartyName;
	}

	public void setCounterPartyName(String counterPartyName) {
		this.counterPartyName = counterPartyName;
	}

	public String getContractTemplate() {
		return contractTemplate;
	}

	public void setContractTemplate(String contractTemplate) {
		this.contractTemplate = contractTemplate;
	}

	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Date getContractExpiryDate() {
		return contractExpiryDate;
	}

	public void setContractExpiryDate(Date contractExpiryDate) {
		this.contractExpiryDate = contractExpiryDate;
	}

	public int getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(int contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public int getContractSubTypeId() {
		return contractSubTypeId;
	}

	public void setContractSubTypeId(int contractSubTypeId) {
		this.contractSubTypeId = contractSubTypeId;
	}

	public boolean isLIBOR() {
		return isLIBOR;
	}

	public void setLIBOR(boolean isLIBOR) {
		this.isLIBOR = isLIBOR;
	}

	public boolean isAmendmentDoc() {
		return isAmendmentDoc;
	}

	public void setAmendmentDoc(boolean isAmendmentDoc) {
		this.isAmendmentDoc = isAmendmentDoc;
	}

	public int getCurrStatusId() {
		return currStatusId;
	}

	public void setCurrStatusId(int currStatusId) {
		this.currStatusId = currStatusId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	}
