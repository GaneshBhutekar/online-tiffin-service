package com.cdac.onlineTiffinService.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cdac.onlineTiffinService.exceptions.MyIOException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class EmailService {
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	
    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;
    
    public void sendEmail(String to,String subject,String content) {
    	Email from = new Email(fromEmail);
    	Email toEmail = new Email(to);
    	Content emailContent = new Content("text/plain",content);
    	Mail mail = new Mail(from,subject,toEmail,emailContent);
    	// configuring the send grid
    	
    	SendGrid sg = new SendGrid(apiKey);
    	Request request = new Request();
    	try {
    		request.setMethod(Method.POST);
    		request.setEndpoint("mail/send");
    		request.setBody(mail.build());
    		
    		//sending the email
    		Response response = sg.api(request);
    		// The SendGrid SDK does NOT throw on 4xx/5xx responses - it returns them
    		// as a normal Response object. We must check the status code ourselves,
    		// otherwise failures (bad API key, unverified sender, bad payload, etc.)
    		// get silently swallowed and it looks like the email was "sent".
    		log.info("SendGrid response status={} to={} subject={}", response.getStatusCode(), to, subject);

    		if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
    			log.error("SendGrid rejected the email. status={} body={} headers={}",
    					response.getStatusCode(), response.getBody(), response.getHeaders());
    			throw new MyIOException("SendGrid rejected the email (status "
    					+ response.getStatusCode() + "): " + response.getBody());
    		}
    	}catch(IOException e) {
    		throw new MyIOException("Failed to send mail "+ e.getMessage());
    	}
    }
}
