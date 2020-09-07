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
import com.gblib.core.repapering.model.ContractRisk;
import com.gblib.core.repapering.services.ContractRiskService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ContractRiskController {

	@Autowired
	ContractRiskService contractRiskService;
	
		
	@RequestMapping(value = "/find/contractrisk/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<ContractRisk> getContractRiskDetails(@PathVariable int contractId) {
		return contractRiskService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/save/contractrisk", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody ContractRisk saveContract(@RequestBody String strContractRisk) {
		
		ObjectMapper objmapper = new ObjectMapper();
		ContractRisk contractRisk = null;
		try {
			contractRisk = objmapper.readValue(strContractRisk, ContractRisk.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return contractRiskService.saveContractRisk(contractRisk);
	}
	
}
