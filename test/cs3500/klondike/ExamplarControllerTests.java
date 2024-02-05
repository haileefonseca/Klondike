package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;


/**
 * Class that tests functionality of the controller.
 */
public class ExamplarControllerTests {
  BasicKlondike game;
  List<Card> deck;
  List<Card> aceTwo;
  List<Card> aceTwoThree;
  TestUtils testUtils;

  @Before
  public void init() {
    this.testUtils = new TestUtils();
    this.game = new BasicKlondike();
    this.deck = game.getDeck();
    this.aceTwo = new ArrayList<>();
    aceTwo.addAll(testUtils.getRank(deck, "A"));
    aceTwo.addAll(testUtils.getRank(deck, "2"));
    this.aceTwoThree = new ArrayList<>();
    aceTwoThree.addAll(testUtils.getRank(deck, "A"));
    aceTwoThree.addAll(testUtils.getRank(deck, "2"));
    aceTwoThree.addAll(testUtils.getRank(deck, "3"));
  }




  @Test
  public void testMoveDrawNoneLeft() {
    init();
    StringReader in = new StringReader("mdf 2 mdf 3 dd q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwoThree, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move."));
  }

  @Test
  public void testMovePileFoundationInvalidNumInput() {
    init();
    Readable in = new StringReader("mpf 1 8 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move."));
  }

  @Test
  public void testMovePileValidInput() {
    init();
    Readable in = new StringReader("mpp 2 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 2);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testMovePileValidInputMoveCompleted() {
    init();
    Readable in = new StringReader("mpp 2 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 2);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertEquals(2, this.game.getPileHeight(0));
  }

  @Test
  public void testMoveDrawValidMoveCompleted() {
    init();
    Readable in = new StringReader("md 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 3);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertEquals("A♡", this.game.getCardAt(0,1).toString());
  }

  @Test
  public void testMovePileInvalidPileNum() {
    init();
    Readable in = new StringReader("mpp 3 5 15");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(this.game, aceTwo, false, 2, 1));
  }

  @Test
  public void testMoveDrawInvalidSkipsBadInput() {
    init();
    Readable in = new StringReader("mdf @ 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Foundation: A♣"));
  }

  @Test
  public void testStartGameInitialState() {
    init();
    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    TextualView view = new KlondikeTextualView(this.game);
    Assert.assertTrue(out.toString().contains("Game quit!\nState of game when quit:\n"
            + view.toString() + "\nScore: 0"));
  }

  @Test
  public void testQuitGameIsOnlyInput() {
    init();
    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Game quit!\nState of game when quit:"));
  }


}
