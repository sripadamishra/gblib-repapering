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
import com.gblib.core.repapering.model.WorkflowAuthTreasury;
import com.gblib.core.repapering.services.WorkflowAuthTreasuryService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthTreasuryController {

	@Autowired
	WorkflowAuthTreasuryService workflowAuthTreasuryService;
	
		
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
}
