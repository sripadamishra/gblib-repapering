package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowClose;
import com.gblib.core.repapering.repository.IWorkflowCloseRepository;

@Service
public class WorkflowCloseService {

	@Autowired
	IWorkflowCloseRepository<WorkflowClose> workflowCloseRepository;
	
	@Transactional
	public List<WorkflowClose> findByContractId(int contractId) {
		
		List<WorkflowClose> lstworkflow = workflowCloseRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowClose> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowClose> lstworkflow = workflowCloseRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowClose saveWorkflowClose(WorkflowClose workflowClose) {
		return workflowCloseRepository.save(workflowClose);
	}
}
