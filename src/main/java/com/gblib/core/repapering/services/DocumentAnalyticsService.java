package com.gblib.core.repapering.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.DocumentMetaData;
import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.repository.IUserRepository;

@Service
public class DocumentAnalyticsService {

	//@Autowired
	//IDocumentAnalyticsRepository<User> documentAnalyticsRepository;
	
	public List<DocumentMetaData> analysetDatafromContractDoc(int contractId) {
		List<DocumentMetaData> metadata = new ArrayList<DocumentMetaData>();
		
		//Get the document name from the contractId
		//PdfReading and populate the metadata and return to caller...
		return metadata;
	}
	
}
