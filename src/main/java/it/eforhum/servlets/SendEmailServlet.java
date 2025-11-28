package it.eforhum.servlets;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eforhum.classes.EmailData;
import it.eforhum.dtos.EmailDataDTO;
import it.eforhum.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name="SendEmailServlet", urlPatterns = "/send/email")
public class SendEmailServlet extends HttpServlet{

    ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        response.setContentType("application/json");
        
        EmailData emailData = null;

        String body = new String(request.getInputStream().readAllBytes());

        EmailDataDTO emailDTO = mapper.readValue(body, EmailDataDTO.class);
        
        String recipient = emailDTO.dest(); // recipient

        if(EmailData.isRecipientValid(recipient)){
            emailData = new EmailData(
                recipient,
                emailDTO.body(),
                emailDTO.subject()
            );
        }else{
            response.setStatus(400);
            response.getWriter().write("Invalid email format");
        }
        
        // create the email sender with all the datas to send with the email requested
        EmailSender sender = new EmailSender(emailData);

        if(sender.sendMessage()){
            response.setStatus(200);
        }else{
            response.setStatus(400);
        }
    }   
}