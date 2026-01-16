package it.eforhum.emailModule.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.colozzacristian.SmtpConnection;
import com.github.colozzacristian.SmtpConnectionBuilder;
import com.github.colozzacristian.SmtpException;
import com.github.colozzacristian.SmtpResponse;
import com.github.colozzacristian.SmtpSession;

import it.eforhum.emailModule.entity.EmailData;
import it.eforhum.emailModule.interfaces.Sendable;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class EmailSenderService implements Sendable{
    
    private static final String SMTP_SERVER = "smtp.gmail.com";

    
    @Value("${gmail.port}")
    private Integer port;

    @Value("${gmail.app.password}")
    private String password;

    @Value("${gmail.account}")
    private String user;

    private EmailData emailData;
    private SmtpSession session;

    public EmailSenderService(){}

    @Override
    public boolean sendMessage(EmailData emailData){
        try {
            SmtpConnection connection = SmtpConnectionBuilder.connectSSL("smtp.gmail.com", port, "localhost");
            
            session = connection.createSession(user, password);

            SmtpResponse response = session.sendMail(
                EmailData.SENDER,
                emailData.getRecipient(), 
                emailData.getSubject(), 
                emailData.getBody()
            );

            if(response.getCode() != 250){
                log.warn( "Failed to send email. Response code: %d", response.getCode());
                return false;
            }
        }catch(SmtpException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                if(session != null){
                    session.close();
                    log.info("Session closed");
                }
            }catch(SmtpException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return true;
    }

    public EmailData getEmailData(){
        return emailData;
    }
}