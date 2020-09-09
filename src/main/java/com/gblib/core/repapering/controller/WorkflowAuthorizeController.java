/**
 * 
 */
package com.gblib.core.repapering.controller;


import java.sql.Timestamp;
import java.util.Date;
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
import com.gblib.core.repapering.global.WorkflowStageCompletionResultEnums;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.WorkflowAuthLegal;
import com.gblib.core.repapering.model.WorkflowAuthProgram;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.WorkflowAuthLegalService;
import com.gblib.core.repapering.services.WorkflowAuthProgramService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowAuthorizeController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowAuthLegalService workflowAuthLegalService;
	
	@Autowired
	WorkflowAuthProgramService workflowAuthProgramService;
	
					
	@RequestMapping(value = "authorize/workflow", method = RequestMethod.POST)
	public @ResponseBody Contract authorizeWorkflow(@RequestBody int contractid) {
		//This is a dummy call - will do nothing to return the response..
		
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Edit.ordinal() + 1);								
		return con;
	}	
}
