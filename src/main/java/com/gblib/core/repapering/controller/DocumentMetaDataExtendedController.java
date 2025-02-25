/**
 * 
 */
package com.gblib.core.repapering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.model.DocumentMetaDataExtended;
import com.gblib.core.repapering.model.User;
import com.gblib.core.repapering.services.DocumentMetaDataExtendedService;
import com.gblib.core.repapering.services.UserService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class DocumentMetaDataExtendedController {

	@Autowired
	DocumentMetaDataExtendedService documentMetaDataExtendedService;
	
		
	@RequestMapping(value = "/get/contractmetadata/{contractid}", method = RequestMethod.GET)
	public @ResponseBody DocumentMetaDataExtended getContractMetaDataDetails(@PathVariable int contractid) {		
		return documentMetaDataExtendedService.getDocumentMataData(contractid);
	}		
}
