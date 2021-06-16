package Models;


import lombok.Data;


@Data
public class Email {

  private String from;

  private String to;

  private String subject;

  private String host;

  private String smtpPort;

  private String content;

  private String attachment;

  private String user;

  private String password;

}
