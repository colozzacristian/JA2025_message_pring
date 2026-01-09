package it.eforhum.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.colozzacristian.SmtpConnection;
import com.github.colozzacristian.SmtpConnectionBuilder;
import com.github.colozzacristian.SmtpException;
import com.github.colozzacristian.SmtpResponse;
import com.github.colozzacristian.SmtpSession;

import io.github.cdimascio.dotenv.Dotenv;
import it.eforhum.classes.EmailData;
import it.eforhum.interfaces.Sendable;

public class EmailSender implements  Sendable{
    
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private final Integer PORT = Integer.valueOf(Dotenv.load().get("GMAIL_PORT"));

    private final String PASSWORD = Dotenv.load().get("GMAIL_APP_PASSWORD");
    private final String USER = Dotenv.load().get("GMAIL_ACCOUNT");

    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    private EmailData emailData;
    private SmtpSession session;
    private SmtpConnection conncetion;

    public EmailSender(){}

    public EmailSender(EmailData emailData){
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

            if(response.getCode() == 250){ // success code
                logger.log(Level.INFO,"Email sended successfully");
                return true;
            }else{
                System.out.println(response.getCode());
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

        return false;
    }

    public EmailData getEmailData(){
        return emailData;
    }
}
