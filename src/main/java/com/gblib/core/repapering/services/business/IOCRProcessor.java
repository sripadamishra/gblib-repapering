package com.gblib.core.repapering.services.business;

public interface IOCRProcessor {
	
	public int convert(String inputFile,String outputFile); 
	public int downloadAndConvert(String inputFile,String outputFile); 
}
