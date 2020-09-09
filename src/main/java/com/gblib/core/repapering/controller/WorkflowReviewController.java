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
import com.gblib.core.repapering.model.WorkflowEdit;
import com.gblib.core.repapering.model.WorkflowReview;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowEditService;
import com.gblib.core.repapering.services.WorkflowReviewService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowReviewController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowReviewService workflowReviewService;
	
	@Autowired
	WorkflowEditService workflowEditService;
	
		
	@RequestMapping(value = "/find/workflow/review/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowReview> getWorkflowReviewDetails(@PathVariable int contractId) {		
		return workflowReviewService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/review/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowReview> getWorkflowReviewDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowReviewService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/review", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowReview saveWorkflowReview(@RequestBody String strWorkflowReview) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowReview workflowReview = null;
		try {
			workflowReview = objmapper.readValue(strWorkflowReview, WorkflowReview.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowReviewService.saveWorkflowReview(workflowReview);
	}
	
	@RequestMapping(value = "review/workflow", method = RequestMethod.POST)
	public Contract reviewWorkflow(@RequestBody int contractid) {
		//Step 1: Find the contract whose Initiate(3) is completed from contract Details.
		//Step 2: Get the details to review - Risk,Financial Data, Collateral,Client outreach dtls..
		//Step 3: If successful, update the workflowReview table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowEdit with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage -  review.
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Initiate.ordinal() + 1);
				
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			List<WorkflowReview> lstworkflowReview = workflowReviewService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != lstworkflowReview && lstworkflowReview.size() > 0)
			{
				WorkflowReview workflowReview = lstworkflowReview.get(0);
				workflowReview.setComments("Review is completed");
				workflowReview.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowReview.setUpdatedBy(workflowReview.getAssignedTo());
				workflowReview.setUpdatedOn(updatedOn);
				
				workflowReviewService.saveWorkflowReview(workflowReview);
				
				WorkflowEdit workflowEdit = new WorkflowEdit();
				workflowEdit.setAssignedTo(workflowReview.getAssignedTo());
				workflowEdit.setContractId(contractid);
				workflowEdit.setComments("Edit is pending");
				updatedOn = new Timestamp(System.currentTimeMillis());
				workflowEdit.setCreatedOn(updatedOn);
				workflowEdit.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
				workflowEdit.setUpdatedOn(updatedOn);
				
				workflowEditService.saveWorkflowEdit(workflowEdit);
				
				
				con.setCurrStatusId(WorkflowStageEnums.Review.ordinal() + 1);
				con = contractService.saveContract(con);		
				
			}
			
		}
		return con;
	}
	
	
	
}
