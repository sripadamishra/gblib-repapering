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
import com.gblib.core.repapering.model.WorkflowAuthProgram;
import com.gblib.core.repapering.model.WorkflowAuthRisk;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowAuthProgramService;
import com.gblib.core.repapering.services.WorkflowAuthRiskService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthProgramController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowAuthProgramService workflowAuthProgramService;
	
	@Autowired
	WorkflowAuthRiskService workflowAuthRiskService;
	
	
		
	@RequestMapping(value = "/find/workflow/authprogram/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthProgram> getWorkflowAuthProgramDetails(@PathVariable int contractId) {		
		return workflowAuthProgramService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/authprogram/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthProgram> getWorkflowAuthProgramDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowAuthProgramService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/authprogram", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthProgram saveWorkflowAuthProgram(@RequestBody String strWorkflowAuthProgram) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowAuthProgram workflowAuthProgram = null;
		try {
			workflowAuthProgram = objmapper.readValue(strWorkflowAuthProgram, WorkflowAuthProgram.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowAuthProgramService.saveWorkflowAuthProgram(workflowAuthProgram);
	}
	
	@RequestMapping(value = "authprogram/workflow", method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthProgram authLegalWorkflow(@RequestBody int contractid) {
		//Step 1: Find the contract whose Edit is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.AuthLegal.ordinal() + 1);
		WorkflowAuthProgram workflowAuthProgram = null;
		
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowAuthProgram> lstworkflowAuthProgram = workflowAuthProgramService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowAuthProgram && lstworkflowAuthProgram.size() > 0)
			{
				workflowAuthProgram = lstworkflowAuthProgram.get(0);
				workflowAuthProgram.setComments("AuthProgram is completed");
				workflowAuthProgram.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowAuthProgram.setUpdatedBy(workflowAuthProgram.getAssignedTo());
				workflowAuthProgram.setUpdatedOn(updatedOn);
				
				workflowAuthProgram = workflowAuthProgramService.saveWorkflowAuthProgram(workflowAuthProgram);
				
				WorkflowAuthRisk workflowAuthRisk = new WorkflowAuthRisk();
				workflowAuthRisk.setAssignedTo(workflowAuthProgram.getAssignedTo());
				workflowAuthRisk.setContractId(contractid);
				workflowAuthRisk.setComments("AuthRisk is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowAuthRisk.setCreatedOn(updatedOn);
				workflowAuthRisk.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowAuthRisk.setUpdatedOn(updatedOn);
				
				workflowAuthRiskService.saveWorkflowAuthRisk(workflowAuthRisk);
				
				
				con.setCurrStatusId(WorkflowStageEnums.AuthProgram.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		return workflowAuthProgram;
	}

	
}
