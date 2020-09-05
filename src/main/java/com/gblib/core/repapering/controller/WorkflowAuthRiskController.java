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
import com.gblib.core.repapering.model.WorkflowAuthRisk;
import com.gblib.core.repapering.services.WorkflowAuthRiskService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthRiskController {

	@Autowired
	WorkflowAuthRiskService workflowAuthRiskService;
	
		
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
}
