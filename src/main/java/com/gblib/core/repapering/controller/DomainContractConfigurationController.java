/**
 * 
 */
package com.gblib.core.repapering.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.model.DomainContractConfiguration;
import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.services.DomainContractConfigurationService;
import com.gblib.core.repapering.services.UserService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class DomainContractConfigurationController {

	@Autowired
	DomainContractConfigurationService domainContractConfigurationService;
	
		
	@RequestMapping(value = "/find/domainccontractfields", method = RequestMethod.GET)	
	public @ResponseBody List<DomainContractConfiguration> findByContractIdAndRegulatoryEventId(@RequestBody int contractid) {
		int eventid = 1;
		return domainContractConfigurationService.findByContractIdAndRegulatoryEventId(contractid,eventid);
	}
	
	
	@RequestMapping(value = "/save/domainccontractfields", method = RequestMethod.POST)
	public @ResponseBody List<DomainContractConfiguration> save(@RequestBody List<DomainContractConfiguration> configDtls) {
		return domainContractConfigurationService.save(configDtls);
	}
}
