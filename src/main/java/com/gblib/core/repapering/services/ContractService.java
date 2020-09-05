package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.repository.IContractRepository;

@Service
public class ContractService {

	@Autowired
	IContractRepository<Contract> contractRepository;
	
	@Transactional
	public Contract findByContractId(String contractId) {
		
		Contract contract = contractRepository.findByContractId(contractId);		
		return contract;
	}
	
	@Transactional
	public Contract findById(int id) {
		
		Contract contract = contractRepository.findById(id);		
		return contract;
	}
	
	public int setNewCurrStatusIdForContract(int currStatusId, int id) {
		return contractRepository.setNewCurrStatusIdForContract(currStatusId,id);
	}
	
	public Contract saveContract(Contract contract) {
		return contractRepository.save(contract);
	}
}
