package it.eforhum.classes;

import io.github.cdimascio.dotenv.Dotenv;
import it.eforhum.interfaces.MSGData;

public class EmailData implements MSGData {

    public static final String SENDER = Dotenv.load().get("testprojectwork2025@gmail.com");
    private String recipient;
    private String body;
    private String subject;
    
    public EmailData(){}

    public EmailData(String recipient, String body, String subject){
        this.recipient = recipient;
        this.body = body;
        this.subject = subject;
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    public String getSubject(){
        return this.subject;
    }

    public static boolean isRecipientValid(String recipient){

        if(recipient.contains("@") && recipient.contains(".")){
            return true;
        }

        return false;
    }
    
}
