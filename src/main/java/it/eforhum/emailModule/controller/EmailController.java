package it.eforhum.emailModule.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eforhum.emailModule.dtos.EmailDataDTO;
import it.eforhum.emailModule.entity.EmailData;
import it.eforhum.emailModule.service.EmailSenderService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/send/email")
public class EmailController {

    private EmailData emailData;
    private static final Logger logger = Logger.getLogger(EmailController.class.getName());

    @PostMapping
    public String sendEmail(@RequestBody EmailDataDTO emailDataDTO) {
        
        if(emailDataDTO.dest() == null || emailDataDTO.dest().isEmpty()) {
            logger.log(Level.WARNING, "Recipient email is required");
            return "Recipient email is required";
        }

        if(!EmailData.isRecipientValid(emailDataDTO.dest())){
            logger.log(Level.WARNING, "The email address " + emailDataDTO.dest() + " is not valid");
            return "Recipient email is not valid";
        }

        emailData.setBody(emailDataDTO.body());
        emailData.setRecipient(emailDataDTO.dest());
        emailData.setSubject(emailDataDTO.subject());

        EmailSenderService emailService = new EmailSenderService(emailData);

        if(emailService.sendMessage()){
            logger.log(Level.INFO, "Email sent successfully to " + emailDataDTO.dest());
            return "Email sent successfully";
        }

        logger.log(Level.WARNING, "Error sending email to " + emailDataDTO.dest());
        return "Error sending email";
    }
}
