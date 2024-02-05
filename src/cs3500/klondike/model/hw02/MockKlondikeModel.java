package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * class to mock the features of a klondike model,
 * appending its actions to a log which can track how the controller interacts with the model.
 * I've chosen to only implement pieces the controller interacts with/ passes input for
 * since it is most useful for testing.
 */
public class MockKlondikeModel implements KlondikeModel {
  private final StringBuilder log;
  private BasicKlondike model;

  /**
   * constructor for a mock klondike model.
   * @param log where data about the actions will be stored
   * @param model a BasicKlondike that can carry out actions
   */
  public MockKlondikeModel(StringBuilder log, BasicKlondike model) {
    this.log = log;
    this.model = model;
  }

  @Override
  public List<Card> getDeck() {
    log.append("Getting deck\n");
    return new ArrayList<Card>();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("Starting game: shuffle is %B, %d cascade piles, and %d draw piles\n",
            shuffle, numPiles, numDraw));
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
      log.append("game started successfully\n");
    }
    catch (IllegalArgumentException | IllegalStateException e) {
      log.append("game not started\n");
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("Moving %d cards from source pile %d to destination pile %d\n",
            numCards, srcPile, destPile));
    try {
      model.movePile(srcPile, numCards, destPile);
      log.append("pile moved\n");
    }
    catch (IllegalArgumentException | IllegalStateException e) {
      log.append("pile could not be moved\n");
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("Moving the next draw card to %d\n", destPile));
    try {
      model.moveDraw(destPile);
      log.append("draw card moved\n");
    }
    catch (IllegalArgumentException | IllegalStateException e) {
      log.append("draw card could not be moved\n");
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("Moving the next card at source pile %d to foundation pile %d",
            srcPile, foundationPile));
    try {
      model.moveToFoundation(srcPile, foundationPile);
      log.append("pile moved to foundation\n");
    }
    catch (IllegalArgumentException | IllegalStateException e) {
      log.append("pile could not be moved to foundation\n");
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append(String.format("Moving the next draw card to foundation pile %d", foundationPile));
    try {
      model.moveDrawToFoundation(foundationPile);
      log.append("draw moved to foundation\n");
    }
    catch (IllegalArgumentException | IllegalStateException e) {
      log.append("draw could not be moved to foundation\n");
    }
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    log.append("Discard draw called\n");
    try {
      model.discardDraw();
      log.append("draw discarded\n");
    }
    catch (IllegalArgumentException | IllegalStateException e) {
      log.append("could not discard draw\n");
    }
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    try {
      boolean gameStatus = model.isGameOver();
      log.append(String.format("game over called, returned %b\n", gameStatus));
    }
    catch (IllegalStateException e) {
      log.append("could not call game over\n");
    }
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    try {
      int score = model.getScore();
      log.append(String.format("get score called, returned %d\n", score));
    }
    catch (IllegalStateException e) {
      log.append("could not call get score\n");
    }
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public Card getCardAt(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    return new ArrayList<Card>();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    return 0;
  }
}
