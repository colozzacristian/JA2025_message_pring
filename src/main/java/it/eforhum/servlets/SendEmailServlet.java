package it.eforhum.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(SendEmailServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException{

        response.setContentType("application/json");
        
        EmailData emailData = null;
        String body = null;
        EmailDataDTO emailDTO = null;

        try {
            body = new String(request.getInputStream().readAllBytes());
            emailDTO= mapper.readValue(body, EmailDataDTO.class);
        } catch (IOException e) {
            response.setStatus(400);
            logger.log(Level.WARNING, "Invalid email data");
        }
        
        String recipient = emailDTO.dest(); // recipient

        if(!EmailData.isRecipientValid(recipient)){
            try {
                response.setStatus(400);
                logger.log(Level.WARNING, "Invalid email format");
                return;
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        emailData = new EmailData(
            recipient,
            emailDTO.body(),
            emailDTO.subject()
        );

        
        // create the email sender with all the datas to send with the email requested
        EmailSender sender = new EmailSender(emailData);

        if(sender.sendMessage()){
            response.setStatus(200);
        }else{
            response.setStatus(400);
        }
    }   
}