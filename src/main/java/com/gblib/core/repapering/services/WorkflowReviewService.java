package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowReview;
import com.gblib.core.repapering.repository.IWorkflowReviewRepository;

@Service
public class WorkflowReviewService {

	@Autowired
	IWorkflowReviewRepository<WorkflowReview> workflowReviewRepository;
	
	@Transactional
	public List<WorkflowReview> findByContractId(int contractId) {
		
		List<WorkflowReview> lstworkflow = workflowReviewRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowReview> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowReview> lstworkflow = workflowReviewRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowReview saveWorkflowReview(WorkflowReview workflowReview) {
		return workflowReviewRepository.save(workflowReview);
	}
}
