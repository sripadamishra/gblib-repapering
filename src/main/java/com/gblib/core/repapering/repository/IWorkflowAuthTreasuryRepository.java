package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowAuthTreasury;

public interface IWorkflowAuthTreasuryRepository<U> extends CrudRepository<WorkflowAuthTreasury,Long> {

	List<WorkflowAuthTreasury> findByContractId(int contractId);
	List<WorkflowAuthTreasury> findByContractIdAndStatusId(int contractId,int statusId);
}
