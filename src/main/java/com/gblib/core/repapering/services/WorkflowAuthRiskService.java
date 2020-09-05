package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowAuthRisk;
import com.gblib.core.repapering.repository.IWorkflowAuthRiskRepository;

@Service
public class WorkflowAuthRiskService {

	@Autowired
	IWorkflowAuthRiskRepository<WorkflowAuthRisk> workflowAuthRiskRepository;
	
	@Transactional
	public List<WorkflowAuthRisk> findByContractId(int contractId) {
		
		List<WorkflowAuthRisk> lstworkflow = workflowAuthRiskRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowAuthRisk> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowAuthRisk> lstworkflow = workflowAuthRiskRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowAuthRisk saveWorkflowAuthRisk(WorkflowAuthRisk workflowAuthRisk) {
		return workflowAuthRiskRepository.save(workflowAuthRisk);
	}
}
