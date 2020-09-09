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
@Table(name = "DocumentProcessingInfo")
public class DocumentProcessingInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int documentProcessingId;
	
	@Column(name = "IsLIBOR")
	private char IsLIBOR;
	
	@Column(name = "docPosition")
	private int docPosition;
	
	@Column(name = "startpage")
	private int startpage;
	
	@Column(name = "startPagePosition")
	private int startPagePosition;
	
	@Column(name = "startSnippet")
	private String startSnippet;
	
	@Column(name = "docFileName")
	private String docFileName;
	
	@Column(name = "predictions")
	private String predictions;
	
	@Column(name = "legalEntityName")
	private String legalEntityName;

	@Column(name = "counterPartyName")
	private String counterPartyName;
	
	@Column(name = "startDate")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "terminationDate")
	@Temporal(TemporalType.DATE)
	private Date terminationDate;
	
	@Column(name = "amount")
	private String amount;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "fallbackPresent")
	private char fallbackPresent;
	
	@Column(name = "fallbackPosition")
	private int fallbackPosition;
	
	@Column(name = "fallbackPage")
	private int fallbackPage;
	
	@Column(name = "fallbackText")
	private String fallbackText;
	
	@Column(name = "fallbackTextComplexity")
	private int fallbackTextComplexity;

	public int getDocumentProcessingId() {
		return documentProcessingId;
	}

	public void setDocumentProcessingId(int documentProcessingId) {
		this.documentProcessingId = documentProcessingId;
	}

	public char getIsLIBOR() {
		return IsLIBOR;
	}

	public void setIsLIBOR(char isLIBOR) {
		IsLIBOR = isLIBOR;
	}

	public int getDocPosition() {
		return docPosition;
	}

	public void setDocPosition(int docPosition) {
		this.docPosition = docPosition;
	}

	public int getStartpage() {
		return startpage;
	}

	public void setStartpage(int startpage) {
		this.startpage = startpage;
	}

	public int getStartPagePosition() {
		return startPagePosition;
	}

	public void setStartPagePosition(int startPagePosition) {
		this.startPagePosition = startPagePosition;
	}

	public String getStartSnippet() {
		return startSnippet;
	}

	public void setStartSnippet(String startSnippet) {
		this.startSnippet = startSnippet;
	}

	public String getDocFileName() {
		return docFileName;
	}

	public void setDocFileName(String docFileName) {
		this.docFileName = docFileName;
	}

	public String getPredictions() {
		return predictions;
	}

	public void setPredictions(String predictions) {
		this.predictions = predictions;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public String getCounterPartyName() {
		return counterPartyName;
	}

	public void setCounterPartyName(String counterPartyName) {
		this.counterPartyName = counterPartyName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public char getFallbackPresent() {
		return fallbackPresent;
	}

	public void setFallbackPresent(char fallbackPresent) {
		this.fallbackPresent = fallbackPresent;
	}

	public int getFallbackPosition() {
		return fallbackPosition;
	}

	public void setFallbackPosition(int fallbackPosition) {
		this.fallbackPosition = fallbackPosition;
	}

	public int getFallbackPage() {
		return fallbackPage;
	}

	public void setFallbackPage(int fallbackPage) {
		this.fallbackPage = fallbackPage;
	}

	public String getFallbackText() {
		return fallbackText;
	}

	public void setFallbackText(String fallbackText) {
		this.fallbackText = fallbackText;
	}

	public int getFallbackTextComplexity() {
		return fallbackTextComplexity;
	}

	public void setFallbackTextComplexity(int fallbackTextComplexity) {
		this.fallbackTextComplexity = fallbackTextComplexity;
	}						
}
