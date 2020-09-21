package com.gblib.core.repapering.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.services.AmazonClient;

@RestController
public class SendEmailController {

	@Autowired
	private AmazonClient amazonClient;

	@RequestMapping(value = "/sendemail", method = RequestMethod.POST)
	public String sendEmail(@RequestBody String emailId, @RequestBody String fileNameWithExtention,
			@RequestBody String contractID) throws Exception {
		sendmail(emailId, fileNameWithExtention, contractID);
		return "Email sent successfully";
	}

	private void sendmail(String emailId, String fileNameWithExtention, String contractID) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("manishsil89@gmail.com", "**********");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("manishsil89@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailId));
		msg.setSubject("Contract Document");
		msg.setContent("GBLIB email", "text/html");
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("GBLIB email", "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		MimeBodyPart attachPart = new MimeBodyPart();

		attachPart.attachFile(getAttachmentFilePath(fileNameWithExtention));
		multipart.addBodyPart(attachPart);
		msg.setContent(multipart);
		Transport.send(msg);
	}

	public String getAttachmentFilePath(String inputFileNameWithExt) throws Exception {
		byte[] document = amazonClient.downloadFile(inputFileNameWithExt);

		File file = new File("email/"+inputFileNameWithExt);
		file.getParentFile().mkdirs();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			// Writes bytes from the specified byte array to this file output stream
			fos.write(document);
			return "email/"+inputFileNameWithExt;
		} catch (Exception e) {
			System.out.println("File not found" + e);
			throw new Exception(e);
		} finally {
			// close the streams using close method
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}

		}
	}
}
