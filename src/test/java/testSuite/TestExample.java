package testSuite;


import BasePages.BasePage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;


@Slf4j
public class TestExample extends BasePage {

  @Test
  public void sauceLabFunctionalTest () {

    navigateToBasePage()
        .login()
        .checkAddToCartFromCatalogView()
        .checkRemoveFromCartInCatalogView()
        .checkAddRemoveFromDetailView()
        .checkRemoveFromCartPage()
        .checkCompleteCheckout();
  }
}
