package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "RegulatoryEventDomainContext")
public class RegulatoryEventDomainContext {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int regulatoryeventDomainContextId;
	
	@Column(name = "regulatoryEventId")
	private int regulatoryEventId;
	
	@Column(name = "contractType")
	private int contractType;
	
	@Column(name = "domainContextDictionaryId")
	private String domainContextDictionaryId;
	
	@Column(name = "domainContextName")
	private String domainContextName;
	
	@Column(name = "domainContextTypeId")
	private int domainContextTypeId;
	
	@Column(name = "domainContextSubTypeId")
	private int domainContextSubTypeId;
	
	@Column(name = "domainContextPossibleNameDefinitions")
	private String domainContextPossibleNameDefinitions;
	
	@Column(name = "domainContextPossibleValueDefinitions")
	private String domainContextPossibleValueDefinitions;
	
	@Column(name = "phraseRule")
	private String phraseRule;
	
	@Column(name = "entityRule")
	private String entityRule;
	
	@Column(name = "referenceExamples")
	private String referenceExamples;

	public int getRegulatoryeventDomainContextId() {
		return regulatoryeventDomainContextId;
	}

	public void setRegulatoryeventDomainContextId(int regulatoryeventDomainContextId) {
		this.regulatoryeventDomainContextId = regulatoryeventDomainContextId;
	}

	public int getRegulatoryEventId() {
		return regulatoryEventId;
	}

	public void setRegulatoryEventId(int regulatoryEventId) {
		this.regulatoryEventId = regulatoryEventId;
	}

	public int getContractType() {
		return contractType;
	}

	public void setContractType(int contractType) {
		this.contractType = contractType;
	}

	public String getDomainContextDictionaryId() {
		return domainContextDictionaryId;
	}

	public void setDomainContextDictionaryId(String domainContextDictionaryId) {
		this.domainContextDictionaryId = domainContextDictionaryId;
	}

	public String getDomainContextName() {
		return domainContextName;
	}

	public void setDomainContextName(String domainContextName) {
		this.domainContextName = domainContextName;
	}

	public int getDomainContextTypeId() {
		return domainContextTypeId;
	}

	public void setDomainContextTypeId(int domainContextTypeId) {
		this.domainContextTypeId = domainContextTypeId;
	}

	public int getDomainContextSubTypeId() {
		return domainContextSubTypeId;
	}

	public void setDomainContextSubTypeId(int domainContextSubTypeId) {
		this.domainContextSubTypeId = domainContextSubTypeId;
	}

	public String getDomainContextPossibleNameDefinitions() {
		return domainContextPossibleNameDefinitions;
	}

	public void setDomainContextPossibleNameDefinitions(String domainContextPossibleNameDefinitions) {
		this.domainContextPossibleNameDefinitions = domainContextPossibleNameDefinitions;
	}

	public String getDomainContextPossibleValueDefinitions() {
		return domainContextPossibleValueDefinitions;
	}

	public void setDomainContextPossibleValueDefinitions(String domainContextPossibleValueDefinitions) {
		this.domainContextPossibleValueDefinitions = domainContextPossibleValueDefinitions;
	}

	public String getPhraseRule() {
		return phraseRule;
	}

	public void setPhraseRule(String phraseRule) {
		this.phraseRule = phraseRule;
	}

	public String getEntityRule() {
		return entityRule;
	}

	public void setEntityRule(String entityRule) {
		this.entityRule = entityRule;
	}

	public String getReferenceExamples() {
		return referenceExamples;
	}

	public void setReferenceExamples(String referenceExamples) {
		this.referenceExamples = referenceExamples;
	}
	
	
}
