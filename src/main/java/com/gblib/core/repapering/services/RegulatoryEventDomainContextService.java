package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.RegulatoryEventDomainContext;
import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.repository.IRegulatoryEventDomainContextRepository;
import com.gblib.core.repapering.repository.IUserRepository;

@Service
public class RegulatoryEventDomainContextService {

	@Autowired
	IRegulatoryEventDomainContextRepository<RegulatoryEventDomainContext> regulatoryEventDomainContextRepository;
	
	public List<RegulatoryEventDomainContext> findByContractType(int contractType) {		
		return regulatoryEventDomainContextRepository.findByContractType(contractType);		
	}
	
	@Transactional
	public List<RegulatoryEventDomainContext> save(List<RegulatoryEventDomainContext> domainContextDtls){
		return (List<RegulatoryEventDomainContext>) regulatoryEventDomainContextRepository.saveAll(domainContextDtls);
	}
}
