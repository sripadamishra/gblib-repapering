/**
 * 
 */
package com.gblib.core.repapering.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.model.RegulatoryEventDomainContext;
import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.services.RegulatoryEventDomainContextService;
import com.gblib.core.repapering.services.UserService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class RegulatoryEventDomainContextController {

	@Autowired
	RegulatoryEventDomainContextService regulatoryEventDomainContextService;
	
		
	@RequestMapping(value = "/find/domaincontextfields/{contracttype}", method = RequestMethod.GET)
	public @ResponseBody List<RegulatoryEventDomainContext> getDomainContextDetails(@PathVariable int contracttype) {		
		return regulatoryEventDomainContextService.findByContractType(contracttype);	
	}
}
