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

import com.gblib.core.repapering.model.CounterParty;
import com.gblib.core.repapering.services.CounterPartyService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class CounterPartyController {

	@Autowired
	CounterPartyService counterPartyService;
	
	@RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
	public @ResponseBody CounterParty getCustomerDetails(@PathVariable int customerId) {
		
		return counterPartyService.findByCustomerId(customerId);
	}
}
