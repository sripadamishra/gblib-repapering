package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowClose;

public interface IWorkflowCloseRepository<U> extends CrudRepository<WorkflowClose,Long> {

	List<WorkflowClose> findByContractId(int contractId);
	List<WorkflowClose> findByContractIdAndStatusId(int contractId,int statusId);
}
