package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowEdit;

public interface IWorkflowEditRepository<U> extends CrudRepository<WorkflowEdit,Long> {

	List<WorkflowEdit> findByContractId(int contractId);
	List<WorkflowEdit> findByContractIdAndStatusId(int contractId,int statusId);
}
