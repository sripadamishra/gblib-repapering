package com.gblib.core.repapering.services.business;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.OCROperation;

@Component
public class OCRProcessor implements IOCRProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(OCRProcessor.class);
	
	@Value("${gblib.core.repapering.ocr.inputdir}")
	private String inputFileDir;
	
	@Value("${gblib.core.repapering.ocr.outputdir}")
	private String outputFileDir;
	
	@Override
	public int convert(String inputFile, String outputFile) {
		// TODO Auto-generated method stub
		String inputFilePath = inputFileDir + File.separator + inputFile;
		String outputFilePath = outputFileDir + File.separator + outputFile;
		int bRet = 1; //success;
		try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
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
        }
		finally {			
		}
		return bRet;
	}

}
