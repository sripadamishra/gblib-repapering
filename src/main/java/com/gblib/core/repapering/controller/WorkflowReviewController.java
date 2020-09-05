/**
 * 
 */
package com.gblib.core.repapering.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.model.WorkflowReview;
import com.gblib.core.repapering.services.WorkflowReviewService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowReviewController {

	@Autowired
	WorkflowReviewService workflowReviewService;
	
		
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
}
