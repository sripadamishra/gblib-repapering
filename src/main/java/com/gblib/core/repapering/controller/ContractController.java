/**
 * 
 */
package com.gblib.core.repapering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
	
		
	@RequestMapping(value = "/contract/findby/{contractId}", method = RequestMethod.GET)
	public @ResponseBody Contract getContractDetails(@PathVariable String contractId) {
		return contractService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "/contract/updateby/{currStatusId}/{id}", method = RequestMethod.GET)
	public @ResponseBody int setNewCurrStatusIdForContract(@PathVariable int currStatusId, @PathVariable int id) {
		return contractService.setNewCurrStatusIdForContract(currStatusId,id);
	}
}
