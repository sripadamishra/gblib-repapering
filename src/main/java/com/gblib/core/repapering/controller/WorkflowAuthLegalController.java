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
import com.gblib.core.repapering.model.WorkflowAuthLegal;
import com.gblib.core.repapering.services.WorkflowAuthLegalService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthLegalController {

	@Autowired
	WorkflowAuthLegalService workflowAuthLegalService;
	
		
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
}
