package com.gblib.core.repapering.repository;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.LoanFinancialDetails;

public interface ILoanFinancialDetailsRepository<U> extends CrudRepository<LoanFinancialDetails,Long> {

	LoanFinancialDetails findByContractId(int contractId);
	LoanFinancialDetails findByCounterPartyId(int counterPartyId);	
}
