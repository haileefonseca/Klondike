package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.List;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.view.KlondikeTextualView;

/**
 * class to run tests for behavior that is the same for basic and limited.
 */
public abstract class BasicLimitedModelTests {
  protected abstract KlondikeModel factory();

  protected List<Card> cards;
  protected List<Card> spades;
  protected List<Card> clubs;
  protected List<Card> hearts;
  protected List<Card> diamonds;
  protected TestUtils testUtils = new TestUtils();


  @Before
  public void init() {
    KlondikeModel model = new BasicKlondike();
    this.cards = model.getDeck();
    this.spades = testUtils.filterSuit(cards, Suit.SPADE);
    this.clubs = testUtils.filterSuit(cards, Suit.CLUB);
    this.diamonds = testUtils.filterSuit(cards, Suit.DIAMOND);
    this.hearts = testUtils.filterSuit(cards, Suit.HEART);
  }

  @Test
  public void testGetCardAtWhenVisible() {
    KlondikeModel model = factory();
    model.startGame(model.getDeck(), false, 3, 2);
    model.moveToFoundation(0, 0);
    Assert.assertEquals("A♠", model.getCardAt(0).toString());
  }


  @Test
  public void testTextViewEmptyMiddleandCardInFoundation() {
    init();
    KlondikeModel game = new BasicKlondike();
    testUtils.placeAtIndex(cards, "2♡", 1);
    testUtils.placeAtIndex(cards, "A♡", 3);
    game.startGame(cards, false, 3, 3);
    game.moveToFoundation(1, 1);
    game.moveToFoundation(1, 1);
    String expect = "Draw: 5♠, 6♠, 7♠\nFoundation:"
            + " <none>, 2♡, <none>, <none>\n A♠  X  ?\n        ?\n       4♠";
    Assert.assertEquals(expect, new KlondikeTextualView(game).toString());
  }

  @Test
  public void testGetCardAtWhenNotVisible() {
    KlondikeModel model = factory();
    model.startGame(model.getDeck(), false, 3, 2);
    Assert.assertThrows(IllegalArgumentException.class, () -> model.getCardAt(1, 0));
  }

  @Test
  public void testIllegalMovePileSameColor() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "K♡", 0);
    testUtils.placeAtIndex(cards, "Q♡", 2);
    game.startGame(cards, false, 2, 2);
    Assert.assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 0));
  }

  @Test
  public void testMoveToEmptyIllegalPlay() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "A♡",0);
    testUtils.placeAtIndex(cards, "2♠", 2);
    game.startGame(cards, false, 3, 2);
    game.moveToFoundation(0, 0);
    Assert.assertThrows(IllegalStateException.class, () -> game.movePile(1, 1, 0));
  }


  @Test
  public void testMovePileLogicalMove() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    List<Card> rigged = testUtils.underRank(cards, 3);
    testUtils.placeAtIndex(rigged, "3♡",0);
    testUtils.placeAtIndex(rigged, "2♠", 3);
    game.startGame(rigged, false, 3, 2);
    game.movePile(1, 1, 0);
    Assert.assertEquals("2♠", game.getCardAt(0, 1).toString());
  }

  @Test
  public void testMovePileToNonExistent() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    List<Card> rigged = testUtils.underRank(cards, 3);
    testUtils.placeAtIndex(rigged, "3♡",0);
    testUtils.placeAtIndex(rigged, "2♠", 3);
    game.startGame(rigged, false, 3, 2);
    Assert.assertThrows(IllegalArgumentException.class, () -> game.movePile(1, 1, 3));
  }

  @Test
  public void testDrawMoreCardsAfterFullDeckCycledSameTopCard() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    Card firstDraw = cards.get(3);
    game.startGame(cards, false, 2, 2);
    for (int i = 0; i < 49; i++) {
      game.discardDraw();
    }
    Assert.assertEquals(firstDraw, game.getDrawCards().get(0));
  }


  @Test
  public void testMoveToFoundationNonAce() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "K♠", 0);
    game.startGame(cards, false, 6, 5);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 0));
  }

  @Test
  public void testMoveToFoundationAce() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "A♠", 21);
    game.startGame(cards, false, 6, 5);
    game.moveDrawToFoundation(1);
    Assert.assertEquals("A♠", game.getCardAt(1).toString());
  }

  @Test
  public void testMoveToFoundationNextCardWrongSuit() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "2♢", 0);
    testUtils.placeAtIndex(cards, "A♠", 21);
    game.startGame(cards, false, 6, 5);
    game.moveDrawToFoundation(1);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 1));
  }

  @Test
  public void testMoveToFoundationWrongNumberWrongSuit() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "3♢", 0);
    testUtils.placeAtIndex(cards, "A♠", 21);
    game.startGame(cards, false, 6, 5);
    game.moveDrawToFoundation(1);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 1));
  }

  @Test
  public void testMoveToFoundationWrongNumberRightSuit() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "3♢", 0);
    testUtils.placeAtIndex(cards, "A♢", 21);
    game.startGame(cards, false, 6, 5);
    game.moveDrawToFoundation(1);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveToFoundation(0, 1));
  }

  @Test
  public void testMoveToNonexistentFoundation() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "A♠", 0);
    game.startGame(cards, false, 7, 6);
    Assert.assertThrows(IllegalArgumentException.class, () -> game.moveToFoundation(0, 4));
  }

  @Test
  public void testPlaceAnyCardOnAce() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "A♢", 0);
    testUtils.placeAtIndex(cards, "K♢", 21);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
  }

//  @Test
//  public void testGameOverNoLegalMovesNoDraw() {
//    KlondikeModel model = factory();
//    List<Card> manipDeck = new ArrayList<>();
//    manipDeck.add(new KlondikeCard(Suit.HEART, Rank.THREE));
//    manipDeck.add(new KlondikeCard(Suit.HEART, Rank.TWO));
//    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.THREE));
//    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.ACE));
//    manipDeck.add(new KlondikeCard(Suit.DIAMOND, Rank.TWO));
//    manipDeck.add(new KlondikeCard(Suit.HEART, Rank.ACE));
//    model.startGame(manipDeck, false, 2, 3);
//    model.moveDrawToFoundation(0);
//    model.moveDrawToFoundation(0);
//    model.moveDrawToFoundation(1);
//    Assert.assertTrue(model.isGameOver());
//  }

//  @Test
//  public void testGameOverWhenWon() {
//    init();
//    KlondikeModel model = factory();
//    testUtils.underRank(cards, 2);
//    model.startGame(cards, false, 2, 1);
//    System.out.print(new KlondikeTextualView(model).toString());
//    model.moveToFoundation(0, 0);
////    model.moveToFoundation(1, 2);
////    model.moveToFoundation(1, 1);
////    model.moveDrawToFoundation(3);
////    model.moveDrawToFoundation(0);
////    model.moveDrawToFoundation(1);
////    model.moveDrawToFoundation(2);
////    model.moveDrawToFoundation(3);
////    Assert.assertTrue(model.isGameOver());
//  }




  /**
   * Creates a BasicKlondike to run tests on.
   */
  public static final class TestBasic extends BasicLimitedModelTests {
    @Override
    protected KlondikeModel factory() {
      return new BasicKlondike();
    }
  }

  /**
   * Creates a LimitedDraw model to run tests on.
   */
  public static final class TestLimited extends BasicLimitedModelTests {
    @Override
    protected KlondikeModel factory() {
      return new LimitedDrawKlondike(2);
    }
  }
}
