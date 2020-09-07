/**
 * 
 */
package com.gblib.core.repapering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.model.DerivativeFinancialDetails;
import com.gblib.core.repapering.services.DerivativeFinancialDetailsService;

/**
 * @author SRIPADA MISHRA
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DerivativeFinancialDetailsController {

	@Autowired
	DerivativeFinancialDetailsService derivativeFinancialDetailsService;
	
		
	@RequestMapping(value = "/find/derivative/{contractId}", method = RequestMethod.GET)
	public @ResponseBody DerivativeFinancialDetails getDerivativeFinancialDetails(@PathVariable int contractId) {		
		return derivativeFinancialDetailsService.findByContractId(contractId);
	}
}
