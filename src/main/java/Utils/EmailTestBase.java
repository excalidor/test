package Utils;


import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Slf4j
public class EmailTestBase {

  public Properties properties = loadProperties();


  public Properties loadProperties () {

    Properties properties = new Properties();
    try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
      properties.load(input);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    log.info("Properties loaded....");
    return properties;
  }

}
