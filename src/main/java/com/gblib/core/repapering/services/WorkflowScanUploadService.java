package com.gblib.core.repapering.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gblib.core.repapering.model.WorkflowScanUpload;
import com.gblib.core.repapering.repository.IWorkflowScanUploadRepository;

@Service
public class WorkflowScanUploadService {

	@Autowired
	IWorkflowScanUploadRepository<WorkflowScanUpload> workflowScanUploadRepository;
	
	@Transactional
	public WorkflowScanUpload findByContractId(int contractId) {
		
		WorkflowScanUpload workflow = workflowScanUploadRepository.findByContractId(contractId);
		return workflow;
	}
	
	@Transactional
	public WorkflowScanUpload saveWorkflowScanUpload(WorkflowScanUpload workflowscanUpload) {
		return workflowScanUploadRepository.save(workflowscanUpload);
	}
}
