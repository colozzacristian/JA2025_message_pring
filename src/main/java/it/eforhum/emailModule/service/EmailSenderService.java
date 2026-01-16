package it.eforhum.emailModule.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.colozzacristian.SmtpConnection;
import com.github.colozzacristian.SmtpConnectionBuilder;
import com.github.colozzacristian.SmtpResponse;
import com.github.colozzacristian.SmtpSession;

import it.eforhum.emailModule.entity.EmailData;
import it.eforhum.emailModule.interfaces.Sendable;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class EmailSenderService implements Sendable{
    
    @Value("${gmail.port}")
    private Integer port;

    @Value("${gmail.app.password}")
    private String password;

    @Value("${gmail.account}")
    private String user;

    private EmailData emailData;
    private SmtpSession session;

    public EmailSenderService(){/*default constructor*/}

    @Override
    public boolean sendMessage(EmailData emailData){

        if(emailData == null){
            log.error("Email data cannot be null");
            return false;
        }
        if(emailData.getRecipient() == null || emailData.getRecipient().isEmpty()){
            log.error("Email recipient cannot be null or empty");
            return false;
        }

        try {
            SmtpConnection connection = SmtpConnectionBuilder.connectSSL("smtp.gmail.com", port, "localhost");
            if(connection == null){
                log.warn("Failed to connect to the SMTP server");
                return false;
            }
            
            session = connection.createSession(user, password);
            if(session == null){
                log.warn("Failed to create session");
                return false;
            }

            SmtpResponse response = session.sendMail(
                EmailData.SENDER,
                emailData.getRecipient(), 
                emailData.getSubject(), 
                emailData.getBody()
            );

            if(response == null){
                log.warn("Error trying to get server response");
                return false;
            }

            if(response.getCode() != 250){
                log.warn( "Failed to send email. Response code: {}", response.getCode());
                return false;
            }
        }catch(IOException e) {
            log.error(
                "IOException occurred while sending the email to:{}", 
                emailData.getRecipient(),
                e
            );
            return false;
        }finally{
            try {
                if(session != null){
                    session.close();
                    log.info("Session closed");
                }
            }catch(IOException e){
                log.error("An error occurred while closing the session", e);
            }
        }

        log.info("Email sended successfully");
        return true;
    }

    public EmailData getEmailData(){
        return emailData;
    }
}