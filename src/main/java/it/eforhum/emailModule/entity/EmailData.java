package it.eforhum.emailModule.entity;

import it.eforhum.emailModule.interfaces.MSGData;
import lombok.Data;

@Data
public class EmailData implements MSGData {

    public static final String SENDER = System.getenv("GMAIL_ACCOUNT");
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

    public static boolean isRecipientValid(String recipient){
        return recipient.contains("@") && recipient.contains(".");
    }
    
}
