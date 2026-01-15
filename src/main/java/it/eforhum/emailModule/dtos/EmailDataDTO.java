package it.eforhum.emailModule.dtos;

public record EmailDataDTO (
    String dest, 
    String subject, 
    String body
){}
