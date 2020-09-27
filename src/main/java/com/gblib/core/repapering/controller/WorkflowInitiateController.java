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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.gblib.core.repapering.model.DocumentMetaDataExtended;
import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.model.DomainContractConfiguration;
import com.gblib.core.repapering.model.RegulatoryEventDomainContext;
import com.gblib.core.repapering.model.WorkflowInitiate;
import com.gblib.core.repapering.model.WorkflowReview;
import com.gblib.core.repapering.services.AmazonClient;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowInitiateController.class);
	
	@Autowired
	RegulatoryEventDomainContextService regulatoryEventDomainContextService;
	
	@Autowired
	DomainContractConfigurationService domainContractConfigurationService;
			
	@Autowired
	DocumentAnalyticsService documentAnalyticsService;
	
	@Autowired
	private AmazonClient amazonClient;
	
	@Value("${gblib.core.repapering.text.analytics.metadatadir}")
	private String analyticsDir;
	
	@Value("${gblib.core.repapering.text.analytics.docmetadata.filename}")
	private String docMetadataFilename;
		
	@Value("${gblib.core.repapering.file.storage}")
	private String locationDocstorage;
	
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
		String docFileName = "";
		
		if(null != con) {
			LOGGER.info("Contract with id= " + contractid + "is found");
			docFileName = con.getDocumentFileName();
			Date updatedOn = new Timestamp(System.currentTimeMillis());
			WorkflowInitiate workflowInitiate = workflowInitiateService.findByContractIdAndStatusId(contractid,WorkflowStageCompletionResultEnums.Pending.ordinal() + 1);//pending=1
			if(null != workflowInitiate) {
				workflowInitiate.setComments("Initiate is completed");
				workflowInitiate.setStatusId(WorkflowStageCompletionResultEnums.Completed.ordinal() + 1);
				workflowInitiate.setUpdatedBy(workflowInitiate.getAssignedTo());
				workflowInitiate.setUpdatedOn(updatedOn);
				workflowInitiateService.saveWorkflowInitiate(workflowInitiate);

				LOGGER.info("Workflow Initiate Saved.");
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
			
			LOGGER.info("Workflow Review Saved.");
			
			
			int contractType = 1; //default is loan
			
			//Try to call Python Script or Classification API here and get the contractType.Default it is 1.
			// Python Lambda will be called post OCR process, once TEXT converted PDF is pushed in S3. It will
			// classify the contract and finally update the Contract table.
			List<DomainContractConfiguration> domainContractConfigDtls = addDomainContextForContract(contractType, contractid);
			LOGGER.info("Domain context for the Contract Configuration is save for contract=." + contractid);
 
			//Create the documentMetaData list as per the Active DomainContext
			List<DocumentMetaData> activeDomainDtls =getContractDocMetaData(domainContractConfigDtls,contractType);
			
			//Now read the PDF and Populate
			documentAnalyticsService.analyseDatafromContractDoc(activeDomainDtls,contractid);
			
			DocumentMetaDataExtended extendedMetadata = documentAnalyticsService.getExtendedMetaData();
			int location = 1;//default file
			if(locationDocstorage.compareToIgnoreCase("awss3") == 0) {
				location = 2;
			}
			//Create extended metadata and write here.
			writetoJSONFile(contractid,docFileName,extendedMetadata,location);
			//Print JSON data.
			/*ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(System.out, extendedMetadata);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			//The above process will write to metadataS3 bucket. Then Lambda will be called and either new update json
			//file or Database will be loaded in response of the lambda trigger.
			// Then after this process...update the docprocessinginfo table as below.
			//This postAnalusis will be written to poll the DB if Lambda response is updated,
			//If so, get the data from DB as updated from Python Lambda side.
			//documentAnalyticsService.getExtendedMetaData_PostAnalysis();
			
			//documentAnalyticsService.updateDocProcessingInfo();
			
			
			//String docFileName = con.getDocumentFileName();
			System.out.println("docFilename=" + docFileName);
			//
			// Wait here until Lambda or Scheduler update the documentProcessingInfo table and 
			// write the extendedmeta updated details into the edit bucket request.  
			// Verify that if the contractId record is saved in the data base.
			//
			
			DocumentProcessingInfo docuData = documentProcessingInfoService.findByContractId(contractid);
			
			int second = 0;
			while(true) {
				if(null == docuData) {
					LOGGER.info("ContractId= " + contractid + " analysis from Python is not completed yet. Please wait before proceedig further.");
					try {
						TimeUnit.SECONDS.sleep(5);
						docuData = documentProcessingInfoService.findByContractId(contractid);
						if(docuData != null) break;
						
						if(second > 6) break;
						
						second++;
					} catch (InterruptedException e) {					
						e.printStackTrace();
					}
					
				}
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
	
	//
	private void writetoJSONFile(int contractId,String filename,DocumentMetaDataExtended extendedMetadata, int location) {

		//If location = 1 // it is AWS bucket - gblib-metadata-bucket
		//If location = 2 // It is Local disk - ./Analytics folder as mentioned in properties file.
		String outFilePath ="";
		String cloudDir = "",key="";
		String msg = "";
		
		filename = filename.substring(0, filename.indexOf(".pdf"));
		
		if(1 == location) {		
			if(docMetadataFilename.isEmpty()) {
				docMetadataFilename = ".json";
			}			
			outFilePath = analyticsDir + File.separator + filename + docMetadataFilename;
		}
		else {
			cloudDir = System.getProperty("java.io.tmpdir");
			outFilePath = cloudDir + File.separator + filename + docMetadataFilename;
			key = filename + docMetadataFilename;
		}
		msg = "Metadata will be written to location: " + outFilePath;
		LOGGER.info(msg);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(outFilePath), extendedMetadata);
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
	// Write to S3 from /temp (outFilePath) folder.
		if(2 == location) {			
			try {
				amazonClient.uploadMetadataFileToS3bucket(key,outFilePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//
	}
	//
}
