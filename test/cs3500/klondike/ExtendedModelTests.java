package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeCard;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * testing behaviors specific to either Whitehead or LimitedDraw.
 */
public class ExtendedModelTests {
  TestUtils testUtils = new TestUtils();

  @Test
  public void testWhiteHeadGameOver() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = new ArrayList<>();
    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.TWO));
    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.THREE));
    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.ACE));
    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.FOUR));
    whGame.startGame(manipDeck, false, 2, 1);
    whGame.discardDraw();
    Assert.assertFalse(whGame.isGameOver());
  }

  @Test
  public void testWhiteHeadGameOverAGAIN() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> deck = whGame.getDeck();
    List<Card> manipDeck = new ArrayList<>(testUtils.filterSuit(deck, Suit.HEART));
    manipDeck.addAll(testUtils.filterSuit(deck, Suit.DIAMOND));
    manipDeck.addAll(testUtils.filterSuit(deck, Suit.SPADE));
    whGame.startGame(manipDeck, false, 2, 1);
    whGame.discardDraw();
    Assert.assertFalse(whGame.isGameOver());
  }

  @Test
  public void testWhiteHeadAllCascadeVisible() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = whGame.getDeck();
    testUtils.placeAtIndex(manipDeck, "2♡", 1);
    whGame.startGame(manipDeck, false, 2, 2);
    Assert.assertEquals("2♡", whGame.getCardAt(1, 0).toString());
  }

  @Test
  public void testAllCardsFaceUp() {
    KlondikeModel whGame = new WhiteheadKlondike();
    whGame.startGame(whGame.getDeck(), false, 5, 5);
    Assert.assertTrue(whGame.isCardVisible(4, 0));
    Assert.assertTrue(whGame.isCardVisible(4, 4));
  }

  @Test
  public void testCascadeMoveToEmptyAllSameSuit() {
    KlondikeModel whGame = new WhiteheadKlondike();
    List<Card> manipDeck = new ArrayList<>(testUtils.filterSuit(whGame.getDeck(), "♡"));
    manipDeck.addAll(testUtils.filterSuit(whGame.getDeck(), "♢"));
    testUtils.placeAtIndex(manipDeck, "3♢", 0);
    testUtils.placeAtIndex(manipDeck, "5♢", 1);
    testUtils.placeAtIndex(manipDeck, "4♢", 2);
    whGame.startGame(manipDeck, false, 2, 1);
    whGame.movePile(0, 1, 1);
    whGame.movePile(1, 3, 0);
    Assert.assertEquals("5♢", whGame.getCardAt(0, 0).toString());
  }

  @Test
  public void testNoNegativeNumTimesRedraw() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new LimitedDrawKlondike(-12));
  }




}
