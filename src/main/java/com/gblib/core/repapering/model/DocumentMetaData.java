package com.gblib.core.repapering.model;

public class DocumentMetaData {

	String headerName;
	String headerContext;
	int headerPageNo;
	String headerTextContent;
	int    headerParagraphIndex;
	String headerFontName;
	String headerFontSize;
	float startLocationX;
	float startLocationY;
	float endLocationX;
	float endLocationY;
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public String getHeaderContext() {
		return headerContext;
	}
	public void setHeaderContext(String headerContext) {
		this.headerContext = headerContext;
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
}
