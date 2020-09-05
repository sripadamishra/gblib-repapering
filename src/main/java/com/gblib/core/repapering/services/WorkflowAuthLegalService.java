package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowAuthLegal;
import com.gblib.core.repapering.repository.IWorkflowAuthLegalRepository;

@Service
public class WorkflowAuthLegalService {

	@Autowired
	IWorkflowAuthLegalRepository<WorkflowAuthLegal> workflowAuthLegalRepository;
	
	@Transactional
	public List<WorkflowAuthLegal> findByContractId(int contractId) {
		
		List<WorkflowAuthLegal> lstworkflow = workflowAuthLegalRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowAuthLegal> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowAuthLegal> lstworkflow = workflowAuthLegalRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowAuthLegal saveWorkflowAuthLegal(WorkflowAuthLegal workflowAuthLegal) {
		return workflowAuthLegalRepository.save(workflowAuthLegal);
	}
}
