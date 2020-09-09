package com.gblib.core.repapering.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.global.WorkflowStageEnums;
import com.gblib.core.repapering.model.Contract;
import com.gblib.core.repapering.model.LiborDocument;
import com.gblib.core.repapering.services.ContractService;
import com.gblib.core.repapering.services.FileParsingService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class PdfDocumentEditController {

	@Autowired
	ContractService contractService;
	
	@Autowired
	FileParsingService parser;
	
	@RequestMapping(value = "/modify/contract", method = RequestMethod.POST)
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
				PdfReader pdfReader = new PdfReader("D:\\GoodByeLibor\\Testing docs\\irswap_DEUTSCHE BANK_LIBOR_TEXT.pdf");
				PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("D:\\GoodByeLibor\\Testing docs\\irswap_DEUTSCHE BANK_LIBOR_TEXT_updated.pdf"));
				PdfContentByte canvas = pdfStamper.getUnderContent(1);
				//ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Paragraph("If on any date on which Borrower seeks to establish a LIBOR Rate as the Applicable Interest Rate pursuant to Section 2.3 hereof or if Section 2.3(d) hereof applies, Agent determines (which determination shall be conclusive and binding upon Borrower absent manifest error) that (i) Dollar deposits in an amount approximately equal to the then outstanding principal balance of the Loan Portion bearing interest at a LIBOR Rate are not generally available at such time in the London interbank Eurodollar market for deposits in Eurodollars, (ii) reasonable means do not exist for ascertaining LIBOR, or (iii) the Applicable Interest Rate would be in excess of the maximum interest rate which Borrower may by law pay, Agent shall promptly give notice (the ?Non-Availability Notice?) of such fact to Borrower and the option to convert to or to continue the Applicable Interest Rate on such Loan Portion as a LIBOR Rate shall be suspended until such time as such condition no longer exists. In the event that the option to elect, to convert to or to continue an Applicable Interest Rate as a LIBOR Rate shall be suspended as provided in this Section 2.10(a), effective upon the giving of the Non-Availability Notice, and if applicable, effective as of the first date that the one (1) month LIBOR Rate Period would otherwise be in effect pursuant to Section 2.3(d) hereof, interest on the Loan Portion for which a LIBOR Rate was to be determined shall be payable at the Base Rate, from and including the date of the giving of the Non-Availability Notice (or the date that the one (1) month LIBOR Rate Period would otherwise be in effect pursuant to Section 2.3(d) hereof, if applicable) until the Maturity Date or until any earlier date on which a LIBOR Rate shall become effective for such Loan Portion pursuant to Section 2.3 hereof following the giving of notice by Agent to Borrower that the conditions referred to in this Section 2.10(a) no longer exist (Agent agreeing to give prompt notice to Borrower if such conditions no longer exist).!"), 250, 750, 0);
				//
				ColumnText ct = new ColumnText(null);
				ct.addElement(new Paragraph(10,new Chunk("SECTION 2.10. Unavailability of LIBOR; Illegality.")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("as the Applicable Interest Rate pursuant to Section 2.3 hereof or if Section 2.3(d) hereof applies, Agent ")));
				ct.addElement(new Paragraph(10,new Chunk("determines (which determination shall be conclusive and binding upon Borrower absent manifest error) that")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				ct.addElement(new Paragraph(10,new Chunk("(a) Unavailability of LIBOR. If on any date on which Borrower seeks to establish a LIBOR Rate")));
				
				
				
				PdfImportedPage page = pdfStamper.getImportedPage(pdfReader, 1);
				int i=0;
				while(true) {
					pdfStamper.insertPage(++i,pdfReader.getPageSize(1));
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
}
