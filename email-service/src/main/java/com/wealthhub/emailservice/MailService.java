package com.wealthhub.emailservice;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.FileSystem;

@Service
public class MailService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("doreamonshinchan58@gmail.com");
        simpleMailMessage.setTo("singhshreyash0075@gmail.com");
        simpleMailMessage.setSubject("Confirm the Investment Adviser Agreement for AltQube BlueChip Portfolio");
        simpleMailMessage.setText("Hello from shreyash");
        javaMailSender.send(simpleMailMessage);
    }

    public void sendEmailWithAttachment(String json) throws MessagingException {

        byte[] response = generatedPDF(json).getBody();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("doreamonshinchan58@gmail.com");
        mimeMessageHelper.setTo("singhshreyash0075@gmail.com");
        mimeMessageHelper.setText("Hello");
        mimeMessageHelper.setSubject("Confirm the Investment Adviser Agreement for AltQube BlueChip Portfolio");

        DataSource dataSource = new ByteArrayDataSource(response, "application/pdf");

        mimeMessageHelper.addAttachment("Agreement", dataSource);
        javaMailSender.send(mimeMessage);


    }

    public ResponseEntity<byte[]> generatedPDF(String json){
        String pythonServiceUrl = "http://localhost:8000/api";


        HttpEntity<String> request = new HttpEntity<>(json);


        byte[] response = restTemplate.exchange(pythonServiceUrl, HttpMethod.POST, new org.springframework.http.HttpEntity<>(json), byte[].class).getBody();
        HttpHeaders pdfHeaders = new HttpHeaders();
        pdfHeaders.setContentType(MediaType.APPLICATION_PDF);
        pdfHeaders.setContentDispositionFormData("attachment", "response.pdf");
        pdfHeaders.setContentLength(response.length);

        return new ResponseEntity<>(response, pdfHeaders, HttpStatus.OK);
    }
}
