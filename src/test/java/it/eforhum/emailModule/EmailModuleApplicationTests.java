package it.eforhum.emailModule;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.colozzacristian.SmtpConnection;
import com.github.colozzacristian.SmtpConnectionBuilder;
import com.github.colozzacristian.SmtpException;
import com.github.colozzacristian.SmtpResponse;
import com.github.colozzacristian.SmtpSession;

@SpringBootTest
class EmailModuleApplicationTests {

    @Value("${gmail.port}")
    private Integer port;

    @Value("${gmail.app.password}")
    private String password;

    @Value("${gmail.account}")
    private String user;

    @Test
    void contextLoads() {
        assertNotNull(user);
        assertNotNull(password);
        assertNotNull(port);
    }

    @Test
    void sendEmail() {
        SmtpConnection connection = null;
        SmtpSession session = null;

        try {
            connection = SmtpConnectionBuilder.connectSSL("smtp.gmail.com", port, "localhost");
            session = connection.createSession(user, password);

            SmtpResponse response = session.sendMail(
                user, 
                "andrearomanato06@gmail.com", 
                "TEST", 
                "<a href=\"https://google.com\">test</a>"
            );
            
            assertEquals(250, response.getCode(), "Email should be sent successfully. Response: " + response.getCode());

        } catch(SmtpException ex) {
            ex.printStackTrace();
            fail("SMTP Exception: " + ex.getMessage());
        } catch(IOException e) {
            e.printStackTrace();
            fail("IO Exception: " + e.getMessage());
        } finally {
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}