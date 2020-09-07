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
import com.gblib.core.repapering.model.WorkflowEdit;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowAuthLegalService;
import com.gblib.core.repapering.services.WorkflowEditService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class WorkflowEditController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowEditService workflowEditService;
	
	@Autowired
	WorkflowAuthLegalService workflowAuthLegalService;
	
		
	@RequestMapping(value = "/find/workflow/edit/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowEdit> getWorkflowEditDetails(@PathVariable int contractId) {		
		return workflowEditService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/edit/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowEdit> getWorkflowEditDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowEditService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/edit", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowEdit saveWorkflowEdit(@RequestBody String strWorkflowEdit) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowEdit workflowEdit = null;
		try {
			workflowEdit = objmapper.readValue(strWorkflowEdit, WorkflowEdit.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowEditService.saveWorkflowEdit(workflowEdit);
	}
	
	
	@RequestMapping(value = "edit/workflow/{contractid}", method = RequestMethod.POST)
	public @ResponseBody Contract editWorkflow(@PathVariable int contractid) {
		//Step 1: Find the contract whose Review(4) is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -Edit.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Review.ordinal() + 1);
				
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowEdit> lstworkflowEdit = workflowEditService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowEdit && lstworkflowEdit.size() > 0)
			{
				WorkflowEdit workflowEdit = lstworkflowEdit.get(0);
				workflowEdit.setComments("Edit is completed");
				workflowEdit.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowEdit.setUpdatedBy(workflowEdit.getAssignedTo());
				workflowEdit.setUpdatedOn(updatedOn);
				
				workflowEditService.saveWorkflowEdit(workflowEdit);
				
				WorkflowAuthLegal workflowAuthLegal = new WorkflowAuthLegal();
				workflowAuthLegal.setAssignedTo(workflowEdit.getAssignedTo());
				workflowAuthLegal.setContractId(contractid);
				workflowAuthLegal.setComments("AuthLegal is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowAuthLegal.setCreatedOn(updatedOn);
				workflowAuthLegal.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowAuthLegal.setUpdatedOn(updatedOn);
				
				workflowAuthLegalService.saveWorkflowAuthLegal(workflowAuthLegal);
				
				
				con.setCurrStatusId(WorkflowStageEnums.Edit.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		return con;
	}
}
