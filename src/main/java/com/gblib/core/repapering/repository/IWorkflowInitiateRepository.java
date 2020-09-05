package com.gblib.core.repapering.repository;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowInitiate;

public interface IWorkflowInitiateRepository<U> extends CrudRepository<WorkflowInitiate,Long> {

	WorkflowInitiate findByContractId(int contractId);
}
