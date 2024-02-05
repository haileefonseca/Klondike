package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.AKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

import java.util.List;

/**
 * Class representing a game of LimitedDrawKlondike. Follows all the same rules as an Abstract
 * Klondike, except there is a limited number of times each card may appear in the draw pile.
 * Once that is reached, the card will be permanently discarded. Overrides necessary methods from
 * AKlondike that deal with draw cards to implement this.
 */
public class LimitedDrawKlondike extends AKlondike implements KlondikeModel {
  int numTimesRedrawAllowed;
  int sizeOfDraw;
  int cardRecycled;

  /**
   *  creates a game of LimitedDraw with a given number of times the draw cards are allowed to be
   *  cycled through.
   * @param numTimesRedrawAllowed int, number of times each card can be redrawn before it is
   *                              permanently discarded.
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();
    if (numTimesRedrawAllowed >= 0) {
      this.numTimesRedrawAllowed = numTimesRedrawAllowed;
    }
    else {
      throw new IllegalArgumentException("Cannot redraw a negative number of times");
    }
    this.cardRecycled = 0;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    super.startGame(deck, shuffle, numPiles, numDraw);
    this.sizeOfDraw = this.currentDraw.size() + this.restOfDraw.size();
  }

  @Override
  protected void refreshDraw() {
    for (int draw = 0; draw < maxDraw - this.currentDraw.size(); draw++) {
      if (!(this.restOfDraw.isEmpty())) {
        this.currentDraw.add(this.restOfDraw.remove(0));
        if (sizeOfDraw == this.currentDraw.size() + this.restOfDraw.size()) {
          this.cardRecycled += 1;
        }
        else {
          sizeOfDraw = this.currentDraw.size() + this.restOfDraw.size();
        }
      }
    }
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    startChecker();
    if (this.currentDraw.isEmpty() && this.restOfDraw.isEmpty()) {
      throw new IllegalStateException("No more cards to discard");
    }
    if ((double) cardRecycled / (this.currentDraw.size() + this.restOfDraw.size()) == 1.0) {
      numTimesRedrawAllowed --;
      cardRecycled = 0;
    }
    if (numTimesRedrawAllowed > 0) {
      this.restOfDraw.add(this.currentDraw.remove(0));
    }
    else {
      this.currentDraw.remove(0);
    }
    refreshDraw();
  }

}
