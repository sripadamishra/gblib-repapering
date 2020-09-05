package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.ContractRisk;

public interface IContractRiskRepository<U> extends CrudRepository<ContractRisk,Long> {

	List<ContractRisk> findByContractId(int contractId);
}
