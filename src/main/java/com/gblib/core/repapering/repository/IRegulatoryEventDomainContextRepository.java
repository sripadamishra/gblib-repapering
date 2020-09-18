package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gblib.core.repapering.model.RegulatoryEventDomainContext;


public interface IRegulatoryEventDomainContextRepository<U> extends CrudRepository<RegulatoryEventDomainContext,Long> {

	List<RegulatoryEventDomainContext> findByContractType(int contractType);	
}
