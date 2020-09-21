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
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.services.ContractService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class ContractController {

	@Autowired
	ContractService contractService;
	
	
	@RequestMapping(value = "get/contracts/all", method = RequestMethod.GET)
	public @ResponseBody List<Contract> getAllContracts() {
		return contractService.getAllContracts();
		
	}
	
	@RequestMapping(value = "find/contract", method = RequestMethod.POST)
	public @ResponseBody Contract getContractDetails(@RequestBody int contractId) {
		return contractService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "find/contract/id/{id}", method = RequestMethod.GET)
	public @ResponseBody Contract getContractDetailsById(@PathVariable int id) {
		return contractService.findById(id);
	}
	
	
	@RequestMapping(value = "save/contract/updateby/{currStatusId}/{id}", method = RequestMethod.GET)
	public @ResponseBody int setNewCurrStatusIdForContract(@PathVariable int currStatusId, @PathVariable int id) {
		return contractService.setNewCurrStatusIdForContract(currStatusId,id);
	}
	@RequestMapping(value = "save/contract", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody Contract saveContract(@RequestBody String strContract) {
		
		ObjectMapper objmapper = new ObjectMapper();
		Contract contract = null;
		try {
			contract = objmapper.readValue(strContract, Contract.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return contractService.saveContract(contract);
	}
}
