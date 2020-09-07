/**
 * 
 */
package com.gblib.core.repapering.controller;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import com.gblib.core.repapering.model.WorkflowClose;
import com.gblib.core.repapering.model.WorkflowVerify;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowCloseService;
import com.gblib.core.repapering.services.WorkflowVerifyService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class WorkflowVerifyController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowVerifyService workflowVerifyService;
	
	@Autowired
	WorkflowCloseService workflowCloseService;
	
		
	@RequestMapping(value = "/find/workflow/verify/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowVerify> getWorkflowVerifyDetails(@PathVariable int contractId) {		
		return workflowVerifyService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/verify/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowVerify> getWorkflowVerifyDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowVerifyService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/verify", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowVerify saveWorkflowVerify(@RequestBody String strWorkflowVerify) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowVerify workflowVerify = null;
		try {
			workflowVerify = objmapper.readValue(strWorkflowVerify, WorkflowVerify.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowVerifyService.saveWorkflowVerify(workflowVerify);
	}
	
	@RequestMapping(value = "verify/workflow/{contractid}", method = RequestMethod.POST)
	public @ResponseBody Contract authVerifyWorkflow(@PathVariable int contractid) {
		//Step 1: Find the contract whose Edit is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.AuthTreasury.ordinal() + 1);
				
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowVerify> lstworkflowVerify = workflowVerifyService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowVerify && lstworkflowVerify.size() > 0)
			{
				WorkflowVerify workflowVerify = lstworkflowVerify.get(0);
				workflowVerify.setComments("Verification is completed");
				workflowVerify.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowVerify.setUpdatedBy(workflowVerify.getAssignedTo());
				workflowVerify.setUpdatedOn(updatedOn);
				
				workflowVerifyService.saveWorkflowVerify(workflowVerify);
				
				WorkflowClose workflowClose = new WorkflowClose();
				workflowClose.setAssignedTo(workflowVerify.getAssignedTo());
				workflowClose.setContractId(contractid);
				workflowClose.setComments("Closure is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowClose.setCreatedOn(updatedOn);
				workflowClose.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowClose.setUpdatedOn(updatedOn);
				
				workflowCloseService.saveWorkflowClose(workflowClose);
				
				
				con.setCurrStatusId(WorkflowStageEnums.Verify.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		return con;
}
	
}
