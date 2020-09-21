package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.repository.IContractRepository;

@Service
public class ContractService {

	@Autowired
	IContractRepository<Contract> contractRepository;
	
	public List<Contract> getAllContracts(){
		List<Contract> contracts = contractRepository.getAllContracts();
		return contracts;
	}
	
	@Transactional
	public Contract findByContractId(int contractId) {
		
		Contract contract = contractRepository.findByContractId(contractId);		
		return contract;
	}
	
	@Transactional
	public Contract findByContractIdAndCurrStatusId(int contractId,int currStatusId) {
		
		Contract contract = contractRepository.findByContractIdAndCurrStatusId(contractId,currStatusId);		
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
