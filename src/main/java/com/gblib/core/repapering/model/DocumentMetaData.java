package com.gblib.core.repapering.model;

import javax.persistence.Column;

public class DocumentMetaData {    
	//
	private String headerName;	
	private int headerPageNo;
	private String headerTextContent;
	private int    headerParagraphIndex;
	private String headerFontName;
	private String headerFontSize;
	private float startLocationX;
	private float startLocationY;
	private float endLocationX;
	private float endLocationY;
	
	//
	private int regulatoryEventId;
	private int contractType;
	private String domainContextDictionaryId;
	private String domainContextName;
	private int domainContextTypeId;
	private int domainContextSubTypeId;
	private String domainContextPossibleNameDefinitions;
	private String domainContextPossibleValueDefinitions;
	private String phraseRule;
	private String entityRule;
	private String referenceExamples;
    
	//	
	private int textSimilarity;	
	private String domaincontextCurrentFieldValue;	
	private String domaincontextProposedFieldValue;
	//	
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public int getHeaderPageNo() {
		return headerPageNo;
	}
	public void setHeaderPageNo(int headerPageNo) {
		this.headerPageNo = headerPageNo;
	}
	public String getHeaderTextContent() {
		return headerTextContent;
	}
	public void setHeaderTextContent(String headerTextContent) {
		this.headerTextContent = headerTextContent;
	}
	public int getHeaderParagraphIndex() {
		return headerParagraphIndex;
	}
	public void setHeaderParagraphIndex(int headerParagraphIndex) {
		this.headerParagraphIndex = headerParagraphIndex;
	}
	public String getHeaderFontName() {
		return headerFontName;
	}
	public void setHeaderFontName(String headerFontName) {
		this.headerFontName = headerFontName;
	}
	public String getHeaderFontSize() {
		return headerFontSize;
	}
	public void setHeaderFontSize(String headerFontSize) {
		this.headerFontSize = headerFontSize;
	}
	public float getStartLocationX() {
		return startLocationX;
	}
	public void setStartLocationX(float startLocationX) {
		this.startLocationX = startLocationX;
	}
	public float getStartLocationY() {
		return startLocationY;
	}
	public void setStartLocationY(float startLocationY) {
		this.startLocationY = startLocationY;
	}
	public float getEndLocationX() {
		return endLocationX;
	}
	public void setEndLocationX(float endLocationX) {
		this.endLocationX = endLocationX;
	}
	public float getEndLocationY() {
		return endLocationY;
	}
	public void setEndLocationY(float endLocationY) {
		this.endLocationY = endLocationY;
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
	public int getTextSimilarity() {
		return textSimilarity;
	}
	public void setTextSimilarity(int textSimilarity) {
		this.textSimilarity = textSimilarity;
	}
	public String getDomaincontextCurrentFieldValue() {
		return domaincontextCurrentFieldValue;
	}
	public void setDomaincontextCurrentFieldValue(String domaincontextCurrentFieldValue) {
		this.domaincontextCurrentFieldValue = domaincontextCurrentFieldValue;
	}
	public String getDomaincontextProposedFieldValue() {
		return domaincontextProposedFieldValue;
	}
	public void setDomaincontextProposedFieldValue(String domaincontextProposedFieldValue) {
		this.domaincontextProposedFieldValue = domaincontextProposedFieldValue;
	}
	}
