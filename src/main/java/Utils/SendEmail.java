package Utils;


import Models.Email;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Slf4j
public class SendEmail {

  public boolean sendEmail (Email email) {

    // Get system properties
    Properties properties = System.getProperties();

    // Setup mail server
    properties.put("mail.smtp.host", email.getHost());
    properties.put("mail.smtp.port", "465");
    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");

    // Get the Session object.// and pass
    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

      protected PasswordAuthentication getPasswordAuthentication() {

        return new PasswordAuthentication(email.getUser(), email.getPassword());

      }

    });
    //session.setDebug(true);
    try {
      // Create a default MimeMessage object.
      MimeMessage message = new MimeMessage(session);

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(email.getFrom()));

      // Set To: header field of the header.
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));

      // Set Subject: header field
      message.setSubject(email.getSubject());

      Multipart multipart = new MimeMultipart();

      MimeBodyPart attachmentPart = new MimeBodyPart();

      MimeBodyPart textPart = new MimeBodyPart();

      try {
        if (null != email.getAttachment()) {
          File f = new File(email.getAttachment());
          attachmentPart.attachFile(f);
          multipart.addBodyPart(attachmentPart);
        }
        textPart.setText(email.getContent());
        multipart.addBodyPart(textPart);

      } catch (IOException e) {

        log.error(e.getMessage());
        e.printStackTrace();

      }

      message.setContent(multipart);

      log.info("sending email...");
      // Send message
      Transport.send(message);
      log.info("Successfully sent message....");
      return true;
    } catch (MessagingException mex) {
      log.error(mex.getMessage());
      mex.printStackTrace();
      return false;
    }

  }

}
