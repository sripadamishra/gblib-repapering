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
import com.gblib.core.repapering.model.WorkflowVerify;
import com.gblib.core.repapering.services.WorkflowVerifyService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowVerifyController {

	@Autowired
	WorkflowVerifyService workflowVerifyService;
	
		
	@RequestMapping(value = "/find/workflow/verify/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowVerify> getWorkflowVerifyDetails(@PathVariable int contractId) {		
		return workflowVerifyService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/verify/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowVerify> getWorkflowVerifyDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowVerifyService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/verify", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowVerify saveWorkflowVerify(@RequestBody String strWorkflowVerify) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowVerify workflowVerify = null;
		try {
			workflowVerify = objmapper.readValue(strWorkflowVerify, WorkflowVerify.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowVerifyService.saveWorkflowVerify(workflowVerify);
	}
}
