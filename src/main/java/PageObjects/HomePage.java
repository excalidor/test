package PageObjects;


import BasePages.BasePage;
import BasePages.IPageIdentity;
import Enums.TextComparison;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;


@Slf4j
public class HomePage extends BasePage implements IPageIdentity<BasePage> {

  private static List<WebElement> addToCartButtons;

  private static List<WebElement> removeFromCartButtons;

  private static List<WebElement> detailedViewProducts;

  private static final String URL = "https://www.saucedemo.com/inventory.html";

  private static final String HOME_PAGE_ELEMENT = "//*[@id=\"shopping_cart_container\"]/a";


  public HomePage () {

    assertOnPage();
  }


  public boolean checkPageIdentity () {

    log.info("Checking home page identity...");

    return driver.getTitle().equals(HOME_PAGE_TITLE)
        && driver.getCurrentUrl().equals(URL);
  }


  public boolean checkPageElements () {

    log.info("Checking home page elements...");

    return elementChecks.checkIfElementDisplayed(HOME_PAGE_ELEMENT);
  }


  public HomePage checkAddToCartFromCatalogView () {

    addToCartButtons = driver.findElements(By.xpath("//*[contains(@id,'add-to-cart')]"));
    for (WebElement el : addToCartButtons) {
      elementActions.clickOn(el);
    }
    removeFromCartButtons = driver.findElements(By.xpath("//*[contains(@id,'remove-')]"));

    Assert.assertTrue(
        "The number of buttons to remove products is different than expected",
        addToCartButtons.size() == removeFromCartButtons.size()
    );

    Assert.assertTrue(
        "The basket size is not displayed correctly.",
        elementChecks.checkElementText(
            By.xpath("//*[@id=\"shopping_cart_container\"]/a/span"),
            "" + addToCartButtons.size(),
            TextComparison.EQUALS
        )
    );

    return new HomePage();
  }


  public HomePage checkRemoveFromCartInCatalogView () {

    for (WebElement el : removeFromCartButtons) {
      elementActions.clickOn(el);
    }
    addToCartButtons = driver.findElements(By.xpath("//*[contains(@id,'add-to-cart')]"));
    Assert.assertTrue(
        "The number of buttons to add to cart products is different than expected",
        addToCartButtons.size() == removeFromCartButtons.size()
    );

    Assert.assertTrue(
        "Element should not be displayed.",
        elementChecks.checkIfElementNotDisplayed(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span"))
    );

    return new HomePage();
  }


  public HomePage checkAddRemoveFromDetailView () {

    detailedViewProducts = driver.findElements(By.xpath("//*[contains(@class,'inventory_item_img')]"));

    for (int i = 0; i < detailedViewProducts.size(); i++) {
      List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@class,'inventory_item_img')]"));
      elementActions.clickOn(elements.get(i));
      elementActions.clickOn(driver.findElement(By.xpath("//*[contains(@id,'add-to-cart')]")));
      elementChecks.checkElementText(
          By.xpath("//*[@id=\"shopping_cart_container\"]/a/span"),
          "1",
          TextComparison.EQUALS
      );
      elementActions.clickOn(driver.findElement(By.xpath("//*[contains(@id,'remove-')]")));
      Assert.assertTrue(
          "Element should not be displayed.",
          elementChecks.checkIfElementNotDisplayed(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span"))
      );
      elementActions.clickOn(driver.findElement(By.xpath("//*[contains(@id,'back-to-products')]")));
    }
    return new HomePage();
  }


  public HomePage checkRemoveFromCartPage () {

    addToCartButtons = driver.findElements(By.xpath("//*[contains(@id,'add-to-cart')]"));
    for (WebElement el : addToCartButtons) {
      elementActions.clickOn(el);
    }
    Assert.assertTrue(
        "The basket size is not displayed correctly.",
        elementChecks.checkElementText(
            By.xpath("//*[@id=\"shopping_cart_container\"]/a/span"),
            "" + addToCartButtons.size(),
            TextComparison.EQUALS
        )
    );
    elementActions.clickOn(By.id("shopping_cart_container"));
    List<WebElement> listOfRemovalButtons = driver.findElements(By.xpath("//*[contains(@id,'remove')]"));

    Assert.assertTrue(
        "The basket size is not displayed correctly.",
        addToCartButtons.size() == listOfRemovalButtons.size()
    );
    WebElement element;
    do {
      element = null;
      if (elementChecks.checkIfElementDisplayed(By.xpath("//*[contains(@id,'remove')]")))
        element = driver.findElement(By.xpath("//*[contains(@id,'remove')]"));
      if (element != null) elementActions.clickOn(element);
    } while (element != null);
    Assert.assertTrue(
        "Element should not be displayed.",
        elementChecks.checkIfElementNotDisplayed(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span"))
    );
    elementActions.clickOn(driver.findElement(By.xpath("//*[contains(@id,'continue-shopping')]")));
    return new HomePage();
  }


  public HomePage checkCompleteCheckout () {

    addToCartButtons = driver.findElements(By.xpath("//*[contains(@id,'add-to-cart')]"));
    for (int i = 0; i < addToCartButtons.size(); i++) {
      List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@id,'add-to-cart')]"));
      elementActions.clickOn(elements.get(i));
      elementActions.clickOn(By.id("shopping_cart_container"));
      elementActions.clickOn(driver.findElement(By.xpath("//*[contains(@id,'checkout')]")));
      elementActions.clearAndTypeText(By.id("first-name"), "xxx");
      elementActions.clearAndTypeText(By.id("last-name"), "yyy");
      elementActions.clearAndTypeText(By.id("postal-code"), "777");
      elementActions.clickOn(By.id("continue"));
      elementActions.clickOn(By.id("finish"));
      elementActions.clickOn(By.id("back-to-products"));
    }

    return new HomePage();
  }
}
