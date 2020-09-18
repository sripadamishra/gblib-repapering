package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.LoanFinancialDetails;
import com.gblib.core.repapering.repository.ILoanFinancialDetailsRepository;

@Service
public class LoanFinancialDetailsService {

	@Autowired
	ILoanFinancialDetailsRepository<LoanFinancialDetails> loanFinancialDetailsRepository;
	
	@Transactional
	public LoanFinancialDetails findByContractId(int contractId) {
		
		LoanFinancialDetails loanDetails = loanFinancialDetailsRepository.findByContractId(contractId);
		return loanDetails;
	}
	
	public LoanFinancialDetails findByCounterPartyId(int counterPartyId) {
		
		LoanFinancialDetails loanDetails = loanFinancialDetailsRepository.findByCounterPartyId(counterPartyId);
		return loanDetails;
	}
}
