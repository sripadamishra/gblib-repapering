package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowOCR;

public interface IWorkflowOCRRepository<U> extends CrudRepository<WorkflowOCR,Long> {

	List<WorkflowOCR> findByContractId(int contractId);
	List<WorkflowOCR> findByContractIdAndStatusId(int contractId,int statusId);
}
