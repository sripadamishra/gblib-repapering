package com.gblib.core.repapering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowAuthProgram;
import com.gblib.core.repapering.repository.IWorkflowAuthProgramRepository;

@Service
public class WorkflowAuthProgramService {

	@Autowired
	IWorkflowAuthProgramRepository<WorkflowAuthProgram> workflowAuthProgramRepository;
	
	@Transactional
	public List<WorkflowAuthProgram> findByContractId(int contractId) {
		
		List<WorkflowAuthProgram> lstworkflow = workflowAuthProgramRepository.findByContractId(contractId);
		return lstworkflow;
	}
	
	@Transactional
	public List<WorkflowAuthProgram> findByContractIdAndStatusId(int contractId, int statusId) {
		
		List<WorkflowAuthProgram> lstworkflow = workflowAuthProgramRepository.findByContractIdAndStatusId(contractId,statusId);
		return lstworkflow;
	}
	
	@Transactional
	public WorkflowAuthProgram saveWorkflowAuthProgram(WorkflowAuthProgram workflowAuthProgram) {
		return workflowAuthProgramRepository.save(workflowAuthProgram);
	}
}
