package com.gblib.core.repapering.controller;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gblib.core.repapering.global.WorkflowStageCompletionResultEnums;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.Response;
import com.gblib.core.repapering.model.WorkflowOCR;
import com.gblib.core.repapering.model.WorkflowScanUpload;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.FileStorageService;
import com.gblib.core.repapering.services.RegulatoryEventDomainContextService;
import com.gblib.core.repapering.services.WorkflowOCRService;
import com.gblib.core.repapering.services.WorkflowScanUploadService;

@RestController
public class FileUploadController {
	@Autowired
    private FileStorageService fileStorageService;

	@Autowired
	WorkflowScanUploadService workflowScanUploadService;
	
	@Autowired
	WorkflowOCRService workflowOCRService;
	
	@Autowired
	ContractService contractService;
		
	
    @PostMapping("/v1/uploadFile")
    public Contract uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("userid") String userid) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName)
            .toUriString();
                    	    	
    		//Step:1 Save into ContractWorkflowScanUpload table with CompletionResult =2
    		  
    		Date createdOn = new Timestamp(System.currentTimeMillis());
    		Date updatedOn = new Timestamp(System.currentTimeMillis());
    		int statusId = WorkflowStageCompletionResultEnums.Completed.ordinal() + 1; // Update with CompletionResult =2 as Success
    		String comments = "File uploaded sucessfully.";
    		WorkflowScanUpload workflowScanUpload = new WorkflowScanUpload();
    		workflowScanUpload.setAssignedTo(userid);
    		workflowScanUpload.setUpdatedBy(userid);
    		workflowScanUpload.setCreatedOn(createdOn);
    		workflowScanUpload.setUpdatedOn(updatedOn);
    		workflowScanUpload.setStatusId(statusId);
    		workflowScanUpload.setComments(comments);
    		
    		WorkflowScanUpload savedworkflowScanUpload = workflowScanUploadService.saveWorkflowScanUpload(workflowScanUpload);
    		
    		//Step:2 Save into ContractWorkflowOCR table with CompletionResult = 1(Pending)
    		comments = "File OCR Pending";
    		WorkflowOCR workflowOCR = new WorkflowOCR();
    		workflowOCR.setAssignedTo(userid);
    		workflowOCR.setCreatedOn(createdOn);
    		workflowOCR.setUpdatedOn(updatedOn);
    		workflowOCR.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1); //Pending
    		workflowOCR.setComments(comments);
    		workflowOCR.setContractId(savedworkflowScanUpload.getContractId());
    		
    		workflowOCRService.saveWorkflowOCR(workflowOCR);
    		
    		Contract contract = new Contract();
    		//Step 3: Save into ContractDetails table
    		contract.setContractId(savedworkflowScanUpload.getContractId());
    		contract.setDocumentFileName(fileName);
    		contract.setCurrStatusId(WorkflowStageEnums.ScanUpload.ordinal()+1); // ScanUpload
    		contract.setCreatedBy(userid);
    		contract.setCreatedOn(createdOn);
    		contract = contractService.saveContract(contract);    		    		    		
    		
    		return contract;
    	}

}
