package Utils;


import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;


@Slf4j
public class ElementActions extends DriverSetup {

  public ElementActions () {

    super();
  }


  public void clickOn (WebElement element) {

    log.info("Clicking on: " + element);

    try {
      element.click();
    } catch (Exception e) {
      throw e;
    }
  }


  /**
   * Perform a left mouse click on the provided element
   *
   * @param locator locator of the element to be clicked
   */
  public void clickOn (By locator) {

    wait.until(visibilityOfElementLocated(locator));
    clickOn(driver.findElement(locator));
  }


  public void clearAndTypeText (By locator, String content) {

    clearText(locator);
    typeText(locator, content);
  }


  /**
   * Types in the provided text into the provided webpage element
   *
   * @param locator selector of the element to receive the text
   * @param content text to be typed in
   */
  public void typeText (By locator, String content) {

    log.info("Typing \"" + content + "\" \n\tinto " + locator.toString());

    try {
      wait.until(elementToBeClickable(locator));
      driver.findElement(locator).sendKeys(content);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }


  /**
   * Types in the provided password into the provided webpage element, without showing it in the
   * logs
   *
   * @param locator selector of the element to receive the text
   * @param content text to be typed in
   */
  public void typePassword (By locator, String content) {

    // we don't want passwords displayed in the logs
    log.info(
        "Typing the provided password, '" + content.substring(0, 2) + "***' \n\tinto "
            + locator.toString());

    try {
      wait.until(elementToBeClickable(locator));
      driver.findElement(locator).sendKeys(content);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

  }


  /**
   * Clears all text from an element
   *
   * @param locator selector of the element to be cleared
   */
  public void clearText (By locator) {

    log.info("Clearing text from " + locator.toString());

    try {
      wait.until(elementToBeClickable(locator));
      driver.findElement(locator).clear();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}