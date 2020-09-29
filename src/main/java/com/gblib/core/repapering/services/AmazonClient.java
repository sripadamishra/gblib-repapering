package com.gblib.core.repapering.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.gblib.core.repapering.services.business.OCRProcessor;

@Service
public class AmazonClient {

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.scan.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.ocr.bucketName}")
	private String ocrBucketName;	
	@Value("${amazonProperties.metadata.bucketName}")
	private String metadataBucketName;	
	@Value("${amazonProperties.edit.bucketName}")
	private String editBucketName;
	
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonClient.class);
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		//return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
		return multiPart.getOriginalFilename();
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file));
		LOGGER.info("File is uploaded sucessfully to bucket= " + bucketName + " with key= " + fileName);
	}

	public String uploadFile(MultipartFile multipartFile) throws Exception {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return fileUrl;
	}
	
	public String deleteFileFromS3Bucket(String fileName) {
		//String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
	    return "Successfully deleted";
	}
	
	public byte[] downloadFile(final String keyName) throws Exception {
        byte[] content = null;
        
        try {
        	LOGGER.info("Downloading an object with key= " + keyName);
            final S3Object s3Object = s3client.getObject(bucketName, keyName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            LOGGER.info("File downloaded successfully.");
            s3Object.close();
        } catch(final IOException ex) {
        	LOGGER.error("IO Error Message= " + ex.getMessage());
        	throw new Exception(ex);
        }
        return content;
    }
	
	public String uploadInputStreamToS3bucketAfterOCR(String fileName, InputStream fileInputStream) throws Exception {
		ObjectMetadata meta = new ObjectMetadata();
        try {
			meta.setContentLength(fileInputStream.available());
			// Need to use second bucket which contains all the file after OCR
			s3client.putObject(
					new PutObjectRequest(ocrBucketName, fileName, fileInputStream, meta));
			System.out.println("File Input Stream uploaded successfullyin S3");
			return "File Input Stream uploaded successfullyin S3";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public byte[] downloadOCRFileFromS3bucket(String keyName) throws Exception {
		byte[] content = null;
		try {
        	LOGGER.info("Downloading an object from bucket=" + ocrBucketName + " with key= " + keyName);
            final S3Object s3Object = s3client.getObject(ocrBucketName, keyName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            LOGGER.info( keyName + " File downloaded successfully from bucket= " + ocrBucketName);
            s3Object.close();
        } catch(final IOException ex) {
        	LOGGER.error("IO Error Message= " + ex.getMessage());
        	throw new Exception(ex);
        }
		return content;
	}
	
	public byte[] downloadMetadataFileFromS3bucket(String keyName) throws Exception {
		byte[] content = null;
		try {
        	LOGGER.info("Downloading an object from bucket=" + metadataBucketName + " with key= " + keyName);
            final S3Object s3Object = s3client.getObject(metadataBucketName, keyName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            LOGGER.info( keyName + "File downloaded successfully from bucket= " + metadataBucketName);
            s3Object.close();
        } catch(final IOException ex) {
        	LOGGER.error("IO Error Message= " + ex.getMessage());
        	throw new Exception(ex);
        }
		return content;
		
	}
	public void copyFromScanToOCRS3bucket(String sourceKey,String destKey) {		
		s3client.copyObject(bucketName, sourceKey, ocrBucketName, destKey);
		LOGGER.info("Source Scan bucket= " + bucketName + " with key= " + sourceKey);
		LOGGER.info("Dest OCR bucket= " + ocrBucketName + " with key= " + destKey);
	}
	public void uploadMetadataFileToS3bucket(String key,String fileName) throws IOException {
		
		File metadataFile = new File(fileName);
		s3client.putObject(metadataBucketName, key, metadataFile);
		LOGGER.info("Metadata file is uploaded to bucket= " + metadataBucketName + " with key= " + key);
	}
	
	public void uploadAnalysiedMetadataFileToS3bucket(String key,String fileName) throws IOException {
		
		File metadataFile = new File(fileName);
		s3client.putObject(editBucketName, key, metadataFile);
		LOGGER.info("Analysed Metadata file is uploaded to bucket= " + editBucketName + " with key= " + key);
	}
	
	public byte[] downloadAnalysiedMetadataFileFromS3bucket(String keyName) throws Exception{
		byte[] content = null;
		try {
        	LOGGER.info("Downloading an object from bucket=" + editBucketName + " with key= " + keyName);
            final S3Object s3Object = s3client.getObject(editBucketName, keyName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            LOGGER.info( keyName + "File downloaded successfully from bucketname= " + editBucketName);
            s3Object.close();
        } catch(final IOException ex) {
        	LOGGER.error("IO Error Message= " + ex.getMessage());
        	throw new Exception(ex);
        }
		return content;
	}
	
	public byte[] downloadEditedFileFromS3bucket(String keyName) throws Exception {
		byte[] content = null;
		try {
        	LOGGER.info("Contract File download is started from bucket=" + editBucketName + " with key= " + keyName);
            final S3Object s3Object = s3client.getObject(editBucketName, keyName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            LOGGER.info( keyName + "Contract File download is completed successfully from bucketname= " + editBucketName);
            s3Object.close();
        } catch(final IOException ex) {
        	LOGGER.error("IO Error Message= " + ex.getMessage());
        	throw new Exception(ex);
        }
		return content;
	}
	
	public void uploadEditFileToS3bucket(String key,String fileName) throws IOException {		
		File editFile = new File(fileName);
		LOGGER.info("Upload File " + fileName + "to bucket " + editBucketName +" is started.");
		s3client.putObject(editBucketName, key, editFile);
		LOGGER.info("Upload File " + fileName + "to bucket " + editBucketName +" is completed.");
	}

}
