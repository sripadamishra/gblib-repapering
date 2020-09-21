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
		/*s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));*/
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file));
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
	

}
