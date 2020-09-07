/**
 * 
 */
package com.gblib.core.repapering.controller;


import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.global.WorkflowStageCompletionResultEnums;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.WorkflowInitiate;
import com.gblib.core.repapering.model.WorkflowReview;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowInitiateService;
import com.gblib.core.repapering.services.WorkflowReviewService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class WorkflowInitiateController {

	
	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowInitiateService workflowInitiateService;
	
	@Autowired
	WorkflowReviewService workflowReviewService;
	

		
		
	@RequestMapping(value = "/find/workflow/initiate/{contractId}", method = RequestMethod.GET)
	public @ResponseBody WorkflowInitiate getWorkflowInitiateDetails(@PathVariable int contractId) {		
		return workflowInitiateService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "save/workflow/initiate", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowInitiate saveWorkflowInitiate(@RequestBody String strWorkflowInitiate) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowInitiate workflowInitiate = null;
		try {
			workflowInitiate = objmapper.readValue(strWorkflowInitiate, WorkflowInitiate.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowInitiateService.saveWorkflowInitiate(workflowInitiate);
	}
	
	@RequestMapping(value = "initiate/workflow/{contractid}", method = RequestMethod.POST)
	public @ResponseBody Contract initiateWorkflow(@PathVariable int contractid) {
		//Step 1: Find the contract whose OCR is completed from contract Details.
		//Step 2: Perform the Initiate Operation. (Python Analytics,Read Excel and update DB.)
		//Step 3: If successful, update the workflowInitiate table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowReview with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage - Initiate.
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.OCR.ordinal()+1);
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			WorkflowInitiate workflowInitiate = workflowInitiateService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			workflowInitiate.setComments("Initiate is completed");
			workflowInitiate.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
			workflowInitiate.setUpdatedBy(workflowInitiate.getAssignedTo());
			workflowInitiate.setUpdatedOn(updatedOn);
			workflowInitiateService.saveWorkflowInitiate(workflowInitiate);
			
			WorkflowReview workflowReview = new WorkflowReview();
			workflowReview.setAssignedTo(workflowInitiate.getAssignedTo());
			workflowReview.setContractId(contractid);
			workflowReview.setComments("Review is pending");
			updatedOn = new Timestamp(System.currentTimeMillis());
			workflowReview.setCreatedOn(updatedOn);
			workflowReview.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
			workflowReview.setUpdatedOn(updatedOn);
			
			workflowReviewService.saveWorkflowReview(workflowReview);
			
			con.setCurrStatusId(WorkflowStageEnums.Initiate.ordinal() + 1);
			contractService.saveContract(con);					
		}
		return con;
	}
	
}
