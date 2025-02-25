package com.gblib.core.repapering.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.workdocs.model.DocumentMetadata;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gblib.core.repapering.controller.WorkflowInitiateController;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.DocumentMetaData;
import com.gblib.core.repapering.model.DocumentMetaDataExtended;
import com.gblib.core.repapering.model.DocumentProcessingInfo;
import com.gblib.core.repapering.services.business.UnderlinedBoldedLocationTextExtractionStrategy;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextMarginFinder;


@Service
public class DocumentAnalyticsService {

	@Value("${gblib.core.repapering.ocr.outputdir}")
	private String outputFileDir;
	
	@Value("${gblib.core.repapering.file.storage}")	
	private String storagelocation;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAnalyticsService.class);
	
	@Autowired ContractService contractService;
	
	@Autowired
	DocumentProcessingInfoService documentProcessingInfoService; 
	
	@Autowired
	AmazonClient amazonClient;
	
	//Important Collection variable required for document analysis/searching/update etc.
	private List<DocumentMetaData> listwholeDocScanHeaders = null;
	private HashMap<String,DocumentMetaData> mapwholeDocScanHeaders = null;
	private List<DocumentMetaData> listOnlyConfiguredContextHeaders = null;
	PdfReader pdfReader = null;
	private List<DocumentProcessingInfo> listdocProcessingInfo = null;
	private String docFileName = null;
	DocumentMetaDataExtended extendedMetadata = null;
	private List<String> listpageLevelOnlyRawText = null;
	//
	private void getUnderlinedText(int pageno) {
		PdfDictionary page = pdfReader.getPageN(pageno);
	    
		PRIndirectReference objectReference = (PRIndirectReference) page.get(PdfName.CONTENTS);
	    PRStream stream = (PRStream) PdfReader.getPdfObject(objectReference);	    				
	}
	//
	private void getParagraphHeadingData() {
		//List<DocumentMetaData> docMetadata = new ArrayList<DocumentMetaData>();
		mapwholeDocScanHeaders = new HashMap<String,DocumentMetaData>();
		listwholeDocScanHeaders = new ArrayList<DocumentMetaData>();
			try {
				LOGGER.info("Read whole doc to gather the Bold and Underlined Heading:Started");
				UnderlinedBoldedLocationTextExtractionStrategy strategy = new UnderlinedBoldedLocationTextExtractionStrategy();				
				for(int i=1;i<=pdfReader.getNumberOfPages();i++) {
					PdfTextExtractor.getTextFromPage(pdfReader, i,strategy);					
					//String chunkinfo = strategy.getResultantText();
					
					List<DocumentMetaData> pageData = strategy.getDocMetaData();
					
					int index =0;
					for(DocumentMetaData data:pageData) {
						data.setHeaderPageNo(i);
						data.setHeaderParagraphIndex(index);
						index++;				
						//Below entry is added in Map.
						//System.out.println("Header =" + data.getHeaderName());
						String key = stripTrailingChars(stripTrailingSpaces(data.getHeaderName()));						
						//System.out.println("key =" + key);
						mapwholeDocScanHeaders.put(key, data);
						listwholeDocScanHeaders.add(data);
					}
					strategy.clean();//Reset all member variables after each page read.
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			LOGGER.info("Read whole doc to gather the Bold and Underlined Heading:Completed");		
	}
	
	private String stripTrailingSpaces(String input) {
		return input.stripLeading().stripTrailing();
	}
	
	private String stripTrailingChars(String input) {
		String subStr = "";
		if(input.length() > 1) {
			if(input.charAt(input.length()-1) == '.') {
				subStr = input.substring(0, input.length()-1);
			}
			else {
				subStr = input;
			}
		}
		return subStr;
	}
	
	private String getPageHeaderText(int pageNo, String paraHeadingName) {
		String text = "";
		DocumentMetaData prev = null, next = null;
		LOGGER.info("Get header element paragrapah content:Started");
		try {
			for(DocumentMetaData headerdata:listwholeDocScanHeaders) {			
				//Remove '.' / ':' characters from headerdata - HeaderName.
				if(headerdata != null && headerdata.getHeaderPageNo() == pageNo) {

					if(stripTrailingChars(headerdata.getHeaderName()).compareToIgnoreCase(paraHeadingName) == 0 ||
							stripTrailingChars(headerdata.getHeaderName()).toUpperCase().contains(paraHeadingName.toUpperCase())	
							) {
						prev = headerdata;				
					}
					else if(prev != null && 
							(stripTrailingChars(prev.getHeaderName()).compareToIgnoreCase(paraHeadingName) == 0 ||
									stripTrailingChars(prev.getHeaderName()).toUpperCase().contains(paraHeadingName.toUpperCase())
									) &&
							headerdata.getHeaderParagraphIndex() == prev.getHeaderParagraphIndex() + 1) {
						next = headerdata;						
					}
					if(next != null && prev != null) break;
				}
			}
			//If the search context header does not have any next header in current page, this process wont work.
			//To fix that - find out that if the search context is the last index for the current page, then
			//read the entire rest of the page and continue the next page beginning if any and concatenate to get the string.
			if(null != next && null != prev) {
				//Get margin data
				TextMarginFinder finder;				
				PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
				finder = parser.processContent(pageNo, new TextMarginFinder());
				float llx = finder.getLlx();
				float lly = next.getStartLocationY() + 2; //may need to add lineheight instead of 2.
				float uux = finder.getUrx();
				float uuy = prev.getStartLocationY() +2;

				//Get the in between data of two successive headers from coordinate
				Rectangle rect = new Rectangle(llx,lly,uux,uuy);
				RenderFilter[] renderFilter = new RenderFilter[1];
				renderFilter[0] = new RegionTextRenderFilter(rect);
				FilteredTextRenderListener textExtractionStrategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(),renderFilter);
				text = PdfTextExtractor.getTextFromPage(pdfReader,pageNo,textExtractionStrategy);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("Get header element paragrapah content:Completed");
		return text;
	}

	private void updateDocumentProcessingInfo() {		
		char isLIBOR = 'N';
		int fallbackTextComplexity = 0; //default
		boolean fallbackRobust_P1 = false;
		boolean fallbackAssestConsistency_P2 = false;
		boolean fallbackImplFairFlexi_P3 = false;
		boolean fallbackBenchmarkRate_P4 = false;
		boolean fallbackSpreadAdjustment_P4 = false;
		boolean fallbackTriggerEventProposed_P4 = false;
		boolean fallbackTriggerEventExisting = false;
		listdocProcessingInfo = new ArrayList<DocumentProcessingInfo>();
		DocumentProcessingInfo info = new DocumentProcessingInfo();
		
		for(DocumentMetaData eachContextInfo:listOnlyConfiguredContextHeaders) {			
			
			switch(eachContextInfo.getDomainContextDictionaryId()) {
			case "Agreement_Name":
				break;
			case "Agreement_Date":
				Date startDate = null;
				try {
						if(eachContextInfo.getDomaincontextCurrentFieldValue() != null && !eachContextInfo.getDomaincontextCurrentFieldValue().isEmpty()) {
							startDate = new SimpleDateFormat("YYYY-MM-dd").parse(eachContextInfo.getDomaincontextCurrentFieldValue());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				info.setStartDate(startDate);				
				
				break;			
			case "Borrower":				
				info.setCounterPartyName(eachContextInfo.getDomaincontextCurrentFieldValue());
				break;
			case "Lender":				
				info.setLegalEntityName(eachContextInfo.getDomaincontextCurrentFieldValue());
				break;
			case "Agent":				
				break;
			case "Syndication_Agent":				
				break;
			case "Loan_Amount":
				info.setAmount(eachContextInfo.getDomaincontextCurrentFieldValue());
				break;
			case "Base_Rate":
				break;
			case "Base_Rate_Margin":
				break;
			case "Initial_Maturity_Date":
				Date terminationDate = null;
				try {
					if(eachContextInfo.getDomaincontextCurrentFieldValue() != null && !eachContextInfo.getDomaincontextCurrentFieldValue().isEmpty()) {
						terminationDate = new SimpleDateFormat("YYYY-MM-dd").parse(eachContextInfo.getDomaincontextCurrentFieldValue());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				info.setTerminationDate(terminationDate);
				break;
			case "LIBOR":				
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {					
					isLIBOR = 'Y';
				}
				info.setIsLIBOR(isLIBOR);				
				break;
			case "LIBOR_Banking_Day":				
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {					
					isLIBOR = 'Y';
				}
				info.setIsLIBOR(isLIBOR);
				break;
			case "LIBOR_Rate":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {					
					isLIBOR = 'Y';
				}
				info.setIsLIBOR(isLIBOR);
				break;
			case "LIBOR_Rate_Margin":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {					
					isLIBOR = 'Y';
				}
				info.setIsLIBOR(isLIBOR);
				break;
			case "LIBOR_Rate_Period":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {					
					isLIBOR = 'Y';
				}
				info.setIsLIBOR(isLIBOR);
				break;
			case "Fallback_Benchmark_Unavailable":
				String fallbackText_unavailable = eachContextInfo.getDomaincontextCurrentFieldValue();
				if(fallbackText_unavailable != null && !fallbackText_unavailable.isEmpty()) {
					info.setFallbackText(fallbackText_unavailable);
					info.setFallbackPage(eachContextInfo.getHeaderPageNo());
					info.setFallbackPresent('Y');
					info.setFallbackPosition(eachContextInfo.getHeaderParagraphIndex());
					
				}
				break;
			case "Fallback_Benchmark_Illegal":
				break;
			case "ARRC_P1_Fallback_Robustness":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackRobust_P1 = true;
				}
				break;
			case "ARRC_P2_Fallback_AssetClass_Consistency":				
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackAssestConsistency_P2 = true;
				}
				break;
			case "ARRC_P3_Fallback_Implementation_Fairness":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackImplFairFlexi_P3 = true;
				}
				break;
			case "ARRC_P3_Fallback_Implementation_Flexibility":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Forward-looking SOFR":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Next Available Term SOFR":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Compounded SOFR":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Relevant ISDA Fallback Rate":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Issuer Selected Rate":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Noteholder Selected Rate":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Explicit_Benchmark_Rate_Transaction Specific Rate":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackBenchmarkRate_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Spread_Adjustment_Hardwired_ARCC Selected":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackSpreadAdjustment_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Spread_Adjustment_Hardwired_ISDA Selected":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackSpreadAdjustment_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Spread_Adjustment_Hardwired_Other Selected":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackSpreadAdjustment_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Spread_Adjustment_Amendment_Lender Consent":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackSpreadAdjustment_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Spread_Adjustment_Amendment_Borrower Consent":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackSpreadAdjustment_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Term_Structure":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {

				}
				break;
			case "ARRC_P4_Fallback_Trigger_Permenent_Cessation_Benchmark_Admin":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;	
			case "ARRC_P4_Fallback_Trigger_Permenent_Cessation_Regulatory_Supervisor":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Trigger_Pre-cessation_Regulatory_Supervisor":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Trigger_Pre-cessation_Other":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Trigger_Pre-cessation_FederalOrState_Law":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Trigger_Early_Opt-in_SOFR_Hardwired":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Trigger_Early_Opt-in_Lender_Amendment":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "ARRC_P4_Fallback_Trigger_Early_Opt-in_Syndicate_Agent_Amendment":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventProposed_P4 = true;
				}
				break;
			case "Fallback_Trigger_WaterFall1_Unavailable_At_source":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventExisting = true;
				}
				break;
			case "Fallback_Trigger_WaterFall2_Agent_Unable_Quotes_Banks_London interbank market":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventExisting = true;
				}
				break;
			case "Fallback_Trigger_WaterFall3_Agent_Unable_Quotes_Banks_New York City":
				if(eachContextInfo.getHeaderTextContent() != null && !eachContextInfo.getHeaderTextContent().isEmpty() && eachContextInfo.getTextSimilarity() > 90) {
					fallbackTriggerEventExisting = true;
				}
				break;
			default:
				break;
			}
			
			info.setCurrency("USD");
			String predictions = "syndicate"; //Default Value.
			info.setPredictions(predictions);
			//Fallback complexity Assessment-
			//There are following sections - 1. Fallback Trigger Event 2.ARRC Proposed Fallback Trigger Event
			//3. Fallback Term Structure 4. AssetClass_Consistency(P2) 5. Implementation_Fairness(P3)
			//6. Implementation_Flexibility(P3) 7.Explicit_Benchmark_Rate 8. Spread Adjustment

			if(fallbackRobust_P1 && fallbackAssestConsistency_P2 && fallbackImplFairFlexi_P3) {			
				fallbackTextComplexity = 1;
				if(fallbackBenchmarkRate_P4 && fallbackSpreadAdjustment_P4) {
					fallbackTextComplexity = 2;
					if(fallbackTriggerEventProposed_P4 || fallbackTriggerEventExisting) {
						fallbackTextComplexity = 3;
					}
				}				
			}
			info.setFallbackTextComplexity(fallbackTextComplexity);			
		}
		info.setDocFileName(docFileName);
		listdocProcessingInfo.add(info);
		documentProcessingInfoService.saveDocumentProcessingInfo(listdocProcessingInfo);
	}
	
	public void findMatchHeadersfromContractDoc(int contractId) {
		
		
		getParagraphHeadingData();

		if(null != listOnlyConfiguredContextHeaders && null != mapwholeDocScanHeaders) {
			LOGGER.info("Find Matched Headers as per Configured Context:Started");
			
			for(DocumentMetaData activeContextData:listOnlyConfiguredContextHeaders) {

				//System.out.println(activeContextData.getDomainContextPossibleNameDefinitions());
				//for(DocumentMetaData eachparaHeading:paraHeadingData) {

				String domainContextName = activeContextData.getDomainContextPossibleNameDefinitions();
				DocumentMetaData eachparaHeading = mapwholeDocScanHeaders.get(domainContextName);
				
				if(eachparaHeading != null) {
					StringTokenizer tokenizer = new StringTokenizer(domainContextName,"|");
					String token = "";									
					String eachParaHeadingName = stripTrailingChars(stripTrailingSpaces(eachparaHeading.getHeaderName()));					


					//while(tokenizer.hasMoreTokens()) {
					//token = tokenizer.nextToken();					
					//if(eachParaHeadingName.compareToIgnoreCase(domainContextName) == 0) {					
					if(eachParaHeadingName.contains(domainContextName)) {
						activeContextData.setHeaderFontName(eachparaHeading.getHeaderFontName());
						activeContextData.setHeaderFontSize(eachparaHeading.getHeaderFontSize());
						activeContextData.setHeaderName(eachparaHeading.getHeaderName());
						activeContextData.setHeaderPageNo(eachparaHeading.getHeaderPageNo());
						activeContextData.setHeaderParagraphIndex(eachparaHeading.getHeaderParagraphIndex());
						activeContextData.setEndLocationX(eachparaHeading.getEndLocationX());
						activeContextData.setEndLocationY(eachparaHeading.getEndLocationY());
						activeContextData.setStartLocationX(eachparaHeading.getStartLocationX());
						activeContextData.setStartLocationY(eachparaHeading.getStartLocationY());
						String headerTextContent = getPageHeaderText(eachparaHeading.getHeaderPageNo(),eachParaHeadingName);
						activeContextData.setHeaderTextContent(headerTextContent);									
					}
					//}
					//}
				}
			}
		}
		LOGGER.info("Find Matched Headers as per Configured Context:Completed");
	}
	
	private void getPageLevelRawText() {
		listpageLevelOnlyRawText = new ArrayList<String>();
		String text = "";
		for(int i=1;i<=pdfReader.getNumberOfPages();i++) {
			
			try {
				text = PdfTextExtractor.getTextFromPage(pdfReader, i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listpageLevelOnlyRawText.add(text);
		}
	}
	
	public DocumentMetaDataExtended getExtendedMetaData() {
		extendedMetadata = new DocumentMetaDataExtended();
		extendedMetadata.setListdocumentMetaData(listOnlyConfiguredContextHeaders);
		extendedMetadata.setListpageLevelOnlyRawText(listpageLevelOnlyRawText);
		return extendedMetadata;
	}
	
	public DocumentMetaDataExtended getExtendedMetaData_PostAnalysis() {
		
		List<DocumentMetaData> metadata = extendedMetadata.getListdocumentMetaData();
		for(DocumentMetaData record:metadata) {
			if(record.getDomainContextDictionaryId().compareToIgnoreCase("Fallback_Benchmark_Unavailable") == 0) {
				record.setDictionaryIdupdateRequired(true);
				record.setTextSimilarity(60);
				record.setDomaincontextProposedFieldValue(record.getDomainContextPossibleValueDefinitions());
			}
		}
		//Write it to edit bucket required for doc edit:start
		String key = docFileName,cloudDir="",outFilePath="";
		boolean cloud = true;
		
		key = key.substring(0, key.indexOf(".pdf")) + ".json";
		if(storagelocation.compareToIgnoreCase("awss3") == 0) {
			cloud = true;			
			cloudDir = System.getProperty("java.io.tmpdir");
			outFilePath = cloudDir + File.separator + key;
		}
		else
		{
			outFilePath = ".\\edit" + File.separator + key;
		}
						
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(outFilePath), extendedMetadata);
		} catch (JsonGenerationException e) {			
			e.printStackTrace();
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		if(cloud) {
			try {
				amazonClient.uploadAnalysiedMetadataFileToS3bucket(key, outFilePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}					
					
		//end
		return extendedMetadata;
	}
	
	public void updateDocProcessingInfo() {
		this.updateDocumentProcessingInfo();
	}
	
	public void analyseDatafromContractDoc(List<DocumentMetaData> activeDomainDtls,int contractId) {
		listOnlyConfiguredContextHeaders = activeDomainDtls;
		String key = "";
		byte[] content = null;
		LOGGER.info("Data Analysis for each active Context:Started");
		boolean cloud = false;
		if(storagelocation.compareToIgnoreCase("awss3") == 0){
			cloud = true;
		}
		
		//Read the document from S3 Bucket.
		//
		//Get the document name from the contractId
		Contract con = contractService.findByContractId(contractId);
		String txtConvertedFileName = con.getDocumentFileName();
		docFileName = txtConvertedFileName;
		LOGGER.info("Document Name=" + txtConvertedFileName);
		String outputFilePath = ".\\ocr" + File.separator + txtConvertedFileName;
		if(null != con) {
			try {
				if(cloud)
				{
					content = amazonClient.downloadOCRFileFromS3bucket(docFileName);					
					pdfReader = new PdfReader(content);
				}
				else {
					pdfReader = new PdfReader(outputFilePath);
				}
				findMatchHeadersfromContractDoc(contractId);
				//New logic to find out the underline text as earlier Bold Text finding is not working...
				//
				//Get page level raw text Data.
				getPageLevelRawText();
				pdfReader.close();
				//Now Send to Python for analysis												
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LOGGER.info("Data Analysis for each active Context:Completed");		
	}
	
}
