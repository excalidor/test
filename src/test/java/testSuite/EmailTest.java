package testSuite;


import Models.Email;
import Utils.EmailTestBase;
import Utils.ReceiveEmail;
import Utils.SendEmail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class EmailTest extends EmailTestBase {


  @Test
  public void emailSendTest () {


    Email email = new Email();
    email.setFrom("testqateam2020@gmail.com");
    email.setTo("testqateam2020@gmail.com");
    email.setHost("smtp.gmail.com");
    email.setSubject("Test email sent from java application");
    email.setUser(properties.getProperty("gmailUser"));
    email.setPassword(properties.getProperty("gmailPass"));
    email.setContent("Dummy Content....");
    email.setAttachment("src/test/testData/mfa.png");
    SendEmail sendEmail = new SendEmail();
    ReceiveEmail receiveEmail = new ReceiveEmail();
    Assert.assertTrue("Email not sent.", sendEmail.sendEmail(email));
    Map<String, String> filter = new HashMap<>();
    filter.put("subject", email.getSubject());
    filter.put("folder", "INBOX");
    receiveEmail.deleteEmailsFromGmailAccount(
        properties.getProperty("gmailUser"),
        properties.getProperty("gmailPass"),
        filter
    );
  }


  @Test
  public void emailReceiveTest () {

    Email email = new Email();
    email.setFrom("testqateam2020@gmail.com");
    email.setTo("testqateam2020@gmail.com");
    email.setHost("smtp.gmail.com");
    email.setSubject("Test email sent from java application");
    email.setUser(properties.getProperty("gmailUser"));
    email.setPassword(properties.getProperty("gmailPass"));
    email.setContent("Dummy Content....");
    SendEmail sendEmail = new SendEmail();
    Assert.assertTrue("Email not sent.", sendEmail.sendEmail(email));
    ReceiveEmail receivedMail = new ReceiveEmail();
    String receivedText = receivedMail.getText(
        properties.getProperty("gmailUser"),
        properties.getProperty("gmailPass"),
        email.getSubject(),
        "7bit",
        "------=_Part",
        "true"
    );
    Assert.assertTrue(receivedText.contains(email.getContent()));
  }

}
