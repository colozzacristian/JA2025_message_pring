package it.eforhum;

import static org.junit.Assert.fail;
import org.junit.Test;

import com.github.colozzacristian.SmtpConnection;
import com.github.colozzacristian.SmtpConnectionBuilder;
import com.github.colozzacristian.SmtpResponse;
import com.github.colozzacristian.SmtpSession;

import io.github.cdimascio.dotenv.Dotenv;
/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void sendsEmail() {

        SmtpConnection c;
        Integer port = Integer.valueOf(Dotenv.load().get("GMAIL_PORT"));

        String password = Dotenv.load().get("GMAIL_APP_PASSWORD");
        String user = Dotenv.load().get("GMAIL_ACCOUNT");
        
        try {
            c = SmtpConnectionBuilder.connectSSL("smtp.gmail.com", port , "localhost");
            SmtpSession session = c.createSession(user, password);

            SmtpResponse response = session.sendMail(user, user, "TEST", "<a href=\"google.com\">test</a>");

            if(response.getCode() != 250){
                fail();
            }
            
        } catch (Exception e) {
            fail();
        }


        
    }
}
