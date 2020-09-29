 package com.gblib.core.repapering.services;


import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.DocumentMetaDataExtended;

@Service
public class DocumentMetaDataExtendedService {

	@Autowired
	ContractService contractService;
	
	@Autowired
	AmazonClient amazonClient;
	
	@Value("${gblib.core.repapering.text.analytics.metadatadir}")
	private String analyticsDir;
	
	@Value("${gblib.core.repapering.text.analytics.docmetadata.filename}")
	private String docMetadataFilename;
	
	
	@Value("${gblib.core.repapering.file.storage}")
	private String storageLocation;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentMetaDataExtendedService.class);
	
	public 	DocumentMetaDataExtended getDocumentMataData(int contractId) {
		DocumentMetaDataExtended metadata = new DocumentMetaDataExtended();
		//Read the JSON file and get the object.
		String outFilePath ="";
		File jsonFile = null;
		boolean cloud = false;
		
		Contract con = contractService.findByContractId(contractId);
		String fileName = con.getDocumentFileName();
		byte[] content = null;
		
		if(con != null) {

			if(docMetadataFilename.isEmpty()) {
				docMetadataFilename = ".json";
			}
			
			fileName = fileName.substring(0, fileName.indexOf(".pdf")) + docMetadataFilename;

			if(storageLocation.compareToIgnoreCase("awss3") == 0) {
				cloud = true;
			}

			if(cloud){
				try {
					content = amazonClient.downloadAnalysiedMetadataFileFromS3bucket(fileName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				
				outFilePath = ".\\edit" +File.separator + fileName;
			}

			
			ObjectMapper mapper = new ObjectMapper();
			try {
				if(cloud) {
					metadata = mapper.readValue(content,DocumentMetaDataExtended.class);					
				}
				else {
					jsonFile = new File(outFilePath);
					metadata = mapper.readValue(jsonFile,DocumentMetaDataExtended.class);
				}
				
				if(metadata != null) {
					LOGGER.info("Json to object conversion is sucessful for DocumentMetaDataExtended.");
				}
				
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
		}
		return metadata;
	}


}
