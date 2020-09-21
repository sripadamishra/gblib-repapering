package com.gblib.core.repapering.model;

import java.util.List;


public class DocumentMetaDataExtended{
	List<String> listpageLevelOnlyRawText;
	List<DocumentMetaData> listdocumentMetaData;
	public List<String> getListpageLevelOnlyRawText() {
		return listpageLevelOnlyRawText;
	}
	public void setListpageLevelOnlyRawText(List<String> listpageLevelOnlyRawText) {
		this.listpageLevelOnlyRawText = listpageLevelOnlyRawText;
	}
	public List<DocumentMetaData> getListdocumentMetaData() {
		return listdocumentMetaData;
	}
	public void setListdocumentMetaData(List<DocumentMetaData> listdocumentMetaData) {
		this.listdocumentMetaData = listdocumentMetaData;
	}
}
