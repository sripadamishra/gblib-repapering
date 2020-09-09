/**
 * 
 */
package com.gblib.core.repapering.controller;


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
import com.gblib.core.repapering.model.WorkflowScanUpload;
import com.gblib.core.repapering.services.WorkflowScanUploadService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowScanUploadController {

	@Autowired
	WorkflowScanUploadService workflowScanUploadService;
	
		
	@RequestMapping(value = "/find/workflow/scanupload/{contractId}", method = RequestMethod.GET)
	public @ResponseBody WorkflowScanUpload getWorkflowScanUploadDetails(@PathVariable int contractId) {		
		return workflowScanUploadService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "save/workflow/scanupload", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowScanUpload saveWorkflowScanUpload(@RequestBody String strWorkflowScanUpload) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowScanUpload workflowScanUpload = null;
		try {
			workflowScanUpload = objmapper.readValue(strWorkflowScanUpload, WorkflowScanUpload.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowScanUploadService.saveWorkflowScanUpload(workflowScanUpload);
	}
}
