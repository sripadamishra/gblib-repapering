package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowVerify;

public interface IWorkflowVerifyRepository<U> extends CrudRepository<WorkflowVerify,Long> {

	List<WorkflowVerify> findByContractId(int contractId);
	List<WorkflowVerify> findByContractIdAndStatusId(int contractId,int statusId);
}
