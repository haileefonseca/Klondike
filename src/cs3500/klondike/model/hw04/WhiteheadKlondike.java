package cs3500.klondike.model.hw04;

import java.util.List;

import cs3500.klondike.model.hw02.AKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

/**
 * Class representing a game of Whitehead Klondike, follows similar rules to Abstract Klondike,
 * but all cards are visible, only cards of the same color can be placed on top of one another,
 * and a group of more than one card being moved must all be of the same suit. Overrode methods
 * dealing with legality, moving, and visibility to reflect this.
 */
public class WhiteheadKlondike extends AKlondike implements KlondikeModel {

  /**
   * default, zero argument constructor, makes a game of Whitehead.
   */
  public WhiteheadKlondike() {
    super();
  }


  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    startChecker();
    validPile(this.gameTable, srcPile);
    validPile(this.gameTable, destPile);
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Source and destination are the same");
    }
    if (this.gameTable.get(srcPile).size() < numCards || numCards < 0) {
      throw new IllegalArgumentException("Invalid card number");
    }
    if (numCards > 1) {
      if (!(allSameSuit(srcPile, numCards))) {
        throw new IllegalStateException("All cards in this move are not the same suit");
      }
    }
    int cardHeight = this.gameTable.get(srcPile).size() - numCards;
    if (!(this.isCardVisible(srcPile, cardHeight))) {
      throw new IllegalArgumentException("Card is not visible");
    }
    for (int i = 0; i < numCards; i++) {
      moveCascade(this.gameTable.get(srcPile), this.gameTable.get(destPile), cardHeight);
    }
  }

  @Override
  protected void moveCascade(List<Card> source, List<Card> destination, int cardHeight) throws
          IllegalStateException {
    if (destination.isEmpty()) {
      destination.add(source.remove(cardHeight));
    } else if (legalPlay(destination.get(destination.size() - 1), source.get(cardHeight))) {
      destination.add(source.remove(cardHeight));
    } else {
      throw new IllegalStateException(("Illegal play"));
    }
  }

  private boolean allSameSuit(int srcPile, int numCards) {
    int pileSize = this.gameTable.get(srcPile).size();
    for (int card =  pileSize - numCards; card < pileSize - 1; card ++) {
      if (this.gameTable.get(srcPile).get(card).suit()
              != this.gameTable.get(srcPile).get(card + 1).suit()) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean legalPlay(Card desCard, Card sourceCard) {
    if (desCard.isRed() && sourceCard.isRed() || !desCard.isRed() && !sourceCard.isRed()) {
      return desCard.numerical() - 1 == sourceCard.numerical();
    }
    return false;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    return true;
  }

}
