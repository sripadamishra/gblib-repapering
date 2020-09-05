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

import com.gblib.core.repapering.model.LoanFinancialDetails;
import com.gblib.core.repapering.services.LoanFinancialDetailsService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class LoanFinancialDetailsController {

	@Autowired
	LoanFinancialDetailsService loanFinancialDetailsService;
	
		
	@RequestMapping(value = "/find/loan/{contractId}", method = RequestMethod.GET)
	public @ResponseBody LoanFinancialDetails getLoanFinancialDetails(@PathVariable int contractId) {		
		return loanFinancialDetailsService.findByContractId(contractId);
	}
}
