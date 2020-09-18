package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gblib.core.repapering.model.DomainContractConfiguration;


public interface IDomainContractConfigurationRepository<U> extends CrudRepository<DomainContractConfiguration,Long> {
	
	List<DomainContractConfiguration> findByContractIdAndDomainContextDictionaryId(int contractId,String domainContextDictionaryId);
	List<DomainContractConfiguration> findByContractIdAndRegulatoryEventId(int contractId,int regulatoryEventId);
	
}
