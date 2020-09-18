package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.DerivativeFinancialDetails;
import com.gblib.core.repapering.repository.IDerivativeFinancialDetailsRepository;

@Service
public class DerivativeFinancialDetailsService {

	@Autowired
	IDerivativeFinancialDetailsRepository<DerivativeFinancialDetails> derivativeFinancialDetailsRepository;
	
	@Transactional
	public DerivativeFinancialDetails findByContractId(int contractId) {
		
		DerivativeFinancialDetails derivativeDetails = derivativeFinancialDetailsRepository.findByContractId(contractId);
		return derivativeDetails;
	}
	
	public DerivativeFinancialDetails findByCounterPartyId(int counterPartyId) {
		
		DerivativeFinancialDetails derivativeDetails = derivativeFinancialDetailsRepository.findByCounterPartyId(counterPartyId);
		return derivativeDetails;
	}
	
}
