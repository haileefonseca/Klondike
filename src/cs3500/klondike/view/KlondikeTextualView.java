package cs3500.klondike.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the cs3500.klondike.Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private Appendable appendable;

  /**
   * a constructor for the view that takes in only a model (only toString would work).
   * @param model the basicKlondike game to convert String
   */
  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
  }

  /**
   * A constructor for the view that takes in a model of the game and an appendable, which
   * it can render the current game state onto.
   * @param model the model to be rendered
   * @param ap the appendable to render the output to
   */
  public KlondikeTextualView(KlondikeModel model, Appendable ap) {
    this.model = model;
    this.appendable = ap;
  }

  @Override
  public void render() throws IOException {
    this.appendable.append(this.toString());
  }



  /**
   * Prints out this game of cs3500.klondike.Klondike, using {@code<none>}
   * to represent nonexistent features, and
   * ? to represent face down cards.
   */
  public String toString() {
    String result = "Draw: ";
    if (!(this.model.getDrawCards().isEmpty())) {
      for (int card = 0; card < this.model.getDrawCards().size(); card++) {
        if (card > 0 && card < this.model.getDrawCards().size()) {
          result += ", ";
        }
        result += this.model.getDrawCards().get(card).toString();
      }
    }
    result += "\nFoundation: ";
    for (int foundPile = 0; foundPile < this.model.getNumFoundations(); foundPile++) {
      if (this.model.getCardAt(foundPile) == null) {
        result += "<none>";
      }
      else {
        result += this.model.getCardAt(foundPile).toString();
      }
      if (foundPile < this.model.getNumFoundations() - 1) {
        result += ", ";
      }
    }
    result += "\n";
    List<List<String>> toPrint = convertToArray();
    for (int row = 0; row < this.model.getNumRows(); row ++) {
      for (int stack = 0; stack < this.model.getNumPiles(); stack++) {
        if (toPrint.get(stack).get(row).length() == 2) {
            result += " " + toPrint.get(stack).get(row);
        }
        else {
          result += toPrint.get(stack).get(row);
        }
      }
      if (row < this.model.getNumRows() - 1) {
        result += "\n";
      }
    }
    return result;
  }


  // represents the cards in this model as a 2D array
  private List<List<String>> convertToArray() {
    List<List<String>> result = new ArrayList<>();
    for (int pile = 0; pile < this.model.getNumPiles(); pile ++) {
      result.add(new ArrayList<String>());
    }
    for (int row = 0; row < this.model.getNumRows(); row++) {
      for (int stack = 0; stack < this.model.getNumPiles(); stack++) {
        boolean emptyRow = false;
        if (this.model.getPileHeight(stack) == 0) {
          if (row == 0) {
            result.get(stack).add("  X");
            emptyRow = true;
          }
          else if (row < this.model.getNumRows()) {
            result.get(stack).add("   ");
            emptyRow = true;
          }
        }
        try {
          result.get(stack).add(this.model.getCardAt(stack, row).toString());
        } catch (IllegalArgumentException argumentException) {
          if (argumentException.getMessage().contains("Card not visible") && !emptyRow) {
            result.get(stack).add("  ?");
          }
          else if (!emptyRow) {
            result.get(stack).add("   ");
          }
        }
      }
    }
    return result;
  }
}
