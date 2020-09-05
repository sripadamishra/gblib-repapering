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
import com.gblib.core.repapering.model.WorkflowClose;
import com.gblib.core.repapering.services.WorkflowCloseService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowCloseController {

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
}
