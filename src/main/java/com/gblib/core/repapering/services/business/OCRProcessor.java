package com.gblib.core.repapering.services.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.internal.ExtensionMediaTypeMapping;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.OCROperation;
import com.gblib.core.repapering.services.AmazonClient;

@Component
public class OCRProcessor implements IOCRProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(OCRProcessor.class);

	@Value("${gblib.core.repapering.ocr.inputdir}")
	private String inputFileDir;

	@Value("${gblib.core.repapering.ocr.outputdir}")
	private String outputFileDir;

	@Autowired
	private AmazonClient amazonClient;

	@Override
	public int convert(String inputFile, String outputFile) {
		// TODO Auto-generated method stub
		String inputFilePath = inputFileDir + File.separator + inputFile;
		String outputFilePath = outputFileDir + File.separator + outputFile;
		int bRet = 1; // success;
		try {

			// Initial setup, create credentials instance.
			Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
					.fromFile("pdftools-api-credentials.json").build();

			// Create an ExecutionContext using credentials and create a new operation
			// instance.
			ExecutionContext executionContext = ExecutionContext.create(credentials);
			OCROperation ocrOperation = OCROperation.createNew();

			// Set operation input from a source file.
			FileRef source = FileRef.createFromLocalFile(inputFilePath);
			ocrOperation.setInput(source);

			// Execute the operation
			FileRef result = ocrOperation.execute(executionContext);
			// Save the result at the specified location
			result.saveAs(outputFilePath);

		} catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
			LOGGER.error("Exception encountered while executing operation", ex);
			bRet = 0;
		} finally {
		}
		return bRet;
	}

	@Override
	public int downloadAndConvert(String inputFileNameWithExt, String outputFileName) {
		// TODO Auto-generated method stub
		
		int bRet = 1; // success;
		try {

			// Download the file inputFileWithExt from S3
			LOGGER.info("Start downloading from S3 file Name = " + inputFileNameWithExt);
			byte[] document = amazonClient.downloadFile(inputFileNameWithExt);
			LOGGER.info("Complete downloading from S3 file Name = " + inputFileNameWithExt);

			//Convert byte to InputStream
			LOGGER.info("Start converting byte array to InputStream");
			InputStream documentInputStrm = new ByteArrayInputStream(document);
			
			//Call OCR Api
			LOGGER.info("Calling OCR");
			this.ocrProcess(documentInputStrm, outputFileName);
			LOGGER.info("Successfully complete OCR");	

		} catch (Exception ex) {
			LOGGER.error("Exception encountered while executing operation.", ex);
			bRet = 0;
			//
			// Overwrite the return value with 1 and upload file to S3 manually to handle the failure of OCR process.
			bRet = 1;
			LOGGER.info("Return val is overwritten with success.");
			//
		} finally {
		}
		return bRet;
	}
	
	public void ocrProcess(InputStream documentInputStrm, String outputFileName) throws Exception {
		//String outputFilePath = outputFileDir + File.separator + outputFile;
		try {
			// Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();
            
          //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            OCROperation ocrOperation = OCROperation.createNew();
            
         // Set operation input from a source file.   
            FileRef source = FileRef.createFromStream(documentInputStrm, ExtensionMediaTypeMapping.PDF.getMediaType());
            ocrOperation.setInput(source);

            // Execute the operation
            FileRef result = ocrOperation.execute(executionContext);

            // Save the result at the specified location
            //result.saveAs(outputFilePath);
            //System.out.println("Successfully saved file received from OCR");
            
            
            //Storing data to outputStream and upload to another S3
            OutputStream outputStream= prepareOutputStream(outputFileName);
            result.saveAs(outputStream);
            System.out.println("Successfully saved as outputStream received from OCR");
            
            File initialFile = new File("output/"+outputFileName);
            InputStream targetStream = new FileInputStream(initialFile);
            
            amazonClient.uploadInputStreamToS3bucketAfterOCR(outputFileName,targetStream);
            LOGGER.error("Successfully saved file in S3 location received from OCR");
            
		} catch (Exception ex) {
			ex.printStackTrace();
            LOGGER.error("Exception encountered while executing OCR", ex);
            throw new Exception(ex);
        }
	}
	private static FileOutputStream prepareOutputStream(String outputFileName) throws FileNotFoundException {
        File file = new File("output/"+outputFileName);
        // Create the result directories if they don't exist.
        file.getParentFile().mkdirs();
        return new FileOutputStream(file);
    }

}
