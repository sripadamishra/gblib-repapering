package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowAuthRisk;

public interface IWorkflowAuthRiskRepository<U> extends CrudRepository<WorkflowAuthRisk,Long> {

	List<WorkflowAuthRisk> findByContractId(int contractId);
	List<WorkflowAuthRisk> findByContractIdAndStatusId(int contractId,int statusId);
}
