package com.gblib.core.repapering.repository;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.DerivativeFinancialDetails;

public interface IDerivativeFinancialDetailsRepository<U> extends CrudRepository<DerivativeFinancialDetails,Long> {

	DerivativeFinancialDetails findByContractId(int contractId);
}
