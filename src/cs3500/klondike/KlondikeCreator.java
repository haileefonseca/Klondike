package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Class to create KlondikeModels based on currently implemented options.
 */
public class KlondikeCreator {

  /**
   * enum representation of all currently playable versions of Klondike.
   */
  public enum GameType {
    BASIC(), LIMITED(), WHITEHEAD();
  }

  // creates a KlondikeModel based on the given GameType
  static KlondikeModel create(GameType type) {
    if (type == GameType.LIMITED) {
      return new LimitedDrawKlondike(2);
    }
    if (type == GameType.WHITEHEAD) {
      return new WhiteheadKlondike();
    }
    if (type == GameType.BASIC) {
      return new BasicKlondike();
    }
    else {
      throw new IllegalArgumentException("Invalid game type");
    }
  }

  // creates a limited draw with the given number of redraws
  static KlondikeModel createCustomLimited(GameType type, int numDraw) {
    if (type == GameType.LIMITED) {
      return new LimitedDrawKlondike(numDraw);
    }
    else {
      throw new IllegalArgumentException();
    }
  }


}
