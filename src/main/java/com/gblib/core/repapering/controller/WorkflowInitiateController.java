/**
 * 
 */
package com.gblib.core.repapering.controller;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.global.WorkflowStageCompletionResultEnums;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.CounterParty;
import com.gblib.core.repapering.model.DocumentMetaData;
import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.model.DomainContractConfiguration;
import com.gblib.core.repapering.model.RegulatoryEventDomainContext;
import com.gblib.core.repapering.model.WorkflowInitiate;
import com.gblib.core.repapering.model.WorkflowReview;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.CounterPartyService;
import com.gblib.core.repapering.services.DocumentAnalyticsService;
import com.gblib.core.repapering.services.DocumentProcessingInfoService;
import com.gblib.core.repapering.services.DomainContractConfigurationService;
import com.gblib.core.repapering.services.RegulatoryEventDomainContextService;
import com.gblib.core.repapering.services.WorkflowInitiateService;
import com.gblib.core.repapering.services.WorkflowReviewService;

/**
 * @author SRIPADA MISHRA
 *
 */
@RestController
public class WorkflowInitiateController {

	
	@Autowired
	ContractService contractService;
	
	@Autowired
	WorkflowInitiateService workflowInitiateService;
	
	@Autowired
	WorkflowReviewService workflowReviewService;
	
	@Autowired
	DocumentProcessingInfoService documentProcessingInfoService;

	@Autowired
	CounterPartyService counterPartyService;
	
	@Autowired
	RegulatoryEventDomainContextService regulatoryEventDomainContextService;
	
	@Autowired
	DomainContractConfigurationService domainContractConfigurationService;
	
	@Autowired
	DocumentAnalyticsService documentAnalyticsService;
	
	@RequestMapping(value = "/find/workflow/initiate/{contractId}", method = RequestMethod.GET)
	public @ResponseBody WorkflowInitiate getWorkflowInitiateDetails(@PathVariable int contractId) {		
		return workflowInitiateService.findByContractId(contractId);
	}
	
	@RequestMapping(value = "save/workflow/initiate", consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
	public @ResponseBody WorkflowInitiate saveWorkflowInitiate(@RequestBody String strWorkflowInitiate) {
		
		ObjectMapper objmapper = new ObjectMapper();
		WorkflowInitiate workflowInitiate = null;
		try {
			workflowInitiate = objmapper.readValue(strWorkflowInitiate, WorkflowInitiate.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		
		return workflowInitiateService.saveWorkflowInitiate(workflowInitiate);
	}
	
	@RequestMapping(value = "initiate/workflow", method = RequestMethod.POST)
	public @ResponseBody Contract initiateWorkflow(@RequestBody int contractid) {
		//Step 1: Find the contract whose OCR is completed from contract Details.
		//Step 2: Perform the Initiate Operation. (Python Analytics,Read Excel and update DB.)
		//Step 3: If successful, update the workflowInitiate table with updatedBy and updatedOn and statusId.
		//Step 4: Also, update workflowReview with Pending Status.
		//Step 5: Also update the contractDetails table with statusId with stage - Initiate.
		Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.OCR.ordinal()+1);
		System.out.println("Contract found");
		if(null != con) {
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			WorkflowInitiate workflowInitiate = workflowInitiateService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != workflowInitiate) {
				workflowInitiate.setComments("Initiate is completed");
				workflowInitiate.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowInitiate.setUpdatedBy(workflowInitiate.getAssignedTo());
				workflowInitiate.setUpdatedOn(updatedOn);
				workflowInitiateService.saveWorkflowInitiate(workflowInitiate);

				System.out.println("Workflow Initiate Saved.");
			}
			
			
			WorkflowReview workflowReview = new WorkflowReview();
			if(null != workflowInitiate)
				workflowReview.setAssignedTo(workflowInitiate.getAssignedTo());
			workflowReview.setContractId(contractid);
			workflowReview.setComments("Review is pending");
			updatedOn = new Timestamp(System.currentTimeMillis());
			workflowReview.setCreatedOn(updatedOn);
			workflowReview.setStatusId(WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);
			workflowReview.setUpdatedOn(updatedOn);
			
			workflowReviewService.saveWorkflowReview(workflowReview);
			
			System.out.println("Workflow Review Saved.");
			//
			//OCR is completed in this step. So fetch the analytics data based upon the OCR file name and update the Contract table.
			//Before initiating the Analytics process - 
			//Save the domaincontext data for this contract..
			int contractType = 1; //default is loan
			//Try to call Python Script or Classification API here and get the contractType.Default it is 1.
			List<DomainContractConfiguration> domainContractConfigDtls = addDomainContextForContract(contractType, contractid);
			System.out.println("Domain context for the Contract Configuration is save for contract=." + contractid);
			//Now read the PDF and Populate 
			//Create the documentMetaData list as per the Active DomainContext
			List<DocumentMetaData> activeDomainDtls =getContractDocMetaData(domainContractConfigDtls,contractType);
			 
			documentAnalyticsService.analyseDatafromContractDoc(activeDomainDtls,contractid);
			
			//Print JSON data.
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(System.out, activeDomainDtls);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			String docFileName = con.getDocumentFileName();
			System.out.println("docFilename=" + docFileName);
			
			List<DocumentProcessingInfo> lstDocuData = documentProcessingInfoService.findByDocFileName(docFileName);
			 
			System.out.println("Find doc File Name=" + lstDocuData.toString());
			
			DocumentProcessingInfo docuData = null;
			if(null != lstDocuData && lstDocuData.size() > 0) {
				docuData = lstDocuData.get(0);
			}
			con.setContractStartDate(docuData.getStartDate());
			con.setContractExpiryDate(docuData.getTerminationDate());
			con.setCounterPartyName(docuData.getCounterPartyName());
			//
			int counterPartyId = 0;
			
			CounterParty counterParty = counterPartyService.findByCounterPartyName(docuData.getCounterPartyName());
			if(null != counterParty) {
				counterPartyId = counterParty.getCounterPartyId();
				con.setCounterPartyId(counterPartyId);
			}						
			//
			con.setLegalEntityName(docuData.getLegalEntityName());
			con.setCounterPartyName(docuData.getCounterPartyName());
			
			if(docuData.getIsLIBOR() == 'Y') {
				con.setLIBOR(true);
			}
			else {
				con.setLIBOR(false);
			}
			
			String pred = docuData.getPredictions().toUpperCase();
			
			switch(pred) {
			case "MORTGAGE":
				con.setContractTypeId(1);
				con.setContractSubTypeId(1);
			break;
			case "SYNDICATE":
				con.setContractTypeId(1);
				con.setContractSubTypeId(2);
				break;
			case "TERM":
				con.setContractTypeId(1);
				con.setContractSubTypeId(7);
				break;
			case "NORMAL":
				con.setContractTypeId(1);
				con.setContractSubTypeId(7);
				break;
			case "IRSWAP":
				con.setContractTypeId(2);
				con.setContractSubTypeId(6);				
				break;
			case "CURSWAP":
				con.setContractTypeId(2);
				con.setContractSubTypeId(5);
				break;
			case "ISDA":
				con.setContractTypeId(2);
				con.setContractSubTypeId(6); // Should be updated.
				break;
				default:
				con.setContractTypeId(1);
				con.setContractSubTypeId(1);
				break;
			}
					
			//
			con.setCurrStatusId(WorkflowStageEnums.Initiate.ordinal() + 1);
			contractService.saveContract(con);					
		}
		return con;
	}
	
	private List<DomainContractConfiguration> addDomainContextForContract(int contractType, int contractId) {

		List<RegulatoryEventDomainContext> domainDtls = regulatoryEventDomainContextService.findByContractType(contractType);
		List<DomainContractConfiguration> contractConfigDtls = null;

		if(null != domainDtls) {

			contractConfigDtls = new ArrayList<DomainContractConfiguration>();

			for(RegulatoryEventDomainContext eachdomainContext:domainDtls) {

				if(null != eachdomainContext) {
					DomainContractConfiguration config = new DomainContractConfiguration();
					config.setContractId(contractId);
					config.setRegulatoryEventId(1);
					config.setDomainContextDictionaryId(eachdomainContext.getDomainContextDictionaryId());
					config.setContextConfigurationActive(true);					
					contractConfigDtls.add(config);
				}
			}

			contractConfigDtls = domainContractConfigurationService.save(contractConfigDtls);
		}		
		return contractConfigDtls;		
	}
	
	private List<DocumentMetaData> getContractDocMetaData(List<DomainContractConfiguration> domainContractConfigDtls,int contractType){
		List<DocumentMetaData> contractDocMetaData = new ArrayList<DocumentMetaData>();
		//Get all domain context
		List<RegulatoryEventDomainContext> domainDtls = regulatoryEventDomainContextService.findByContractType(contractType);
		DocumentMetaData docmetadata = null;
		for(DomainContractConfiguration config:domainContractConfigDtls) {
			
			if(null != config && config.isContextConfigurationActive()) {
				docmetadata = new DocumentMetaData();				
				RegulatoryEventDomainContext domainCtxDtl = fetchDomainContextDetails(config.getDomainContextDictionaryId(),domainDtls);
				docmetadata = mapToMetaData(docmetadata,domainCtxDtl);				
				contractDocMetaData.add(docmetadata);
			}			
		}
		return contractDocMetaData;
	}
	
	private RegulatoryEventDomainContext fetchDomainContextDetails(String domainContextDictionaryId,List<RegulatoryEventDomainContext> domainDtls){
		RegulatoryEventDomainContext domainCtxDtlFound = null;
		for(RegulatoryEventDomainContext domainCtx:domainDtls) {
			if(null != domainCtx && domainCtx.getDomainContextDictionaryId().compareToIgnoreCase(domainContextDictionaryId)==0) {
				domainCtxDtlFound = domainCtx;
				break;
			}
		}
		return domainCtxDtlFound;
	}
	
	private DocumentMetaData mapToMetaData(DocumentMetaData docmetadata, RegulatoryEventDomainContext domainCtxDtl) {
		if( null != docmetadata && null != domainCtxDtl) {
			docmetadata.setContractType(domainCtxDtl.getContractType());
			docmetadata.setDomainContextDictionaryId(domainCtxDtl.getDomainContextDictionaryId());
			docmetadata.setDomainContextName(domainCtxDtl.getDomainContextName());
			docmetadata.setDomainContextPossibleNameDefinitions(domainCtxDtl.getDomainContextPossibleNameDefinitions());
			docmetadata.setDomainContextPossibleValueDefinitions(domainCtxDtl.getDomainContextPossibleValueDefinitions());
			docmetadata.setDomainContextSubTypeId(domainCtxDtl.getDomainContextSubTypeId());
			docmetadata.setDomainContextTypeId(domainCtxDtl.getDomainContextTypeId());
			docmetadata.setEntityRule(domainCtxDtl.getEntityRule());
			docmetadata.setPhraseRule(domainCtxDtl.getPhraseRule());
			docmetadata.setContractType(domainCtxDtl.getContractType());
		}
		return docmetadata;
	}
	
}
