package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowAuthProgram;

public interface IWorkflowAuthProgramRepository<U> extends CrudRepository<WorkflowAuthProgram,Long> {

	List<WorkflowAuthProgram> findByContractId(int contractId);
	List<WorkflowAuthProgram> findByContractIdAndStatusId(int contractId,int statusId);
}
