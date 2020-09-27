package com.gblib.core.repapering.scheduler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.DocumentMetaData;
import com.gblib.core.repapering.model.DocumentMetaDataExtended;
import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.model.PythonDataExtract;
import com.gblib.core.repapering.services.AmazonClient;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.DocumentProcessingInfoService;

@Service
@Configuration
@EnableScheduling
public class DataClassifyExtractionScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataClassifyExtractionScheduler.class);
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	AmazonClient amazonClient;
	
	@Autowired
	DocumentProcessingInfoService documentProcessingInfoService;
	
	@Value("${gblib.core.repapering.dataclassifyextractor.scheduler}")
	private String dataClassifyExtractorCronExpr;
	
	@Value("${gblib.core.repapering.file.storage}")
	private String locationDocstorage;
	
	private DocumentMetaDataExtended metadata = null;
	private List<Contract> OCRContracts = null;
	private DocumentProcessingInfo docInfo = new DocumentProcessingInfo();
	
	
	private int current_ContractId =0;
	private String current_fileName = "";
	private String current_predictions = "";
	private String current_extractedData = "";
	private String current_similarityData = "";
	private Contract current_Contract = null;
	boolean libor = false;
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void extractDataFromContract() {
		LOGGER.info("Data classifies and extractor scheduler is running every 5 sec.");
		getOCRContracts();
		downloadOCRContractsFromS3();				
	}
	
	private String convertTxtFromJsonMetadata(String jsonfile) {		
		ObjectMapper mapper = new ObjectMapper();
		
		String filename = jsonfile;
		String outputFilename = filename.replaceFirst(".json", ".txt");
		
		try {
			metadata = mapper.readValue(new File(filename),DocumentMetaDataExtended.class);
			List<String> listTextWholedoc = metadata.getListpageLevelOnlyRawText();
			String lines = "";
			BufferedWriter writer = null;
			if(metadata != null && listTextWholedoc != null) {
				listTextWholedoc = metadata.getListpageLevelOnlyRawText();
				writer = new BufferedWriter(new FileWriter(outputFilename));
				for(String eachpage:listTextWholedoc) {
					writer.write(eachpage);
					//Find LIBOR
					if(eachpage.indexOf("LIBOR") != -1) {
						libor = true;
						docInfo.setIsLIBOR('Y');
					}
					//
				}
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputFilename;					
		
			
	}
	
	private void getOCRContracts() {		
		OCRContracts = contractService.findByCurrStatusId(WorkflowStageEnums.OCR.ordinal()+1);
		
		LOGGER.info("Received list of OCR contracts; Count= "+ OCRContracts.size());
	}
	
	private void downloadOCRContractsFromS3() {
		String fileName = "",outFileName = "",txtFileName = "";
		byte[] content = null;
		
		for(Contract eachCon:OCRContracts) {
			fileName = eachCon.getDocumentFileName();
			current_fileName = fileName;
			String fileNameTmp =fileName.replaceFirst(".pdf",".json");
			try {
				
				content = amazonClient.downloadMetadataFileFromS3bucket(fileNameTmp);
				if(content != null) {
				LOGGER.info("Download OCR contract is sucessful. for key=" + fileNameTmp);
				outFileName = writeToTempFile(content,fileNameTmp);
				txtFileName = convertTxtFromJsonMetadata(outFileName);
				docInfo.setContractId(eachCon.getContractId());
				current_predictions = callPythonClassifyScript_Simulated(txtFileName);
				
				setContractType();
				
				current_extractedData = callPythonExtractScript_Simulated(txtFileName);
									
				saveDocumentProcessingInfo();
				
				current_similarityData = callPythonSimilarityScript_Simulated(outFileName);
				
				uploadToS3EditBucket();
				
				//Reset all
				current_ContractId = 0;
				current_fileName = "";
				current_predictions = "";
				current_extractedData = "";
				current_similarityData = "";
				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				if(content == null) {
					LOGGER.error("Download OCR contract is unsuccessful. for key=" + fileNameTmp);
					LOGGER.info("Next contract is searched for download.");
				}
			}
			
		}
	}
	
	private void setContractType() {
		docInfo.setPredictions(current_predictions);
	}
	
	private String writeToTempFile(byte[] content,String fileName) {
		BufferedWriter writer = null;
		String cloudDir = System.getProperty("java.io.tmpdir");
		String outputFilename = cloudDir + File.separator + fileName;
		Path path = Paths.get(outputFilename);
		try {
			Files.write(path, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Writing File to Temp location:" + outputFilename);
		return outputFilename;
	}
	
	private String callPythonClassifyScript_Simulated(String FileName) {
		String predictions = "syndicate"; //syndicate  by default
		if(FileName.contains("irswap_DEUTSCHE_BANK_LIBOR_TEXT_edit")) {
			predictions = "irswap";
		}
		LOGGER.info("callPythonClassifyScript_Simulated is completed");
		return predictions;
	}
	
	private String callPythonExtractScript_Simulated(String FileName) {
		String extractedData = "",loanData="",swapData="";
		
		PythonDataExtract swap = new PythonDataExtract();
		swap.setEntity1("Deutsche Bank");
		swap.setEntity2("MagnaChip Semiconductor S.A.");
		try {
			//SimpleDateFormat format = new SimpleDateFormat("Month D, Yr");
			//format.parse(source);
			swap.setStartdate(new SimpleDateFormat("MMMM d, yyyy").parse("June 27, 2019"));
			swap.setTerminationdate(new SimpleDateFormat("MMMM d, yyyy").parse("June 15, 2022"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
				
		swap.setCurrency("USD");
		swap.setAmount("300,000,000.00");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			swapData = mapper.writeValueAsString(swap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		PythonDataExtract loan = new PythonDataExtract();
		loan.setEntity1("GREAT WOLF LODGE OF THE CAROLINAS, LLC");
		loan.setEntity2("CREDIT AGRICOLE CORPORATE AND INVESTMENT BANK");
		try {
			loan.setStartdate(new SimpleDateFormat("MMMM d, yyyy").parse("July 15, 2019"));
			loan.setTerminationdate(new SimpleDateFormat("MMMM d, yyyy").parse("July 15, 2022"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		loan.setCurrency("USD");
		loan.setAmount("56000000.00");
		
		//
		try {
			if(FileName.contains("irswap_DEUTSCHE BANK_LIBOR_TEXT_edit")) {
				swapData = mapper.writeValueAsString(swap);
				extractedData=swapData;
			}
			else {
				loanData = mapper.writeValueAsString(loan);
				extractedData=loanData;
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//				
		LOGGER.info("callPythonClassifyScript_Simulated is completed");
		return extractedData;
	}
	
	private String callPythonSimilarityScript_Simulated(String FileName) {
		List<String> listTextWholedoc = metadata.getListpageLevelOnlyRawText();
		List<DocumentMetaData> nontextdata = metadata.getListdocumentMetaData();
		for(DocumentMetaData doc:nontextdata) {
			String dictionayId = doc.getDomainContextDictionaryId();
			//The algorithm will work like as below -
			//1. Find the text similarity between 
			//   the doc.getHeaderTextContent() vs
			//   doc.getDomainContextPossibleValueDefinitions()
			//   If similarity > 70%, there is match in context, update is required in that page.
			
			if(dictionayId.compareToIgnoreCase("Fallback_Benchmark_Unavailable") == 0)			   
			{
				doc.setDictionaryIdupdateRequired(true);
			}			
									
		}
		LOGGER.info("callPythonSimilarityScript_Simulated is completed");
		return "";
	}
	
	private void saveDocumentProcessingInfo() {
				
		PythonDataExtract pde = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			pde = mapper.readValue(current_extractedData, PythonDataExtract.class);
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
		if(pde != null) {
			docInfo.setLegalEntityName(pde.getEntity1());
			docInfo.setCounterPartyName(pde.getEntity2());
			docInfo.setStartDate(pde.getStartdate());
			docInfo.setTerminationDate(pde.getTerminationdate());
			docInfo.setCurrency(pde.getCurrency());
			docInfo.setAmount(pde.getAmount());
		}
		else {
			//default value
			
		}
		
		documentProcessingInfoService.saveDocumentProcessingInfo(docInfo);
		LOGGER.info("Classification Type and Extracted data save is completed.");
	}
	
	private void uploadToS3EditBucket() {		
		//Write the update metadata into he edit bucket.
		String cloudDir = "",outFilePath = "", key = "", msg = "";
		boolean cloud = true;
		String filename = current_fileName;
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
		msg = "After similarity analysis,updated context Metadata will be written to location: " + outFilePath;
		LOGGER.info(msg);
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
		LOGGER.info("uploadToS3EditBucket is completed from Scheduler.");
	}
}
