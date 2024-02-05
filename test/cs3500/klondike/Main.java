package cs3500.klondike;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Main class to play the klondike game.
 */
public class Main {

  /**
   * Creates a model, deck, controller, and plays the game with user input.
   * @param args arguments passed to main
   */
  public static void main(String[] args) {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck();
    TestUtils testUtils = new TestUtils();
    KlondikeController controller =
            new KlondikeTextualController(new InputStreamReader(System.in), System.out);
    controller.playGame(model, deck, true, 7, 3);
  }

}
