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
import com.gblib.core.repapering.model.WorkflowAuthRisk;
import com.gblib.core.repapering.model.WorkflowAuthTreasury;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowAuthRiskService;
import com.gblib.core.repapering.services.WorkflowAuthTreasuryService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class WorkflowAuthRiskController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowAuthRiskService workflowAuthRiskService;
	
	@Autowired
	WorkflowAuthTreasuryService workflowAuthTreasuryService;
	
		
	@RequestMapping(value = "/find/workflow/authrisk/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthRisk> getWorkflowAuthRiskDetails(@PathVariable int contractId) {		
		return workflowAuthRiskService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/authrisk/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthRisk> getWorkflowAuthRiskDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowAuthRiskService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/authrisk", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthRisk saveWorkflowAuthRisk(@RequestBody String strWorkflowAuthRisk) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowAuthRisk workflowAuthRisk = null;
		try {
			workflowAuthRisk = objmapper.readValue(strWorkflowAuthRisk, WorkflowAuthRisk.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowAuthRiskService.saveWorkflowAuthRisk(workflowAuthRisk);
	}
	
	@RequestMapping(value = "authrisk/workflow/{contractid}", method = RequestMethod.POST)
	public @ResponseBody Contract authRiskWorkflow(@PathVariable int contractid) {
		//Step 1: Find the contract whose Edit is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.AuthProgram.ordinal() + 1);
				
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowAuthRisk> lstworkflowAuthRisk = workflowAuthRiskService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowAuthRisk && lstworkflowAuthRisk.size() > 0)
			{
				WorkflowAuthRisk workflowAuthRisk = lstworkflowAuthRisk.get(0);
				workflowAuthRisk.setComments("AuthRisk is completed");
				workflowAuthRisk.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowAuthRisk.setUpdatedBy(workflowAuthRisk.getAssignedTo());
				workflowAuthRisk.setUpdatedOn(updatedOn);
				
				workflowAuthRiskService.saveWorkflowAuthRisk(workflowAuthRisk);
				
				WorkflowAuthTreasury workflowAuthTreasury = new WorkflowAuthTreasury();
				workflowAuthTreasury.setAssignedTo(workflowAuthRisk.getAssignedTo());
				workflowAuthTreasury.setContractId(contractid);
				workflowAuthTreasury.setComments("AuthTreasury is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowAuthTreasury.setCreatedOn(updatedOn);
				workflowAuthTreasury.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowAuthTreasury.setUpdatedOn(updatedOn);
				
				workflowAuthTreasuryService.saveWorkflowAuthTreasury(workflowAuthTreasury);
				
				
				con.setCurrStatusId(WorkflowStageEnums.AuthRisk.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		return con;
}

}
