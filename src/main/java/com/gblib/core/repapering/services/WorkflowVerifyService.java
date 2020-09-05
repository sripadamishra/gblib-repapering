package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowVerify;
import com.gblib.core.repapering.repository.IWorkflowVerifyRepository;

@Service
public class WorkflowVerifyService {

	@Autowired
	IWorkflowVerifyRepository<WorkflowVerify> workflowVerifyRepository;
	
	@Transactional
	public List<WorkflowVerify> findByContractId(int contractId) {
		
		List<WorkflowVerify> lstworkflow = workflowVerifyRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowVerify> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowVerify> lstworkflow = workflowVerifyRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowVerify saveWorkflowVerify(WorkflowVerify workflowVerify) {
		return workflowVerifyRepository.save(workflowVerify);
	}
}
