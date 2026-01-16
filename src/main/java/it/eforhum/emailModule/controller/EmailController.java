package it.eforhum.emailModule.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eforhum.emailModule.dtos.EmailDataDTO;
import it.eforhum.emailModule.entity.EmailData;
import it.eforhum.emailModule.service.EmailSenderService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/send/email")
@Slf4j
public class EmailController {

    private EmailData emailData;

    private final EmailSenderService senderService;

    public EmailController(EmailSenderService senderService){
        this.senderService = senderService;
    }
  

    @PostMapping
    public String sendEmail(@RequestBody EmailDataDTO emailDataDTO) {
        
        if(emailDataDTO.dest() == null || emailDataDTO.dest().isEmpty()) {
            log.warn("Recipient is required");
            return "Recipient email is required";
        }

        if(!EmailData.isRecipientValid(emailDataDTO.dest())){
            log.warn("The email provided is not valid");
            return "Recipient email is not valid";
        }

        emailData = new EmailData(emailDataDTO.dest(), emailDataDTO.body(), emailDataDTO.subject());

        if(senderService.sendMessage(emailData)){
            log.info("Email sended successfully");
            return "Email sent successfully";
        }

        log.warn("Error sending the email");
        return "Error sending email";
    }
}
