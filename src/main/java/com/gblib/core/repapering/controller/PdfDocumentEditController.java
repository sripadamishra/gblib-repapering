package com.gblib.core.repapering.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.DocumentMetaData;
import com.gblib.core.repapering.model.DocumentUpdateRequest;
import com.gblib.core.repapering.model.LiborDocument;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.FileParsingService;
import com.gblib.core.repapering.services.business.UnderlinedBoldedLocationTextExtractionStrategy;
import com.gblib.core.repapering.services.business.UnderlinedBoldedLocationTextExtractionStrategy.TextChunkInfo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextMarginFinder;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor;

@RestController
public class PdfDocumentEditController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	FileParsingService parser;
	
	@Value("${gblib.core.repapering.text.analytics.metadatadir}")
	private String analyticsDir;
	
	@Value("${gblib.core.repapering.text.analytics.docmetadata.filename}")
	private String docMetadataFilename;
	
	@Value("${gblib.core.repapering.text.analytics.domaincontextmetadata.filename}")
	private String domainMetadataFilename;
	
	private DocumentUpdateRequest documentUpdateRequest = null; 
	
	@RequestMapping(value = "/generate/contract/docmetadata/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<DocumentMetaData> readPdf(@PathVariable int contractId)	{		
		List<DocumentMetaData> docMetadata = new ArrayList<DocumentMetaData>();		
		try {
			PdfReader pdfReader = new PdfReader("D:\\Projects\\libor-master\\Demo-document\\syndicate_CREDIT AGRICOLE_LIBOR_TEXT.pdf");
			UnderlinedBoldedLocationTextExtractionStrategy strategy = new UnderlinedBoldedLocationTextExtractionStrategy();
			//for(int i=;i<=pdfReader.getNumberOfPages();i++) {
			for(int i=30;i<=31;i++) {				
				PdfTextExtractor.getTextFromPage(pdfReader, i,strategy);
				String chunkinfo = strategy.getResultantText();
				
				List<DocumentMetaData> pageData = strategy.getDocMetaData();
				int index =0;
				for(DocumentMetaData data:pageData) {
					data.setHeaderPageNo(i);
					data.setHeaderParagraphIndex(index);
					index++;
					docMetadata.add(data);
				}
				strategy.clean();//Reset all member variables after each page read.
			}
			System.out.println(getPageHeaderText(pdfReader,29,"No Withholdings",docMetadata));
			pdfReader.close();			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
		//
		// Write the docMetadata into a JSON file as well.
		writetoJSONFile(contractId,docMetadata);
		//
		return docMetadata;
	}
	
	@RequestMapping(value = "/edit/contract", method = RequestMethod.POST)
	public @ResponseBody String rewritePdf(@RequestBody String docmetadat) {
		String text = "Doc is updated sucesfully.";
		String docFileName = "",docFilePath = "";
		String updatedDocFileName = "",updatedDocFilePath = "";
		// Convert the docmetadata into DocumentUpdateRequest		
		
		Contract con = null;
		int contractId = 0;
		ObjectMapper mapper = new ObjectMapper();
		try {
			documentUpdateRequest = mapper.readValue(docmetadat, DocumentUpdateRequest.class);
			if(documentUpdateRequest != null) {
				con = contractService.findByContractId(documentUpdateRequest.getContractId());
				if(con != null) {
					docFileName = con.getDocumentFileName();
					updatedDocFileName = docFileName.substring(0, docFileName.indexOf(".pdf")) + "_UPDATED" + ".pdf";
					docFilePath = analyticsDir + File.separator + docFileName;
					updatedDocFilePath = analyticsDir + File.separator + updatedDocFileName;
					updateContract(docFilePath,updatedDocFilePath);
				}
			}
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	
	@RequestMapping(value = "/modify/contract", method = RequestMethod.GET)
	public @ResponseBody String editPdf(/*@RequestBody int contractid*/) {
	
		//Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Edit.ordinal() + 1);				
		//1. Get the text converted pdf file from contract table
		//2. Get the impacted pages where edit will be made from the documentprocessinginfo table
		//3. Get the text options from the 		
		
		
		//if(null != con) {
			
		//}
		//Contract con = contractService.findByContractIdAndCurrStatusId(contractid, WorkflowStageEnums.Edit.ordinal() + 1);
		String newFileName = "";
				try
		{
				PdfReader pdfReader = new PdfReader("D:\\Projects\\libor-master\\Demo-document\\syndicate_CREDIT AGRICOLE_LIBOR_TEXT.pdf");						
				PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("D:\\Projects\\libor-master\\Demo-document\\syndicate_CREDIT AGRICOLE_LIBOR_TEXT_UPDATED.pdf"));
				PdfContentByte canvas = pdfStamper.getUnderContent(1);				
				ColumnText ct = new ColumnText(null);
				Paragraph libor = new Paragraph("Unavailability of LIBOR:",
			               FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, new BaseColor(0, 0, 0)));
				ct.addElement(libor);
				ct.addElement(new Paragraph(10,"Three-month LIBOR means the rate (expressed as a percentage per annum) for deposits in US dollars",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"for a three-month period commencing on the first day of a Dividend Period that appears on the Reuters",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"Screen LIBOR01 Page as of 11:00 a.m. (London time) on the LIBOR Determination Date for that",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"Dividend Period. If such rate does not appear on Reuters Screen LIBOR01 Page,",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"Three-month LIBOR will be determined on the basis of the rates at which deposits in United States",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"dollars for a three-month period ... are offered to prime banks in the London interbank market",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"by four major banks in the London interbank market selected by the Calculation Agent ...",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"If at least two such quotations are provided, Three-month LIBOR with respect to that Dividend Period",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"will be the arithmetic mean ... of such quotations. If fewer than two quotations are provided,",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"Three-month LIBOR with respect to that Dividend Period will be the arithmetic mean ... of the rates quoted",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"by three major banks in New York City selected by the Calculation Agent ... However, if fewer than three banks",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"selected by the Calculation Agent to provide quotations are quoting as described above, three-month LIBOR for",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				ct.addElement(new Paragraph(10,"that Dividend Period will be the same three-month LIBOR as determined for the previous Dividend Period.",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
				
				
				
				
				PdfImportedPage page = pdfStamper.getImportedPage(pdfReader, 30);
				int i=0;
				while(true) {
					pdfStamper.insertPage(30,pdfReader.getPageSize(30));
					//pdfStamper.getUnderContent(i).addTemplate(page,0,0);
					ct.setCanvas(pdfStamper.getOverContent(i));
					ct.setSimpleColumn(10,10,600,200);
					if(!ColumnText.hasMoreText(ct.go())) {
						break;
					}					
				}
				
				//
				pdfStamper.close();
				pdfReader.close();
		}
		catch (IOException e)
		{
				e.printStackTrace();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}		
	    
		return newFileName;
	}
	
	private void updateContract(String inputFile, String updatedFile){
		
		PdfReader pdfReader = null;
		PdfStamper pdfStamper = null;
		Font fontBoldHeader = null;
		Font fontNormalBody = null;
		Rectangle rectHeader=null, rectBody = null;
		//Get margin data
		TextMarginFinder finder = null;
		PdfReaderContentParser parser = null;
		int pageNo = 1, pageIndex = 0;
		float llx=0.0f,lly=0.0f,uux=0.0f,uuy=0.0f;
		String[] arrSplit = null;
		DocumentMetaData prev = null,next = null;
		List<PdfCleanUpLocation> pdfCleanUpLocations = null; 

		List<DocumentMetaData> metadata = null;
		try {
			pdfReader = new PdfReader(inputFile);
			pdfStamper = new PdfStamper(pdfReader,new FileOutputStream(updatedFile));
			fontBoldHeader = new Font(FontFamily.TIMES_ROMAN,10,Font.BOLD,BaseColor.BLACK);
			fontNormalBody = new Font(FontFamily.TIMES_ROMAN,10,Font.NORMAL,BaseColor.BLUE);			
			if(documentUpdateRequest != null) {
				metadata= documentUpdateRequest.getListDocumentMetadata();
				if(metadata != null) {
					for(DocumentMetaData fieldDetail:metadata) {						
						if(fieldDetail != null && fieldDetail.isDictionaryIdupdateRequired()) {
							pageNo = fieldDetail.getHeaderPageNo(); 
							parser = new PdfReaderContentParser(pdfReader);
							prev = fieldDetail;
							next = metadata.get(pageIndex+1);
							if(next != null) {
								finder = parser.processContent(pageNo, new TextMarginFinder());
								if(finder!= null) {
									llx = finder.getLlx();
									lly = next.getStartLocationY() + 6; //may need to add lineheight instead of 2.
									uux = finder.getUrx();
									uuy = prev.getStartLocationY() +5;
									rectBody = new Rectangle(llx,lly,uux,uuy);
									pdfCleanUpLocations = new ArrayList<PdfCleanUpLocation>();
									pdfCleanUpLocations.add(new PdfCleanUpLocation(pageNo,rectBody,BaseColor.WHITE));
									
									PdfContentByte cb = pdfStamper.getOverContent(pageNo);
									ColumnText ct = new ColumnText(cb);
									ct.setSimpleColumn(rectBody);
									
									String textPlusHeader = fieldDetail.getDomainContextName() + ":" + "\n" + fieldDetail.getDomaincontextProposedFieldValue(); 
									
									arrSplit = textPlusHeader.split("\\r?\\n");
									int i =0;
									Font fontSelected =null;
									if(arrSplit != null) {
										for(String line:arrSplit) {
											if(i ==0) {
												fontSelected = fontBoldHeader;												
											}
											else {
												fontSelected = fontNormalBody;												
											}
											Paragraph para = new Paragraph(12,line,fontSelected);									
											ct.addElement(para);
											ct.go();
											
											i++;
										}
									}
								}		
							}
						}
						pageIndex++;
					}
				}
			}								
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			PdfCleanUpProcessor processor = new PdfCleanUpProcessor(pdfCleanUpLocations, pdfStamper);
			processor.cleanUp();
			pdfStamper.close();
			pdfReader.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
	
	private String removeTrailingChars(String input) {
		return input.replace('.', '\0').replace(':', '\0'); //Replace trailing . and :
	}
	
	private String getPageHeaderText(PdfReader pdfReader, int pageNo, String domainContextDesc,List<DocumentMetaData> docMetadata) {
		String text = "";
		DocumentMetaData prev = null, next = null;
		try {
			for(DocumentMetaData headerdata:docMetadata) {			
				//Remove '.' / ':' characters from headerdata - HeaderName.
				if(headerdata != null && headerdata.getHeaderPageNo() == pageNo) {

					if(removeTrailingChars(headerdata.getHeaderName()).compareToIgnoreCase(domainContextDesc) == 0 ||
							removeTrailingChars(headerdata.getHeaderName()).toUpperCase().contains(domainContextDesc.toUpperCase())	
							) {
						prev = headerdata;				
					}
					else if(prev != null && 
							(removeTrailingChars(prev.getHeaderName()).compareToIgnoreCase(domainContextDesc) == 0 ||
							removeTrailingChars(prev.getHeaderName()).toUpperCase().contains(domainContextDesc.toUpperCase())
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
		return text;
	}

	private void writetoJSONFile(int contractId,List<DocumentMetaData> docMetadata) {
		
		String outFilePath ="";
				
		if(docMetadataFilename.isEmpty()) {
			docMetadataFilename = "_DOCMETADATA.JSON";
		}
		
		outFilePath = analyticsDir +File.separator + contractId + docMetadataFilename;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(outFilePath), docMetadata);
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
	}
}
