package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowEdit;
import com.gblib.core.repapering.repository.IWorkflowEditRepository;

@Service
public class WorkflowEditService {

	@Autowired
	IWorkflowEditRepository<WorkflowEdit> workflowEditRepository;
	
	@Transactional
	public List<WorkflowEdit> findByContractId(int contractId) {
		
		List<WorkflowEdit> lstworkflow = workflowEditRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowEdit> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowEdit> lstworkflow = workflowEditRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowEdit saveWorkflowEdit(WorkflowEdit workflowEdit) {
		return workflowEditRepository.save(workflowEdit);
	}
}
