package com.gblib.core.repapering.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.gblib.core.repapering.global.WorkflowStageCompletionResultEnums;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.WorkflowOCR;
import com.gblib.core.repapering.model.WorkflowScanUpload;
import com.gblib.core.repapering.services.AmazonClient;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.FileStorageService;
import com.gblib.core.repapering.services.WorkflowOCRService;
import com.gblib.core.repapering.services.WorkflowScanUploadService;

@RestController
public class S3bucketController {
//https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646
	private AmazonClient amazonClient;
	
	@Autowired
	S3bucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }
	
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
                
            //Upload file to S# bucket
        	try {
				this.amazonClient.uploadFile(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to upload file in S3 bucket");
				e.printStackTrace();
			}
        	
        	
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

    @PostMapping("/direct/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws Exception {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
    
    @GetMapping(value= "/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value= "fileName") final String keyName) {
        final byte[] data = amazonClient.downloadFile(keyName); 
        //writeByte(data);;
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + keyName + "\"")
                .body(resource);
    }
    
    
}
