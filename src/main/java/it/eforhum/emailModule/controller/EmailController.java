package it.eforhum.emailModule.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eforhum.emailModule.dtos.EmailDataDTO;
import it.eforhum.emailModule.entity.EmailData;
import it.eforhum.emailModule.service.EmailSenderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/send/email")
@Slf4j
public class EmailController {

    private final EmailSenderService senderService;

    public EmailController(EmailSenderService senderService){
        this.senderService = senderService;
    }
  

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDataDTO emailDataDTO) {

        log.info("Recived email send request for: {}", emailDataDTO.dest());
        
        if(emailDataDTO.dest() == null || emailDataDTO.dest().isEmpty()) {
            log.warn("Recipient is required");
            return ResponseEntity.badRequest().build();
        }

        if(!EmailData.isRecipientValid(emailDataDTO.dest())){
            log.warn("The email provided is not valid");
            return ResponseEntity.badRequest().build();
        }

        EmailData emailData = new EmailData(emailDataDTO.dest(), emailDataDTO.body(), emailDataDTO.subject());

        if(senderService.sendMessage(emailData)){
            log.info("Email sent successfully");
            return ResponseEntity.ok().build();
        }

        log.warn("An error occurred while sending the email");
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
    }
}
