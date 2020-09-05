package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowAuthLegal;

public interface IWorkflowAuthLegalRepository<U> extends CrudRepository<WorkflowAuthLegal,Long> {

	List<WorkflowAuthLegal> findByContractId(int contractId);
	List<WorkflowAuthLegal> findByContractIdAndStatusId(int contractId,int statusId);
}
