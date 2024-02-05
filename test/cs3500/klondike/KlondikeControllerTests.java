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
import cs3500.klondike.model.hw02.MockKlondikeModel;

/**
 * class to test the funcitonality of the KlondikeController class.
 */
public class KlondikeControllerTests {
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
  public void testDetectsGameOverWon() {
    init();
    StringReader in = new StringReader("mpf 1 1  mpf 2 3 mpf 2 2 mdf 4 mdf 1 mdf 2 mdf 3 mdf 4");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("You win"));
  }

  @Test
  public void testOneMoveScore() {
    init();
    StringReader in = new StringReader("mdf 1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Score: 1"));
    Assert.assertEquals(1, this.game.getScore());
  }

  @Test
  public void testMovePileFoundationInvalidAmountInputSkipsCommand() {
    init();
    Readable in = new StringReader("mpf 1 1 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMovePileValidInputSkipsInvalid() {
    init();
    Readable in = new StringReader("mpp 2 ! 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 3);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testMovePileInvalidAmount() {
    init();
    Readable in = new StringReader("mpp 1 1 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again"));
  }

  @Test
  public void testDrawToFoundationInvalid() {
    init();
    Readable in = new StringReader("mdf 0 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again"));
  }

  @Test
  public void testStartGameNoInput() {
    init();
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(this.game, aceTwo, false, 2, 1));
  }

  @Test
  public void testStartGameNoRealInput() {
    init();
    Readable in = new StringReader("mpf haiii hewwow  q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(this.game, aceTwo, false, 2, 1));
  }

  @Test
  public void testControllerSkipsGarbageInput() {
    init();
    Readable in = new StringReader("mdf haiii hewwow ?$$m    1 1  1  q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Foundation: A♣"));
  }

  @Test
  public void testStartGameNullModel() {
    init();
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            controller.playGame(null, aceTwo, false, 2, 1));
  }

  @Test
  public void testStartGameNullReadable() {
    init();
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new KlondikeTextualController(null, out));
  }


  @Test
  public void testStartGameNoQuitThrows() {
    init();
    Readable in = new StringReader("md 1");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () ->
            controller.playGame(new BasicKlondike(), aceTwo, false, 2, 1));
  }

  @Test
  public void testUserFriendlyInputWontTakeZeros() {
    init();
    Readable in = new StringReader("md 0 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(new BasicKlondike(), aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move."));
  }


  @Test
  public void testGameOverScoreOutPut() {
    init();
    StringReader in =
            new StringReader("mpf 1 1  mpf 2 3 mpf 2 2 mdf 4 mdf 1 mdf 2 mdf 3 mdf 4 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Score: 8"));
  }

  @Test
  public void testGameQuitDuringCommand() {
    init();
    StringReader in = new StringReader("mpf 1 1  mpf q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Game quit"));
  }

  @Test
  public void testImpossibleInput() {
    init();
    StringReader in = new StringReader("mpf 15 -1 1 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again"));
  }

  @Test
  public void testZerosAsInput() {
    init();
    StringReader in = new StringReader("mdf 0 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again"));
  }

  @Test
  public void testNoValidCommand() {
    init();
    StringReader in = new StringReader("aaa 0 q");
    StringBuilder out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.game, aceTwo, false, 2, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again"));
  }


  @Test
  public void testMovePileReadCorrectly() {
    init();
    Readable in = new StringReader("mpp 2 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 2);
    StringBuilder gameLog = new StringBuilder();
    BasicKlondike model = new BasicKlondike();
    controller.playGame(new MockKlondikeModel(gameLog, model),
            aceTwo, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Moving 1 cards "
            + "from source pile 1 to destination pile 0"));
    Assert.assertTrue(gameLog.toString().contains("pile moved"));
  }

  @Test
  public void testMoveDrawReadCorrectly() {
    init();
    Readable in = new StringReader("md 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 2);
    StringBuilder gameLog = new StringBuilder();
    BasicKlondike model = new BasicKlondike();
    controller.playGame(new MockKlondikeModel(gameLog, model),
            aceTwo, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Moving the next draw card to 0"));
    Assert.assertTrue(gameLog.toString().contains("draw card moved"));
  }

  @Test
  public void testMovePileToFoundationReadCorrectly() {
    init();
    Readable in = new StringReader("mpf 2 1 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 2);
    StringBuilder gameLog = new StringBuilder();
    BasicKlondike model = new BasicKlondike();
    controller.playGame(new MockKlondikeModel(gameLog, model),
            aceTwo, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Moving the next "
            + "card at source pile 1 to foundation pile 0"));
    Assert.assertTrue(gameLog.toString().contains("pile moved to foundation"));
  }

  @Test
  public void testMoveDrawToFoundationReadCorrectly() {
    init();
    Readable in = new StringReader("mdf 1 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwo, "2♣", 0);
    testUtils.placeAtIndex(aceTwo, "A♡", 2);
    StringBuilder gameLog = new StringBuilder();
    BasicKlondike model = new BasicKlondike();
    controller.playGame(new MockKlondikeModel(gameLog, model),
            aceTwo, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Moving the "
            + "next draw card to foundation pile 0"));
    Assert.assertTrue(gameLog.toString().contains("draw moved to foundation"));
  }

  @Test
  public void testRenderAsExpectedMidGame() {
    init();
    Readable in = new StringReader("md 2 q");
    Appendable out = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(in, out);
    testUtils.placeAtIndex(aceTwoThree, "2♣", 2);
    controller.playGame(this.game, aceTwoThree, false, 2, 2);
    Assert.assertTrue(out.toString().contains(" A♠  ?\n    2♣\n    A♡"));
  }


}
