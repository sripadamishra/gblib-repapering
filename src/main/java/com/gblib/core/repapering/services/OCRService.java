package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gblib.core.repapering.services.business.IOCRProcessor;

@Service
public class OCRService {

	@Autowired
	IOCRProcessor ocrProcessor;
	
	
	public void convert(String inputFile,String outputFile) {
		ocrProcessor.convert(inputFile, outputFile);
	}
	
}
