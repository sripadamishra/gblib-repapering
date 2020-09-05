package com.gblib.core.repapering.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DocumentProcessingInfo")
public class DocumentProcessingInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int documentProcessingId;
	
	@Column(name = "contractId")
	private int contractId;
	
	@Column(name = "impactedPageNo")
	private int impactedPageNo;
	
	@Column(name = "impactedParagraphNo")
	private int impactedParagraphNo;
	
	@Column(name = "startPosition")
	private int startPosition;
	
	@Column(name = "endPosition")
	private int endPosition;
	
	@Column(name = "paragraphText")
	private String paragraphText;

	public int getDocumentProcessingId() {
		return documentProcessingId;
	}

	public void setDocumentProcessingId(int documentProcessingId) {
		this.documentProcessingId = documentProcessingId;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getImpactedPageNo() {
		return impactedPageNo;
	}

	public void setImpactedPageNo(int impactedPageNo) {
		this.impactedPageNo = impactedPageNo;
	}

	public int getImpactedParagraphNo() {
		return impactedParagraphNo;
	}

	public void setImpactedParagraphNo(int impactedParagraphNo) {
		this.impactedParagraphNo = impactedParagraphNo;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public String getParagraphText() {
		return paragraphText;
	}

	public void setParagraphText(String paragraphText) {
		this.paragraphText = paragraphText;
	}
			
}
