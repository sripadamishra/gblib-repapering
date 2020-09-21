package com.gblib.core.repapering.services;


import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.model.DocumentMetaDataExtended;

@Service
public class DocumentMetaDataExtendedService {

	@Value("${gblib.core.repapering.text.analytics.metadatadir}")
	private String analyticsDir;
	
	@Value("${gblib.core.repapering.text.analytics.docmetadata.filename}")
	private String docMetadataFilename;
	
	public 	DocumentMetaDataExtended getDocumentMataData(int contractId) {
	DocumentMetaDataExtended metadata = new DocumentMetaDataExtended();
	//Read the JSON file and get the object.
	String outFilePath ="";
	
	if(docMetadataFilename.isEmpty()) {
		docMetadataFilename = "_DOCMETADATA.JSON";
	}
	
	outFilePath = analyticsDir +File.separator + contractId + docMetadataFilename;
	ObjectMapper mapper = new ObjectMapper();
	try {
		File jsonFile = new File(outFilePath);
		metadata = mapper.readValue(jsonFile,DocumentMetaDataExtended.class);
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return metadata;
}
	
	
}
