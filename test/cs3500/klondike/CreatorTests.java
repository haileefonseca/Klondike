package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 *  Tests creator class functionality.
 */
public class CreatorTests {

  @Test
  public void testCreateBasic() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    Assert.assertTrue(model instanceof BasicKlondike);
  }

  @Test
  public void testCreateBasicPlayable() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    model.startGame(model.getDeck(), true, 3, 2);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testCreateLimited() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    Assert.assertTrue(model instanceof LimitedDrawKlondike);
  }

  @Test
  public void testCreateLimitedPlayable() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.LIMITED);
    model.startGame(model.getDeck(), true, 3, 2);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testCreateLimitedWithDrawPile() {
    KlondikeModel model = KlondikeCreator.createCustomLimited(KlondikeCreator.GameType.LIMITED, 1);
    TestUtils testUtils = new TestUtils();
    List<Card> manipDeck = new ArrayList<>(testUtils.getRank(model.getDeck(), "A"));
    model.startGame(manipDeck, false, 2, 1);
    model.discardDraw();
    model.discardDraw();
    Assert.assertTrue(model.getDrawCards().isEmpty());
  }

  @Test
  public void testCreateWhiteHead() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    Assert.assertTrue(model instanceof WhiteheadKlondike);
  }

  @Test
  public void testCreateWhiteheadPlayable() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    model.startGame(model.getDeck(), true, 3, 2);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testCreateNullThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> KlondikeCreator.create(null));
  }

}
