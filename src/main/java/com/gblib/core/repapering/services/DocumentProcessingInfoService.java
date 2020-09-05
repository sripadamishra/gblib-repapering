package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.repository.IDocumentProcessingInfoRepository;

@Service
public class DocumentProcessingInfoService {

	@Autowired
	IDocumentProcessingInfoRepository<DocumentProcessingInfo> documentProcessingInfoRepository;
	
	@Transactional
	public List<DocumentProcessingInfo> findByContractId(int contractId) {
		
		List<DocumentProcessingInfo> doc = documentProcessingInfoRepository.findByContractId(contractId);
		return doc;
	}
	
	public List<DocumentProcessingInfo> saveDocumentProcessingInfo(List<DocumentProcessingInfo> lstDocInfo){
		
		return (List<DocumentProcessingInfo>) documentProcessingInfoRepository.saveAll(lstDocInfo);
		
	}
}
