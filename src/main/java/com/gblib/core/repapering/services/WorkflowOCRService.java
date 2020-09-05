package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowOCR;
import com.gblib.core.repapering.repository.IWorkflowOCRRepository;

@Service
public class WorkflowOCRService {

	@Autowired
	IWorkflowOCRRepository<WorkflowOCR> workflowOCRRepository;
	
	@Transactional
	public List<WorkflowOCR> findByContractId(int contractId) {
		
		List<WorkflowOCR> lstworkflow = workflowOCRRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowOCR> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowOCR> lstworkflow = workflowOCRRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowOCR saveWorkflowOCR(WorkflowOCR workflowOCR) {
		return workflowOCRRepository.save(workflowOCR);
	}
}
