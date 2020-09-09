package com.gblib.core.repapering.model;

import java.util.Date;

public class LiborDocument {
	private int LIBOR_docposition;
	private int LIBOR_startpage;
	private int LIBOR_startpageposition;
	private String LIBOR_startsnippet;
	private String file;
	private String predictions;
	private String entity1;
	private String entity2;
	private String startdate;
	private String terminationdate;
	private String amount;
	private String currency;
	private String fallbackPresent;
	private int fallbackPosition;
	private int fallbackPage;	
	private String fallbackText;
	private int fallbackTextComplexity;
	
	
	
	
	
	
	
	
	public LiborDocument(int lIBOR_docposition, int lIBOR_startpage, int lIBOR_startpageposition, String lIBOR_startsnippet,
			String file, String predictions, String entity1, String entity2, String startdate, String terminationdate,
			String amount,String currency,String fallbackPresent,int fallbackPosition,int fallbackPage,String fallbackText,
			int fallbackTextComplexity) {
		super();
		LIBOR_docposition = lIBOR_docposition;
		LIBOR_startpage = lIBOR_startpage;
		LIBOR_startpageposition = lIBOR_startpageposition;
		LIBOR_startsnippet = lIBOR_startsnippet;
		this.file = file;
		this.predictions = predictions;
		this.entity1 = entity1;
		this.entity2 = entity2;
		this.startdate = startdate;
		this.terminationdate = terminationdate;
		this.amount = amount;
		this.currency = currency;
		this.fallbackPresent = fallbackPresent;
		this.fallbackPosition = fallbackPosition;
		this.fallbackPage = fallbackPage;
		this.fallbackText = fallbackText;
		this.fallbackTextComplexity = fallbackTextComplexity;
	}
	public int getLIBOR_docposition() {
		return LIBOR_docposition;
	}
	public void setLIBOR_docposition(int lIBOR_docposition) {
		LIBOR_docposition = lIBOR_docposition;
	}
	public int getLIBOR_startpage() {
		return LIBOR_startpage;
	}
	public void setLIBOR_startpage(int lIBOR_startpage) {
		LIBOR_startpage = lIBOR_startpage;
	}
	public int getLIBOR_startpageposition() {
		return LIBOR_startpageposition;
	}
	public void setLIBOR_startpageposition(int lIBOR_startpageposition) {
		LIBOR_startpageposition = lIBOR_startpageposition;
	}
	public String getLIBOR_startsnippet() {
		return LIBOR_startsnippet;
	}
	public void setLIBOR_startsnippet(String lIBOR_startsnippet) {
		LIBOR_startsnippet = lIBOR_startsnippet;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getPredictions() {
		return predictions;
	}
	public void setPredictions(String predictions) {
		this.predictions = predictions;
	}
	public String getEntity1() {
		return entity1;
	}
	public void setEntity1(String entity1) {
		this.entity1 = entity1;
	}
	public String getEntity2() {
		return entity2;
	}
	public void setEntity2(String entity2) {
		this.entity2 = entity2;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getTerminationdate() {
		return terminationdate;
	}
	public void setTerminationdate(String terminationdate) {
		this.terminationdate = terminationdate;
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
	public String getFallbackPresent() {
		return fallbackPresent;
	}
	public void setFallbackPresent(String fallbackPresent) {
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
	@Override
	public String toString() {
		return "LiborDocument [LIBOR_docposition=" + LIBOR_docposition + ", LIBOR_startpage=" + LIBOR_startpage
				+ ", LIBOR_startpageposition=" + LIBOR_startpageposition + ", LIBOR_startsnippet=" + LIBOR_startsnippet
				+ ", file=" + file + ", predictions=" + predictions + ", entity1=" + entity1 + ", entity2=" + entity2
				+ ", startdate=" + startdate + ", terminationdate=" + terminationdate + ", amount=" + amount
				+ "]";
	}	
	
	

}
