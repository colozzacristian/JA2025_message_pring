package it.eforhum.emailModule.interfaces;

import it.eforhum.emailModule.entity.EmailData;

public interface Sendable{
    public abstract boolean sendMessage(EmailData emailData);
}