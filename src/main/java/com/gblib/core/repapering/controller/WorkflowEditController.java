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
import com.gblib.core.repapering.model.WorkflowEdit;
import com.gblib.core.repapering.services.WorkflowEditService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowEditController {

	@Autowired
	WorkflowEditService workflowEditService;
	
		
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
}
