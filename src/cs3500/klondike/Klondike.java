package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;

import static java.lang.Integer.parseInt;

/**
 * Class to start a playable game of Klondike, through running the main method.
 */
public final class Klondike {
  static int workingIndex = 0;

  /**
   * Function that allows users to play Klondike through command-line input.
   * @param args a list of strings made from the input
   */
  public static void main(String[] args) {
    try {
      String anything = args[0];
    }
    catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("No inputs");
    }
    KlondikeModel model = createGame(args);
    int numPiles = 7;
    int numDraw = 3;
    try {
      numPiles = parseInt(args[workingIndex]);
      try {
        workingIndex ++;
        numDraw = parseInt(args[workingIndex]);
      } catch (IndexOutOfBoundsException ignored) {
      }
    } catch (IndexOutOfBoundsException ignored2) {
    }
    if (hasNegatives(numPiles) || hasNegatives(numDraw)) {
      System.out.print("Invalid number of piles");
      return;
    }
    KlondikeController controller = new KlondikeTextualController(new InputStreamReader(System.in),
            System.out);
    controller.playGame(model, model.getDeck(),true, numPiles, numDraw);
  }

  // checks a given argument isn't negative
  private static boolean hasNegatives(int arg) {
    return arg <= 0;
  }


  // uses args to attempt to return a model
  private static KlondikeModel createGame(String [] args) {
    workingIndex = 0;
    String userInput = args[workingIndex];
    workingIndex++;
    switch (userInput) {
      case "whitehead":
        return KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
      case "limited":
        try {
          int draw = parseInt(args[1]);
          if (Float.isNaN(draw)) {
            throw new IllegalArgumentException("Must give limited a  valid number");
          }
          if (draw < 0) {
            throw new IllegalArgumentException("Cannot have a negative amount of redraws");
          }
          workingIndex++;
          return KlondikeCreator.createCustomLimited(KlondikeCreator.GameType.LIMITED, draw);
        } catch (IndexOutOfBoundsException e) {
          throw new IllegalArgumentException("Limited must be given a number of redraws");
        }
      case "basic":
        return KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }
}
