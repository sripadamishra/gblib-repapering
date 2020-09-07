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
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowCloseService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class WorkflowCloseController {

	
	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowCloseService workflowCloseService;
	
		
	@RequestMapping(value = "/find/workflow/close/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowClose> getWorkflowCloseDetails(@PathVariable int contractId) {		
		return workflowCloseService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/close/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowClose> getWorkflowCloseDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowCloseService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/close", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowClose saveWorkflowClose(@RequestBody String strWorkflowClose) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowClose workflowClose = null;
		try {
			workflowClose = objmapper.readValue(strWorkflowClose, WorkflowClose.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowCloseService.saveWorkflowClose(workflowClose);
	}
	
	
	@RequestMapping(value = "close/workflow/{contractid}", method = RequestMethod.POST)
	public @ResponseBody Contract authCloseWorkflow(@PathVariable int contractid) {
		//Step 1: Find the contract whose Edit is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Verify.ordinal() + 1);
				
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowClose> lstworkflowClose = workflowCloseService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowClose && lstworkflowClose.size() > 0)
			{
				WorkflowClose workflowClose = lstworkflowClose.get(0);
				workflowClose.setComments("Closure is completed");
				workflowClose.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowClose.setUpdatedBy(workflowClose.getAssignedTo());
				workflowClose.setUpdatedOn(updatedOn);
				
				workflowCloseService.saveWorkflowClose(workflowClose);
									
				
				con.setCurrStatusId(WorkflowStageEnums.Close.ordinal() + 1);
				con = contractService.saveContract(con);
				
			}
			
		}
		return con;
}
	
}
