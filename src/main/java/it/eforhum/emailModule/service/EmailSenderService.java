package it.eforhum.emailModule.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.colozzacristian.SmtpConnection;
import com.github.colozzacristian.SmtpConnectionBuilder;
import com.github.colozzacristian.SmtpException;
import com.github.colozzacristian.SmtpResponse;
import com.github.colozzacristian.SmtpSession;

import it.eforhum.emailModule.entity.EmailData;
import it.eforhum.emailModule.interfaces.Sendable;


@Service
public class EmailSenderService implements Sendable{
    
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private final Integer PORT = System.getenv("GMAIL_PORT") != null ? Integer.valueOf(System.getenv("GMAIL_PORT")) : 465;
    
    @Value("${gmail.app.password}")
    private String PASSWORD;

    @Value("${gmail.account}")
    private String USER;

    private static final Logger logger = Logger.getLogger(EmailSenderService.class.getName());

    private EmailData emailData;
    private SmtpSession session;
    private SmtpConnection conncetion;

    public EmailSenderService(){}

    public EmailSenderService(EmailData emailData){
        this.emailData = emailData;
    }

    @Override
    public boolean sendMessage(){
        try {
            conncetion = SmtpConnectionBuilder.connectSSL(SMTP_SERVER, PORT, "localhost");
            
            session = conncetion.createSession(USER, PASSWORD);

            SmtpResponse response = session.sendMail(
                EmailData.SENDER,
                emailData.getRecipient(), 
                emailData.getSubject(), 
                emailData.getBody()
            );

            if(response.getCode() != 250){
                logger.log(Level.WARNING, "Failed to send email. Response code: " + response.getCode());
                return false;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            try {
                session.close();
                logger.log(Level.INFO, "Session closed");
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