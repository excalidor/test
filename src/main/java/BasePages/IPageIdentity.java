package BasePages;


import static org.testng.AssertJUnit.assertTrue;


public interface IPageIdentity<T> {

  /**
   * checks if we're on the correct page (URL, title, meta description and any other page
   * identifiers)
   */
   default public  boolean checkPageElements(){
     return false;
   }

  /**
   * runs any other methods that check the main sections of the page
   */
  public boolean checkPageIdentity();


  default T assertOnPage() {
    assertTrue("Page identity check failed", checkPageIdentity());
    assertTrue("Page elements check failed", checkPageElements());

    return (T) this;
  }
}