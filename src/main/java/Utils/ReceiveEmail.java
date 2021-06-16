package Utils;


import lombok.extern.slf4j.Slf4j;

import static com.jayway.awaitility.Awaitility.await;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.mail.*;
import javax.mail.search.SubjectTerm;


@Slf4j
public class ReceiveEmail {

  private static String extractedText;

  private static final String PROTOCOL = "mail.store.protocol";

  private static final String IMAPS = "imaps";

  private static final String SMTP = "imap.gmail.com";

  String subjectValue = "";

  String folderName = "INBOX";


  public ReceiveEmail () {

  }


  /**
   * <p>
   * Provided you input a valid gmail account, this method will retrieve the last unread email and
   * and an activation link;
   * <p>
   * Please amend the split statement as desired, in order to retrieve the expected text, in accordance
   * with the format of the email.
   * <p>
   * For instance, the below split will search for the link between "Visit" and "complete the
   * procees":
   * <p>
   * String prior = "Visit "; String after = " to complete the procees"; " expectedText =
   * buffer.toString().split(prior)[1].split(after)[0] "
   * <p>
   * <p>
   * The below parameters should be provided in tests:
   * <p>
   * If you wish to delete the email after retrieving the link, set delete_flag to "true":
   */

  public String getText (
      String gmailAddress, String password,
      String subject,
      String prior, String after, String deleteFLAG
  ) {

    boolean isMailFound = false;
    Message mailFromSender = null;
    Store store = null;
    Folder folder = null;
    int contor = 0;

    try {
      do {
        contor++;
        log.info("" + contor);

        store = connect(gmailAddress, password);

        if (store != null) {
          folder = store.getFolder(this.folderName);
          folder.open(Folder.READ_WRITE);

          log.info(" >> Total Messages: " + folder.getMessageCount());
          log.info(" >> Unread Message: " + folder.getUnreadMessageCount());

          Message[] messages;

          /* Search for particular email, with a given Subject */

          log.info(" >> Searching emails based on given Subject: " + subject);
          messages = folder.search(new SubjectTerm(subject), folder.getMessages());

          await()
              .atMost(30, TimeUnit.SECONDS)
              .pollInterval(10, TimeUnit.SECONDS)
              .until(() -> messages.length > 0);

          /* Search for an UNREAD email with the given Subject */
          log.info(
              "Waiting for the expected text to be retrieved from Gmail. ");

          for (Message mail : messages) {

            if (!mail.isSet(Flags.Flag.SEEN)) {
              mailFromSender = mail;
              log.info(" >> EVRIKA! Found UNREAD message. Message Count is: "
                           + mailFromSender.getMessageNumber());
              isMailFound = true;
            }
          }
          if (!isMailFound) {

            folder.close(true);
            store.close();
          }
        }

      } while (contor < 100 && (mailFromSender == null));

      /* Test fails if no UNREAD email was found with the Given Subject */

      if (!isMailFound) {
        throw new IllegalArgumentException(
            "Could not find an UNREAD MESSAGE with given SUBJECT. Please verify if the EXCHANGE SERVER is working properly");

      } else return extractText(mailFromSender, prior, after, deleteFLAG);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return "";
  }


  private String extractText (Message mailFromSender, String prior, String after, String deleteFLAG) {

    String line;
    StringBuilder buffer = new StringBuilder();
    InputStream temp = null;
    try {
      temp = mailFromSender.getInputStream();

      if (temp != null) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(temp));

        while (null != (line = reader.readLine())) {
          buffer.append(line);
        }
        log.info("" + buffer);

        /* Splitting the email to retrieve the expected text */

        extractedText = buffer.toString().split(prior)[1].split(after)[0];
        log.info(extractedText);
        if (deleteFLAG == "true") {

          mailFromSender.setFlag(Flags.Flag.DELETED, true);
          log.info("Message has been deleted");

        } else {
          log.info("Message has NOT been deleted");
        }
        reader.close();

      }
    } catch (IOException | MessagingException e) {
      log.info(e.getMessage());
      return "";
    }
    return extractedText;
  }


  /**
   * Method is used for deleting emails from Gmail account given as parameter, with a specific
   * subject (or all for '*')
   *
   * @param filter : filter.put(subject,value), (subject=*. deletes all mail),
   *               filter.put(folder,name of the folder), filter.put ("folder","INBOX") deletes from Inbox.
   *               Other folders may be used as well No verifications made on folder existence.
   * @return bool
   */
  public boolean deleteEmailsFromGmailAccount (
      String gmailAddress, String password,
      Map<String, String> filter
  ) {


    List<Message> arrayMessages;
    boolean isMailFound = false;
    String subject = filter.get("subject");
    String folder = filter.get("folder");
    Message message = null;
    Store store = null;
    Folder mailFolder = null;

    log.info("DELETING EMAILS FROM " + gmailAddress + " with the subject : " + filter);
    try {

      while (message == null) {

        store = connect(gmailAddress, password);
        if (store != null) {
          mailFolder = store.getFolder(folder);
          if (mailFolder != null) {
            mailFolder.open(Folder.READ_WRITE);

            arrayMessages = Arrays.asList(mailFolder.getMessages());

            for (int p = arrayMessages.size() - 1; p >= 0; p--) {
              message = arrayMessages.get(p);
              this.subjectValue = message.getSubject();
              if (this.subjectValue.isEmpty()) this.subjectValue = "subject = null";
              if (this.subjectValue.contains(subject) || this.subjectValue.matches(subject)) {
                message.setFlag(Flags.Flag.DELETED, true);
                log.info("Marked DELETE for message: " + subject);
                isMailFound = true;
              }
            }
            mailFolder.close(true);
          }
          store.close();
        }
      }
    } catch (NullPointerException | MessagingException e) {
      log.error(e.getMessage());

    }
    return isMailFound;
  }


  private Store connect (String email, String password) throws MessagingException {

    Store store = null;
    try {
      log.info("Opening the connection for custom email - " + email);
      Properties properties = System.getProperties();
      properties.setProperty(PROTOCOL, IMAPS);

      Session session = Session.getInstance(properties);
      store = session.getStore(IMAPS);
      store.connect(SMTP, email, password);
      log.info("Connection ready for use");

    } catch (Exception e) {
      log.info(e.getMessage());
      throw new MessagingException("Messaging exception.");
    }
    return store;
  }

}