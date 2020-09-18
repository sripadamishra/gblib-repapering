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
import com.gblib.core.repapering.model.WorkflowAuthLegal;
import com.gblib.core.repapering.model.WorkflowAuthProgram;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowAuthLegalService;
import com.gblib.core.repapering.services.WorkflowAuthProgramService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthLegalController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowAuthLegalService workflowAuthLegalService;
	
	@Autowired
	WorkflowAuthProgramService workflowAuthProgramService;
	
		
	@RequestMapping(value = "/find/workflow/authlegal/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthLegal> getWorkflowAuthLegalDetails(@PathVariable int contractId) {		
		return workflowAuthLegalService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/authlegal/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthLegal> getWorkflowAuthLegalDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowAuthLegalService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/authlegal", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthLegal saveWorkflowAuthLegal(@RequestBody String strWorkflowAuthLegal) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowAuthLegal workflowAuthLegal = null;
		try {
			workflowAuthLegal = objmapper.readValue(strWorkflowAuthLegal, WorkflowAuthLegal.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowAuthLegalService.saveWorkflowAuthLegal(workflowAuthLegal);
	}
	
	@RequestMapping(value = "authlegal/workflow", method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthLegal authLegalWorkflow(@RequestBody int contractid) {
		//Step 1: Find the contract whose Edit is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Edit.ordinal() + 1);
		WorkflowAuthLegal workflowAuthLegal = null;
		if(null != con) { 
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowAuthLegal> lstworkflowAuthLegal = workflowAuthLegalService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowAuthLegal && lstworkflowAuthLegal.size() > 0)
			{
				workflowAuthLegal = lstworkflowAuthLegal.get(0);				
				workflowAuthLegal.setComments("AuthLegal is completed");
				workflowAuthLegal.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowAuthLegal.setUpdatedBy(workflowAuthLegal.getAssignedTo());
				workflowAuthLegal.setUpdatedOn(updatedOn);
				
				workflowAuthLegal = workflowAuthLegalService.saveWorkflowAuthLegal(workflowAuthLegal);
				
				WorkflowAuthProgram workflowAuthProgram = new WorkflowAuthProgram();
				workflowAuthProgram.setAssignedTo(workflowAuthLegal.getAssignedTo());
				workflowAuthProgram.setContractId(contractid);
				workflowAuthProgram.setComments("AuthProgram is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowAuthProgram.setCreatedOn(updatedOn);
				workflowAuthProgram.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowAuthProgram.setUpdatedOn(updatedOn);
				
				workflowAuthProgramService.saveWorkflowAuthProgram(workflowAuthProgram);
				
				
				con.setCurrStatusId(WorkflowStageEnums.AuthLegal.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		//Print it
		System.out.println("AuthLegal-AssignedTo" + workflowAuthLegal.getAssignedTo());
		System.out.println("AuthLegal-ContractId" + workflowAuthLegal.getContractId());
		//
		return workflowAuthLegal;
	}
	
}
