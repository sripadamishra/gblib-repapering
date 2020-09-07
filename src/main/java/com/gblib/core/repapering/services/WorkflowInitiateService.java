package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowInitiate;
import com.gblib.core.repapering.repository.IWorkflowInitiateRepository;

@Service
public class WorkflowInitiateService {

	@Autowired
	IWorkflowInitiateRepository<WorkflowInitiate> workflowInitiateRepository;
	
	@Transactional
	public WorkflowInitiate findByContractId(int contractId) {
		
		WorkflowInitiate workflow = workflowInitiateRepository.findByContractId(contractId);
		return workflow;
	}
	
	@Transactional
	public WorkflowInitiate findByContractIdAndStatusId(int contractId, int statusId) {
		
		WorkflowInitiate workflow = workflowInitiateRepository.findByContractIdAndStatusId(contractId,statusId);
		return workflow;
	}
	
	@Transactional
	public WorkflowInitiate saveWorkflowInitiate(WorkflowInitiate workflowInitiate) {
		return workflowInitiateRepository.save(workflowInitiate);
	}
}
