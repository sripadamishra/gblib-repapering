package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowAuthTreasury;
import com.gblib.core.repapering.repository.IWorkflowAuthTreasuryRepository;

@Service
public class WorkflowAuthTreasuryService {

	@Autowired
	IWorkflowAuthTreasuryRepository<WorkflowAuthTreasury> workflowAuthTreasuryRepository;
	
	@Transactional
	public List<WorkflowAuthTreasury> findByContractId(int contractId) {
		
		List<WorkflowAuthTreasury> lstworkflow = workflowAuthTreasuryRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowAuthTreasury> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowAuthTreasury> lstworkflow = workflowAuthTreasuryRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowAuthTreasury saveWorkflowAuthTreasury(WorkflowAuthTreasury workflowAuthTreasury) {
		return workflowAuthTreasuryRepository.save(workflowAuthTreasury);
	}
}
