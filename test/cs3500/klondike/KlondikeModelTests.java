package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * class to run tests for behavior that is the same across all AKlondike classes.
 */
public abstract class KlondikeModelTests {
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
  public final void testStartGameRunsNotOfEvenLength() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>();
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.addAll(testUtils.underRank(this.diamonds, 8));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testPlaceAnyCardOnAce() {
    KlondikeModel game = factory();
    List<Card> cards = game.getDeck();
    testUtils.placeAtIndex(cards, "A♢", 0);
    testUtils.placeAtIndex(cards, "K♢", 21);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
  }

  @Test
  public void testStartGameBrokenRun() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>();
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.addAll(testUtils.underRank(this.diamonds, 6));
    customDeck.remove(4);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testStartGameOneAceMissing() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = model.getDeck();
    customDeck.remove(0);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testStartGameSingleSuitBrokenRun() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>(this.spades);
    customDeck.remove(11);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testStartGameSingleSuitSEVERELYBrokenRun() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>(this.spades);
    customDeck.remove(12);
    customDeck.remove(8);
    customDeck.remove(2);
    customDeck.remove(2);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, true, 3, 3));
  }

  @Test
  public void testStartGameSingleSuitMissingMiddle() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>(this.spades);
    customDeck.remove(2);
    customDeck.remove(2);
    customDeck.remove(2);
    customDeck.remove(2);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, true, 3, 3));
  }

  @Test
  public void testStartGameAllSuitsAllMissing2() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>();
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.addAll(testUtils.underRank(this.diamonds, 6));
    customDeck.addAll(testUtils.underRank(this.hearts, 6));
    customDeck.addAll(testUtils.underRank(this.clubs, 6));
    customDeck.remove(1);
    customDeck.remove(6);
    customDeck.remove(16);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testStartGameDuplicateSuitsBroken() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>();
    customDeck.addAll(testUtils.underRank(this.diamonds, 5));
    customDeck.addAll(testUtils.underRank(this.diamonds, 5));
    customDeck.addAll(testUtils.underRank(this.diamonds, 5));
    customDeck.remove(2);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testStartDuplicateSuitsAllMissingSame() {
    init();
    KlondikeModel model = factory();
    List<Card> customDeck = new ArrayList<>();
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.addAll(testUtils.underRank(this.spades, 6));
    customDeck.remove(1);
    customDeck.remove(6);
    customDeck.remove(16);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(customDeck, false, 3, 3));
  }

  @Test
  public void testStartGameExceptionZeroPiles() {
    init();
    KlondikeModel model = factory();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(cards, false, 0, 2));
  }

  @Test
  public void testStartGameExceptionZeroDraw() {
    init();
    KlondikeModel model = factory();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(cards, false, 3, 0));
  }

  @Test
  public void testStartGameExceptionNegativePiles() {
    init();
    KlondikeModel model = factory();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(cards, false, -1, 2));
  }

  @Test
  public void testStartGameExceptionNegativeDraw() {
    init();
    KlondikeModel model = factory();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(cards, false, 3, -1));
  }

  @Test
  public void testStartGameExceptionNullCards() {
    init();
    KlondikeModel model = factory();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(null, false, 3, 2));
  }

  // very rarely this test may fail, if shuffle happens to be the order of the original dealt deck
  @Test
  public void testStartGameShuffles() {
    init();
    KlondikeModel model = factory();
    List<Card> copyCards = new ArrayList<>(cards);
    copyCards.remove(0);
    model.startGame(cards, true, 1, 51);
    Assert.assertNotEquals(copyCards, model.getDrawCards());
  }

  @Test
  public void testStartGameDoesntShuffle() {
    init();
    KlondikeModel model = factory();
    List<Card> copyCards = new ArrayList<>(cards);
    copyCards.remove(0);
    model.startGame(cards, false, 1, 51);
    Assert.assertEquals(copyCards, model.getDrawCards());
  }

  @Test
  public void testGameOverReturnsFalseWhenInProgress() {
    init();
    KlondikeModel model = factory();
    model.startGame(cards, false, 1, 51);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testGameOverReturnsFalseOnlyDrawRemains() {
    init();
    KlondikeModel model = factory();
    testUtils.placeAtIndex(cards, "A♢", 0);
    model.startGame(cards, false, 1, 51);
    model.moveToFoundation(0, 0);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testGameOverMoveToFoundationLeft() {
    init();
    KlondikeModel game = factory();
    List<Card> customDeck = testUtils.underRank(spades, 7);
    testUtils.placeAtIndex(customDeck, "A♠", 6);
    game.startGame(customDeck, false, 3, 1);
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
  }

  @Test
  public void testGetCardAtEmptyFoundation() {
    init();
    KlondikeModel game = factory();
    game.startGame(cards, false, 3, 1);
    Assert.assertNull(game.getCardAt(0));
  }

  @Test
  public void testGetCardAtFoundation() {
    init();
    KlondikeModel game = factory();
    List<Card> customDeck = testUtils.underRank(spades, 7);
    testUtils.placeAtIndex(customDeck, "A♠", 6);
    game.startGame(customDeck, false, 3, 1);
    game.moveDrawToFoundation(0);
    Assert.assertEquals("A♠", game.getCardAt(0).toString());
  }





  /**
   * Creates a BasicKlondike to run tests on.
   */
  public static final class TestBasic extends KlondikeModelTests  {
    @Override
    protected KlondikeModel factory() {
      return new BasicKlondike();
    }
  }

  /**
   * Creates a LimitedDraw model to run tests on.
   */
  public static final class TestLimited extends KlondikeModelTests  {
    @Override
    protected KlondikeModel factory() {
      return new LimitedDrawKlondike(2);
    }
  }

  /**
   * Creates a Whitehead model to run tests on.
   */
  public static final class TestWhiteHead extends KlondikeModelTests {
    @Override
    protected KlondikeModel factory() {
      return new WhiteheadKlondike();
    }
  }

}
