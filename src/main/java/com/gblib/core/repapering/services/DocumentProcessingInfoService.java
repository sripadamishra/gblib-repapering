package com.gblib.core.repapering.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.repository.IDocumentProcessingInfoRepository;
@Transactional
@Service
public class DocumentProcessingInfoService {

	@Autowired
	IDocumentProcessingInfoRepository<DocumentProcessingInfo> documentProcessingInfoRepository;
	
	@Transactional
	public List<DocumentProcessingInfo> findByCounterPartyName(String counterPartyName) {
		
		List<DocumentProcessingInfo> doc = documentProcessingInfoRepository.findByCounterPartyName(counterPartyName);
		return doc;
	}
	
	
	public List<DocumentProcessingInfo> findByDocFileName(String docFileName) {
		
		List<DocumentProcessingInfo> doc = documentProcessingInfoRepository.findByDocFileName(docFileName);
		return doc;
	}
	
	public List<DocumentProcessingInfo> saveDocumentProcessingInfo(List<DocumentProcessingInfo> lstDocInfo){
		
		return (List<DocumentProcessingInfo>) documentProcessingInfoRepository.saveAll(lstDocInfo);
		
	}
}
