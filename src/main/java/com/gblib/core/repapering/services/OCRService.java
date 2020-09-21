package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gblib.core.repapering.services.business.IOCRProcessor;

@Service
public class OCRService {

	@Autowired
	IOCRProcessor ocrProcessor;
	
	
	public int convert(String inputFile,String outputFile) {
		return ocrProcessor.convert(inputFile, outputFile);
	}
	
	public int downloadFromS3andConvert(String inputFileName,String outputFileName) {
		return ocrProcessor.downloadAndConvert(inputFileName, outputFileName);
	}
	
}
