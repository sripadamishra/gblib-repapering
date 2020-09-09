/**
 * 
 */
package com.gblib.core.repapering.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.model.LiborDocument;
import com.gblib.core.repapering.services.DocumentProcessingInfoService;
import com.gblib.core.repapering.services.FileParsingService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class DocumentProcessingInfoController {

	@Autowired
	DocumentProcessingInfoService documentProcessingInfoService;
	
	@Autowired
	FileParsingService fileParsingService;
		
	@RequestMapping(value = "find/docinfo/counterparty", method = RequestMethod.POST)
	public @ResponseBody List<DocumentProcessingInfo> getDocumentProcessingInfoDetails(@RequestBody String counterpartyname) {
		return documentProcessingInfoService.findByCounterPartyName(counterpartyname);
	}
	
	@RequestMapping(value = "find/docinfo/docfilename/{docfilename}", method = RequestMethod.POST)
	public @ResponseBody List<DocumentProcessingInfo> getDocumentProcessingInfoDetailsByFileName(@PathVariable String docfilename) {
		System.out.println(docfilename);
		return documentProcessingInfoService.findByDocFileName(docfilename);
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
	
	@RequestMapping(value = "load/analyticsdata", method = RequestMethod.POST)
	public @ResponseBody List<DocumentProcessingInfo> loadAnalyticsData() {
		
		List<LiborDocument> doc = fileParsingService.parseCsvFile();
		List<DocumentProcessingInfo> lstDocInfo = new ArrayList<DocumentProcessingInfo>();
		if(null != doc && doc.size() > 0) {
			DocumentProcessingInfo eachdocinfo = null;
			for (LiborDocument eachrow : doc) {
				eachdocinfo = new DocumentProcessingInfo();
				eachdocinfo.setAmount(eachrow.getAmount());
				eachdocinfo.setCounterPartyName(eachrow.getEntity2());
				eachdocinfo.setCurrency(eachrow.getCurrency());
				eachdocinfo.setDocFileName(eachrow.getFile());
				eachdocinfo.setDocPosition(eachrow.getLIBOR_docposition());
				eachdocinfo.setFallbackPage(eachrow.getFallbackPage());
				eachdocinfo.setFallbackPosition(eachrow.getFallbackPosition());
				if(null != eachrow.getFallbackPresent() && !eachrow.getFallbackPresent().isEmpty()) {
					eachdocinfo.setFallbackPresent(eachrow.getFallbackPresent().charAt(0));
				}
				eachdocinfo.setFallbackText(eachrow.getFallbackText());
				eachdocinfo.setFallbackTextComplexity(eachrow.getFallbackTextComplexity());
				
				if(eachrow.getLIBOR_docposition() > 0 && eachrow.getLIBOR_startpage() >0) {
					eachdocinfo.setIsLIBOR('Y');
				}
				else
				{
					eachdocinfo.setIsLIBOR('N');
				}
				eachdocinfo.setLegalEntityName(eachrow.getEntity1());
				eachdocinfo.setPredictions(eachrow.getPredictions());
				try {
					eachdocinfo.setStartDate(new SimpleDateFormat("YYYY-MM-dd").parse(eachrow.getStartdate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				eachdocinfo.setStartpage(eachrow.getLIBOR_startpage());
				eachdocinfo.setStartPagePosition(eachrow.getLIBOR_startpageposition());
				eachdocinfo.setStartSnippet(eachrow.getLIBOR_startsnippet());
				try {
					eachdocinfo.setTerminationDate(new SimpleDateFormat("YYYY-MM-dd").parse(eachrow.getTerminationdate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lstDocInfo.add(eachdocinfo);
			}			
		}
		return documentProcessingInfoService.saveDocumentProcessingInfo(lstDocInfo);
	}
}
