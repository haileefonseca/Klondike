package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * exemplar tests to explore the functionality of extended model implementations.
 */
public class ExamplarExtendedModelTests {

  void placeAtIndex(List<Card> deck, String card, int index) {
    int cardIndex = 0;
    for (int i = 0; i < deck.size(); i++) {
      if (deck.get(i).toString().equals(card)) {
        cardIndex = i;
      }
    }
    deck.add(index, deck.remove(cardIndex));
  }

  List<Card> getRank(List<Card> deck, String rank) {
    List<Card> result = new ArrayList<>();
    for (Card c: deck) {
      if (c.toString().contains(rank)) {
        result.add(c);
      }
    }
    return result;
  }

  List<Card> filterSuit(List<Card> deck, String suit) {
    List<Card> result = new ArrayList<>();
    for (Card c: deck) {
      if (c.toString().contains(suit)) {
        result.add(c);
      }
    }
    return result;
  }

  @Before
  public void init() {
    KlondikeModel whGame = new WhiteheadKlondike();
  }


  @Test
  public void testWhiteHeadMoveLegality() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = whGame.getDeck();
    placeAtIndex(manipDeck, "2♡", 0);
    placeAtIndex(manipDeck, "A♢", 2);
    whGame.startGame(manipDeck, false, 2, 2);
    whGame.movePile(1, 1, 0);
    Assert.assertEquals("A♢", whGame.getCardAt(0, 1).toString());
  }

  @Test
  public void testWhiteHeadMoveAnyCardToEmptyCascade() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = whGame.getDeck();
    placeAtIndex(manipDeck, "A♡", 0);
    placeAtIndex(manipDeck, "5♡", 3);
    whGame.startGame(manipDeck, false, 2, 2);
    whGame.moveToFoundation(0, 0);
    whGame.moveDraw(0);
    Assert.assertEquals("5♡", whGame.getCardAt(0, 0).toString());
  }

  @Test
  public void testDrawOnlyLoopsAmountAllowed() {
    KlondikeModel ldGame = new LimitedDrawKlondike(2);
    List<Card> manipDeck = new ArrayList<>(getRank(ldGame.getDeck(), "A"));
    ldGame.startGame(manipDeck, false, 2, 1);
    ldGame.discardDraw();
    ldGame.discardDraw();
    ldGame.discardDraw();
    Assert.assertTrue(ldGame.getDrawCards().isEmpty());
  }

  @Test
  public void testLimitedDrawIsEmptyAfterLimit() {
    KlondikeModel ldGame = new LimitedDrawKlondike(0);
    List<Card> manipDeck = new ArrayList<>(getRank(ldGame.getDeck(), "A"));
    ldGame.startGame(manipDeck, false, 2, 1);
    ldGame.discardDraw();
    Assert.assertTrue(ldGame.getDrawCards().isEmpty());
  }

  @Test
  public void testCascadeMoveAllSameSuit() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = new ArrayList<>(filterSuit(whGame.getDeck(), "♡"));
    manipDeck.addAll(filterSuit(whGame.getDeck(), "♢"));
    placeAtIndex(manipDeck, "4♢", 0);
    placeAtIndex(manipDeck, "5♢", 1);
    placeAtIndex(manipDeck, "3♡", 2);
    whGame.startGame(manipDeck, false, 2, 1);
    whGame.movePile(1, 1, 0);
    Assert.assertThrows(IllegalStateException.class, () -> whGame.movePile(0, 2, 1));
  }

  @Test
  public void testCascadeMoveLegalityDiffersFromBasic() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = whGame.getDeck();
    placeAtIndex(manipDeck, "4♣", 0);
    placeAtIndex(manipDeck, "5♢", 1);
    placeAtIndex(manipDeck, "3♡", 2);
    whGame.startGame(manipDeck, false, 2, 1);
    Assert.assertThrows(IllegalStateException.class, () -> whGame.movePile(1, 1, 0));
  }

}
