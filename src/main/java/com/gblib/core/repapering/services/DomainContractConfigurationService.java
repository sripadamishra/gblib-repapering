package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.DomainContractConfiguration;
import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.repository.IDomainContractConfigurationRepository;
import com.gblib.core.repapering.repository.IUserRepository;

@Service
public class DomainContractConfigurationService {

	@Autowired
	IDomainContractConfigurationRepository<DomainContractConfiguration> domainContractConfigurationRepository;
	
	public List<DomainContractConfiguration> findByContractIdAndDomainContextDictionaryId(int contractId,String domainContextDictionaryId) {
		return domainContractConfigurationRepository.findByContractIdAndDomainContextDictionaryId(contractId, domainContextDictionaryId);
	}
	
	public List<DomainContractConfiguration> findByContractIdAndRegulatoryEventId(int contractId,int regulatoryEventId) {
		return domainContractConfigurationRepository.findByContractIdAndRegulatoryEventId(contractId, regulatoryEventId);
	}
	
	@Transactional
	public List<DomainContractConfiguration> save(List<DomainContractConfiguration> configDtls){
		return (List<DomainContractConfiguration>) domainContractConfigurationRepository.saveAll(configDtls);
	}
	
	@Transactional
	public DomainContractConfiguration save(DomainContractConfiguration configDtl){
		return (DomainContractConfiguration) domainContractConfigurationRepository.save(configDtl);
	}
}
