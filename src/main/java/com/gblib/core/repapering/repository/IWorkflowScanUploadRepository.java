package com.gblib.core.repapering.repository;

import org.springframework.data.repository.CrudRepository;
import com.gblib.core.repapering.model.WorkflowScanUpload;

public interface IWorkflowScanUploadRepository<U> extends CrudRepository<WorkflowScanUpload,Long> {

	WorkflowScanUpload findByContractId(int contractId);
}
