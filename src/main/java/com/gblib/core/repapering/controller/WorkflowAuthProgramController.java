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
import com.gblib.core.repapering.model.WorkflowAuthProgram;
import com.gblib.core.repapering.services.WorkflowAuthProgramService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthProgramController {

	@Autowired
	WorkflowAuthProgramService workflowAuthProgramService;
	
		
	@RequestMapping(value = "/find/workflow/authprogram/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthProgram> getWorkflowAuthProgramDetails(@PathVariable int contractId) {		
		return workflowAuthProgramService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/authprogram/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowAuthProgram> getWorkflowAuthProgramDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowAuthProgramService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/authprogram", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowAuthProgram saveWorkflowAuthProgram(@RequestBody String strWorkflowAuthProgram) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowAuthProgram workflowAuthProgram = null;
		try {
			workflowAuthProgram = objmapper.readValue(strWorkflowAuthProgram, WorkflowAuthProgram.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowAuthProgramService.saveWorkflowAuthProgram(workflowAuthProgram);
	}
}
