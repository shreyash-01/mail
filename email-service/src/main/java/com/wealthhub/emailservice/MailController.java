package com.wealthhub.emailservice;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/email")
    public ResponseEntity<?> sendMail(@RequestBody String json) throws MessagingException {
        mailService.sendEmailWithAttachment(json);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
