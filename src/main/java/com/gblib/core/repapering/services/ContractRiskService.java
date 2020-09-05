package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.ContractRisk;
import com.gblib.core.repapering.repository.IContractRiskRepository;

@Service
public class ContractRiskService {

	@Autowired
	IContractRiskRepository<ContractRisk> contractRiskRepository;
	
	@Transactional
	public List<ContractRisk> findByContractId(int contractId) {
		
		List<ContractRisk> lstConRisks = contractRiskRepository.findByContractId(contractId);		
		return lstConRisks;
	}
	
	@Transactional
	public ContractRisk saveContractRisk(ContractRisk contractRisk) {
		return contractRiskRepository.save(contractRisk);
	}
	
}
