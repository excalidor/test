package Utils;


import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static Utils.ColourCodes.BLACK;
import static Utils.ColourCodes.BLUE;
import static Utils.ColourCodes.RED;


@Slf4j
public class DriverSetup {

  public static WebDriver driver;

  public Properties properties;

  public static ElementActions elementActions;

  public static ElementChecks elementChecks;

  protected static int timeout = 30;

  protected static Wait<WebDriver> wait;

  @BeforeTest
  public void setUp () {

    log.info(BLACK + "Setup driver starting...");
    if (System.getProperty("os.name").contains("Windows")) {
      System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
    } else {
      System.setProperty("webdriver.chrome.driver", "resources/chromedriver");
    }
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    properties = loadProperties();
    wait = new WebDriverWait(driver, timeout);
    elementActions = new ElementActions();
    elementChecks = new ElementChecks();
    log.info("Setup driver done...");
  }


  public Properties loadProperties () {

    Properties prop = new Properties();

    try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
      prop.load(input);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    log.info("Properties loaded....");
    return prop;
  }


  @AfterTest
  public void tearDown () {

    log.info(BLACK + "Driver tear down...");
//    driver.quit();
  }


  /**
   * Please no longer use this explicit wait (we look to deprecate it)
   *
   * @param millisecs the length of the pause, in milliseconds
   */
  public void waitTime (int millisecs) {


    log.debug(BLUE + "Waiting " + (double) millisecs / 1000 + " seconds.");
    try {
      Thread.sleep(millisecs);
    } catch (InterruptedException e) {
      log.error(RED + String.valueOf(e), driver);
      Thread.currentThread().interrupt();
    }
  }

}