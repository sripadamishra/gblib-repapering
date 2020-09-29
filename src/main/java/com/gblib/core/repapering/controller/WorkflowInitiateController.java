/**
 * 
 */
package com.gblib.core.repapering.controller;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
			List<DomainContractConfiguration> domainContractConfigDtls = addDomainContextForContract(contractType, contractid);
			LOGGER.info("DomainContractConfiguration for contract=." + contractid);

			//Create the documentMetaData list as per the Active DomainContext
			List<DocumentMetaData> activeDomainDtls = getContractDocMetaData(domainContractConfigDtls,contractType);
			LOGGER.info("Getting the active domain context is completed.");
			
			//Now read the PDF and Populate
			LOGGER.info("Reading the PDF doc is started to get metadata.");
			documentAnalyticsService.analyseDatafromContractDoc(activeDomainDtls,contractid);
			LOGGER.info("Reading the PDF doc is completed to get metadata.");
			
			DocumentMetaDataExtended extendedMetadata = documentAnalyticsService.getExtendedMetaData();
			int location = 1;//default file
			if(locationDocstorage.compareToIgnoreCase("awss3") == 0) {
				location = 2;
			}
			//Create extended metadata and write here.
			LOGGER.info("writetoJSONFile is started.");
			writetoJSONFile(contractid,docFileName,extendedMetadata,location);														
			LOGGER.info("writetoJSONFile is completed.");
			
			LOGGER.info("Waiting for Python Anlytics to update the DocumentProcessingInfo table.");
			//TO DO HERE:Start
			// Step:1-Create a Loan data and swap data Document Processing Info instance and load it for each 
			// type of contract statically.
			// Step:2- Update the meta data information and add to edit bucket/folder.
			//Step:3- Check the time taken.
			//Step:4- See if we can show update for LIBOR with SOFR, or multiple updates to a doc.
			//Step:5- See if Swap doc can be updated as well.
			//TO DO HERE:End
			DocumentProcessingInfo loanDoc = getLoanData(contractid,docFileName); 
			DocumentProcessingInfo derivativeDoc = getDerivativeData(contractid,docFileName);
			DocumentProcessingInfo docuData = null;
			if(docFileName.contains("CREDIT AGRICOLE_LIBOR_small")) {
				docuData = loanDoc;
			}
			if(docFileName.contains("DEUTSCHE BANK_LIBOR_TEXT_edit")) {
				docuData = derivativeDoc;
			}
			else {
				docuData = loanDoc;
			}
			//Add this info to table.
			try {
				documentProcessingInfoService.saveDocumentProcessingInfo(docuData);
			}
			catch(Exception e) {
				LOGGER.error("Save to DocumentProcessingInfo failed.");
			}
			// Update the object - extendedMetadata
			updateMetadata(docuData,extendedMetadata);
			uploadToS3EditBucket(docFileName,extendedMetadata);
			// Add the json into edit bucket / edit folder for local
			
			LOGGER.info("copyFromDocProssingInfoToContract is started.");
			copyFromDocProssingInfoToContract(con,docuData);
			LOGGER.info("copyFromDocProssingInfoToContract is completed.");
			con = contractService.saveContract(con);
			LOGGER.info("Contract table is saved for Initialize Controller.");
			
		}	
		return con;
	}
	
	private void copyFromDocProssingInfoToContract(Contract con, DocumentProcessingInfo docuData) {
		
		if(docuData != null) {
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
			con.setCurrStatusId(WorkflowStageEnums.Initiate.ordinal() + 1);					
	}
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
	
	
	private void writetoJSONFile(int contractId,String filename,DocumentMetaDataExtended extendedMetadata, int location) {
		
		String outFilePath ="";
		String cloudDir = "",key="";
		String msg = "";
		
		filename = filename.substring(0, filename.indexOf(".pdf"));
		
		if(1 == location) {		
			if(docMetadataFilename.isEmpty()) {
				docMetadataFilename = ".json";
			}			
			outFilePath = ".\\metadata" + File.separator + filename + docMetadataFilename;
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

	}
	
	private DocumentProcessingInfo getLoanData(int contractId, String docFileName) {
		DocumentProcessingInfo loan = new DocumentProcessingInfo();
		try {
			loan.setAmount("56000000.00");
			loan.setContractId(contractId);
			loan.setCounterPartyName("GREAT WOLF LODGE OF THE CAROLINAS, LLC");
			loan.setLegalEntityName("CREDIT AGRICOLE CORPORATE AND INVESTMENT BANK");
			loan.setCurrency("USD");
			loan.setDocFileName(docFileName);
			loan.setFallbackPresent('Y');
			loan.setIsLIBOR('Y');
			loan.setPredictions("syndicate");		
			loan.setStartDate(new SimpleDateFormat("MMMM d, yyyy").parse("July 15, 2019"));
			loan.setTerminationDate(new SimpleDateFormat("MMMM d, yyyy").parse("July 15, 2022"));
		}
		catch(Exception e) {
			LOGGER.error("Loan Creation Failed.");
		}
		return loan;
	}
	
	private DocumentProcessingInfo getDerivativeData(int contractId, String docFileName) {
		DocumentProcessingInfo derivative = new DocumentProcessingInfo();
		
		try {
			derivative.setAmount("");
			derivative.setContractId(contractId);
			derivative.setCounterPartyName("MagnaChip Semiconductor S.A.");
			derivative.setLegalEntityName("Deutsche Bank");
			derivative.setCurrency("USD");
			derivative.setDocFileName(docFileName);
			derivative.setFallbackPresent('Y');
			derivative.setIsLIBOR('Y');
			derivative.setPredictions("irswap");
			derivative.setStartDate(new SimpleDateFormat("MMMM d, yyyy").parse("June 27, 2019"));
			derivative.setTerminationDate(new SimpleDateFormat("MMMM d, yyyy").parse("June 15, 2022"));
		}
		catch(Exception e) {
			LOGGER.error("Derivative Creation Failed.");
		}
		return derivative;
	}
	
	private void updateMetadata(DocumentProcessingInfo docuData,DocumentMetaDataExtended extendedMetadata) {
		List<DocumentMetaData> metadata = extendedMetadata.getListdocumentMetaData();
		if(metadata != null) {
			for(DocumentMetaData eachdoc:metadata) {
				if(eachdoc.getDomainContextDictionaryId().compareToIgnoreCase("Fallback_Benchmark_Unavailable")==0) {
					eachdoc.setTextSimilarity(80);
					eachdoc.setDictionaryIdupdateRequired(true);
				}
				else if(eachdoc.getDomainContextDictionaryId().compareToIgnoreCase("Agreement_Date")==0) {
					eachdoc.setDomaincontextCurrentFieldValue(docuData.getStartDate().toString());					
				}
				else if(eachdoc.getDomainContextDictionaryId().compareToIgnoreCase("Borrower")==0) {
					eachdoc.setDomaincontextCurrentFieldValue(docuData.getCounterPartyName());
				}
				else if(eachdoc.getDomainContextDictionaryId().compareToIgnoreCase("Lender")==0) {
					eachdoc.setDomaincontextCurrentFieldValue(docuData.getLegalEntityName());
				}
				else if(eachdoc.getDomainContextDictionaryId().compareToIgnoreCase("Loan_Amount")==0) {
					eachdoc.setDomaincontextCurrentFieldValue(docuData.getAmount());
				}
				else if(eachdoc.getDomainContextDictionaryId().compareToIgnoreCase("Initial_Maturity_Date")==0) {
					eachdoc.setDomaincontextCurrentFieldValue(docuData.getTerminationDate().toString());
				}				
				
			}
			
		}		
	}
 
	private void uploadToS3EditBucket(String docFileName, DocumentMetaDataExtended metadata) {		
		//Write the update metadata into he edit bucket.
		String cloudDir = "",outFilePath = "", key = "", msg = "";
		boolean cloud = false;
		String filename = docFileName;
		filename = filename.substring(0, filename.indexOf(".pdf"));
		if(locationDocstorage.compareToIgnoreCase("awss3") == 0) {
			cloud = true;
		}
		if(cloud) {
			cloudDir = System.getProperty("java.io.tmpdir");
			outFilePath = cloudDir + File.separator + filename + ".json";
			key = filename + ".json";
		}
		else {
			outFilePath = ".\\edit" + File.separator + filename + ".json";
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(outFilePath), metadata);
		} catch (JsonGenerationException e) {		
			e.printStackTrace();
		} catch (JsonMappingException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		if(cloud) {
			try {
				amazonClient.uploadAnalysiedMetadataFileToS3bucket(key,outFilePath);
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		LOGGER.info("uploadToEdit is completed from Initialization.");
	}

	
	
}

