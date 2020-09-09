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
import com.gblib.core.repapering.model.WorkflowAuthTreasury;
import com.gblib.core.repapering.model.WorkflowVerify;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowAuthTreasuryService;
import com.gblib.core.repapering.services.WorkflowVerifyService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthTreasuryController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowAuthTreasuryService workflowAuthTreasuryService;
	
	@Autowired
	WorkflowVerifyService workflowVerifyService;
	
		
	@RequestMapping(value = "/find/workflow/authtreasury/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthTreasury> getWorkflowAuthTreasuryDetails(@PathVariable int contractId) {		
		return workflowAuthTreasuryService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/authtreasury/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthTreasury> getWorkflowAuthTreasuryDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowAuthTreasuryService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/authtreasury", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthTreasury saveWorkflowAuthTreasury(@RequestBody String strWorkflowAuthTreasury) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowAuthTreasury workflowAuthTreasury = null;
		try {
			workflowAuthTreasury = objmapper.readValue(strWorkflowAuthTreasury, WorkflowAuthTreasury.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowAuthTreasuryService.saveWorkflowAuthTreasury(workflowAuthTreasury);
	}
	
	@RequestMapping(value = "authtreasury/workflow", method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthTreasury authRiskWorkflow(@RequestBody int contractid) {
		//Step 1: Find the contract whose Edit is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.AuthRisk.ordinal() + 1);
		WorkflowAuthTreasury workflowAuthTreasury = null;		
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowAuthTreasury> lstworkflowAuthTreasury = workflowAuthTreasuryService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowAuthTreasury && lstworkflowAuthTreasury.size() > 0)
			{
				workflowAuthTreasury = lstworkflowAuthTreasury.get(0);
				workflowAuthTreasury.setComments("AuthTreasury is completed");
				workflowAuthTreasury.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowAuthTreasury.setUpdatedBy(workflowAuthTreasury.getAssignedTo());
				workflowAuthTreasury.setUpdatedOn(updatedOn);
				
				workflowAuthTreasuryService.saveWorkflowAuthTreasury(workflowAuthTreasury);
				
				WorkflowVerify workflowVerify = new WorkflowVerify();
				workflowVerify.setAssignedTo(workflowAuthTreasury.getAssignedTo());
				workflowVerify.setContractId(contractid);
				workflowVerify.setComments("Verification is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowVerify.setCreatedOn(updatedOn);
				workflowVerify.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowVerify.setUpdatedOn(updatedOn);
				
				workflowVerifyService.saveWorkflowVerify(workflowVerify);
				
				
				con.setCurrStatusId(WorkflowStageEnums.AuthTreasury.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		return workflowAuthTreasury;
}

}
