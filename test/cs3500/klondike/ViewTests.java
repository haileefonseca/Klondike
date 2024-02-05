package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.view.KlondikeTextualView;

public class ViewTests {

  public BasicKlondike game;
  public List<Card> cards;
  public List<Card> spades;
  public List<Card> clubs;
  public List<Card> hearts;
  public List<Card> diamonds;
  TestUtils testUtils = new TestUtils();

  @Before
  public void init() {
    this.game = new BasicKlondike();
    this.cards = game.getDeck();
    this.spades = testUtils.filterSuit(cards, Suit.SPADE);
    this.clubs = testUtils.filterSuit(cards, Suit.CLUB);
    this.diamonds = testUtils.filterSuit(cards, Suit.DIAMOND);
    this.hearts = testUtils.filterSuit(cards, Suit.HEART);
  }

  @Test
  public void testTextViewEmptyMiddleandCardInFoundation() {
    init();
    testUtils.placeAtIndex(cards, "2♡", 1);
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
  public void testTextViewEmptyDraw() {
    init();
    List<Card> customDeck = testUtils.underRank(spades, 7);
    testUtils.placeAtIndex(customDeck, "A♠", 6);
    game.startGame(customDeck, false, 3, 1);
    game.moveDrawToFoundation(0);
    String expect = "Draw: \nFoundation:"
            + " A♠\n 2♠  ?  ?\n    5♠  ?\n       7♠";
    Assert.assertEquals(expect, new KlondikeTextualView(game).toString());
  }

  @Test
  public void testView() {
    init();
    List<Card> customDeck = testUtils.underRank(spades, 3);
    customDeck.addAll(testUtils.underRank(hearts, 3));
    testUtils.placeAtIndex(customDeck, "2♠", 2);
    game.startGame(customDeck, false, 2, 3);
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    game.moveDraw(1);
    String expect = new KlondikeTextualView(game).toString();
    System.out.println(expect);
  }
}
