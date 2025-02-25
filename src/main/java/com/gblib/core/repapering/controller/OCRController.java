 package com.gblib.core.repapering.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.global.WorkflowStageCompletionResultEnums;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.WorkflowInitiate;
import com.gblib.core.repapering.model.WorkflowOCR;
import com.gblib.core.repapering.services.AmazonClient;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.OCRService;
import com.gblib.core.repapering.services.WorkflowInitiateService;
import com.gblib.core.repapering.services.WorkflowOCRService;

@RestController
 public class OCRController {

	
	@Value("${gblib.core.repapering.file.storage}")
	private String storageLocation;
	
	@Autowired
	OCRService ocrService;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowOCRService workflowOCRService;
	
	@Autowired
	WorkflowInitiateService workflowInitiateService;
	
	@Autowired
	AmazonClient amazonClient;
	
	@RequestMapping(value = "v1/ocr/workflow", method = RequestMethod.POST)
	public @ResponseBody Contract converttoPdf(@RequestBody int contractid) {
		//Get input
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.ScanUpload.ordinal()+1);		
		//
		if(null != con) {
			String input =con.getDocumentFileName();
			//Create output file name from the input appending with '_text'
			String output = input.substring(0, input.indexOf(".pdf")) + ".pdf";
			int ret = 1;
			//ret = ocrService.convert(input, output);
			ret = 1; // overwrite it.
			
			//Save into ContractWorkflowOCR table
			//Get the Pending record
			List<WorkflowOCR> lstworkflowPendingOCR = workflowOCRService.findByContractIdAndStatusId(contractid, WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
			
			if(null != lstworkflowPendingOCR && lstworkflowPendingOCR.size() > 0)
			{
				WorkflowOCR workflowPendingOCR = lstworkflowPendingOCR.get(0);
				Date updatedOn = new Timestamp(System.currentTimeMillis());
				int statusId = WorkflowStageCompletionResultEnums.Completed.ordinal() + 1; //Completed
				String comments = "";
				comments = "Scan File  is OCRed successfully.";
				if(0 == ret) {
					statusId = WorkflowStageCompletionResultEnums.Pending.ordinal() + 1;
					comments = "Scan File  OCR process is failed!";
				}

				workflowPendingOCR.setStatusId(statusId);
				workflowPendingOCR.setComments(comments);
				workflowPendingOCR.setUpdatedBy(con.getCreatedBy());
				workflowPendingOCR.setUpdatedOn(updatedOn);
				workflowOCRService.saveWorkflowOCR(workflowPendingOCR);
				//Step 2: Save to WorkflowInitiate with Pending
				
				WorkflowInitiate workflowInitiate = new WorkflowInitiate();				
				workflowInitiate.setAssignedTo(con.getCreatedBy());
				workflowInitiate.setComments("Initiate is Pending.");
				workflowInitiate.setContractId(con.getContractId());
				workflowInitiate.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//Pending
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowInitiate.setCreatedOn(updatedOn);
				workflowInitiate.setUpdatedOn(updatedOn);
				workflowInitiateService.saveWorkflowInitiate(workflowInitiate);
				
				//Step 3: Save to ContractDetails
				//Save into ContractDetails table // update currStatusId = OCR(2)
				con.setCurrStatusId(WorkflowStageEnums.OCR.ordinal()+1);
				con.setDocumentFileName(output);
				con = contractService.saveContract(con);
			}		
		}
		return con;
	}
	
	@RequestMapping(value = "/ocr/workflow", method = RequestMethod.POST)
	public @ResponseBody Contract converttoPdfAndUploadToS3(@RequestBody int contractid) {
		//Get input
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.ScanUpload.ordinal()+1);
		String key = "";
		//
		if(null != con) {
			String inputFileName =con.getDocumentFileName();
			// Add a 5 Sec delay for simulation.
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				System.out.println("Simulation delay does not work.");
			}
			//String outputFileName = inputFileName.substring(0, inputFileName.indexOf(".pdf")) +  ".pdf";
			String outputFileName = inputFileName;
			int ret = 1;
			//ret = ocrService.downloadFromS3andConvert(inputFileName, outputFileName);
			ret = 1; // overwrite it.
			//scan bucket does not have any contract prefixed so trim it.
			inputFileName = inputFileName.substring(inputFileName.indexOf("_")+1, inputFileName.length());
			
			if(storageLocation.compareToIgnoreCase("awss3") == 0) {
				amazonClient.copyFromScanToOCRS3bucket(inputFileName,outputFileName);
			}
			else {
				String inFilePath = ".\\scan" + File.separator + inputFileName;
				String outFilePath = ".\\ocr" + File.separator + outputFileName;
	        	File source = new File(inFilePath);
	        	File dest = new File(outFilePath);
	        	//copy here.
	        	try {	        		
	        		Files.copy(source.toPath(), dest.toPath());				
	        		System.out.println("Copying file is sucessful from path " + inFilePath + "to " + outFilePath);
	        	} catch (IOException e) {
	        		System.out.println("Copying file fails from path " + inFilePath + "to " + outFilePath);
	        		e.printStackTrace();
	        	}
			}
			
			//Save into ContractWorkflowOCR table
			//Get the Pending record
			List<WorkflowOCR> lstworkflowPendingOCR = workflowOCRService.findByContractIdAndStatusId(contractid, WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
			
			if(null != lstworkflowPendingOCR && lstworkflowPendingOCR.size() > 0)
			{
				WorkflowOCR workflowPendingOCR = lstworkflowPendingOCR.get(0);
				Date updatedOn = new Timestamp(System.currentTimeMillis());
				int statusId = WorkflowStageCompletionResultEnums.Completed.ordinal() + 1; //Completed
				String comments = "";
				comments = "Scan File  is OCRed successfully.";
				if(0 == ret) {
					statusId = WorkflowStageCompletionResultEnums.Pending.ordinal() + 1;
					comments = "Scan File  OCR process is failed!";
				}

				workflowPendingOCR.setStatusId(statusId);
				workflowPendingOCR.setComments(comments);
				workflowPendingOCR.setUpdatedBy(con.getCreatedBy());
				workflowPendingOCR.setUpdatedOn(updatedOn);
				workflowOCRService.saveWorkflowOCR(workflowPendingOCR);
				//Step 2: Save to WorkflowInitiate with Pending
				
				WorkflowInitiate workflowInitiate = new WorkflowInitiate();				
				workflowInitiate.setAssignedTo(con.getCreatedBy());
				workflowInitiate.setComments("Initiate is Pending.");
				workflowInitiate.setContractId(con.getContractId());
				workflowInitiate.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//Pending
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowInitiate.setCreatedOn(updatedOn);
				workflowInitiate.setUpdatedOn(updatedOn);
				workflowInitiateService.saveWorkflowInitiate(workflowInitiate);
				
				//Step 3: Save to ContractDetails
				//Save into ContractDetails table // update currStatusId = OCR(2)
				con.setCurrStatusId(WorkflowStageEnums.OCR.ordinal()+1);
				con.setDocumentFileName(outputFileName);
				con = contractService.saveContract(con);
				//
				// Writing to metadata bucket can be done here as well. TO DO:-
				//
			}		
		}
		return con;
	}
	
}
