package com.gblib.core.repapering.model;

import java.util.List;

public class DocumentUpdateRequest {
	private int contractId;
	List<DocumentMetaData> listDocumentMetadata;
	public int getContractId() {
		return contractId;
	}
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}
	public List<DocumentMetaData> getListDocumentMetadata() {
		return listDocumentMetadata;
	}
	public void setListDocumentMetadata(List<DocumentMetaData> listDocumentMetadata) {
		this.listDocumentMetadata = listDocumentMetadata;
	}
}
