/**
 * 
 */
package com.gblib.core.repapering.controller;

import java.util.Arrays;
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
import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.services.DocumentProcessingInfoService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class DocumentProcessingInfoController {

	@Autowired
	DocumentProcessingInfoService documentProcessingInfoService;
	
		
	@RequestMapping(value = "find/docinfo/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<DocumentProcessingInfo> getDocumentProcessingInfoDetails(@PathVariable int contractId) {
		return documentProcessingInfoService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "save/docinfo", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody List<DocumentProcessingInfo> saveDocumentProcessingInfo(@RequestBody String strDocInfo) {
		
		ObjectMapper objmapper = new ObjectMapper();
		List<DocumentProcessingInfo> lstDocInfo = null;
		try {
			lstDocInfo = Arrays.asList(objmapper.readValue(strDocInfo, DocumentProcessingInfo[].class));
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return documentProcessingInfoService.saveDocumentProcessingInfo(lstDocInfo);
	}
	
	
	
}
