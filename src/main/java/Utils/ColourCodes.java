package Utils;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class ColourCodes {

  public static final String BLACK = "\033[30m";

  public static final String RED = "\033[31m";

  public static final String GREEN = "\033[32m";

  public static final String YELLOW = "\033[33m";

  public static final String BLUE = "\033[34m";

  public static final String MAGENTA = "\033[35m";

  public static final String CYAN = "\033[36m";

  public static final String WHITE = "\033[37m";


  private ColourCodes () {

    log.error(RED + "You should not instantiate this class.");
  }
}
