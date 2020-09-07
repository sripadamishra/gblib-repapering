/**
 * 
 */
package com.gblib.core.repapering.controller;


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
import com.gblib.core.repapering.model.WorkflowOCR;
import com.gblib.core.repapering.services.WorkflowOCRService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class WorkflowOCRController {

	@Autowired
	WorkflowOCRService workflowOCRService;
	
		
	@RequestMapping(value = "/find/workflow/ocr/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowOCR> getWorkflowOCRDetails(@PathVariable int contractId) {		
		return workflowOCRService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/find/workflow/ocr/{contractId}/{statusId}", method = RequestMethod.GET)
	public @ResponseBody List<WorkflowOCR> getWorkflowOCRDetailsByStatusId(@PathVariable int contractId, @PathVariable int statusId) {		
		return workflowOCRService.findByContractIdAndStatusId(contractId,statusId);
	}
	
	@RequestMapping(value = "save/workflow/ocr", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowOCR saveWorkflowOCR(@RequestBody String strWorkflowOCR) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowOCR workflowOCR = null;
		try {
			workflowOCR = objmapper.readValue(strWorkflowOCR, WorkflowOCR.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowOCRService.saveWorkflowOCR(workflowOCR);
	}
}
