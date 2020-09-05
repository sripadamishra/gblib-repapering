package com.gblib.core.repapering.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowReview;

public interface IWorkflowReviewRepository<U> extends CrudRepository<WorkflowReview,Long> {

	List<WorkflowReview> findByContractId(int contractId);
	List<WorkflowReview> findByContractIdAndStatusId(int contractId,int statusId);
}
