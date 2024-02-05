package cs3500.klondike.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

/**
 * Class that implements the functionality of a controller for a game of cs3500.klondike.Klondike,
 * by responding to user inputs and outputting the state of the game.
 */
public class KlondikeTextualController implements KlondikeController {
  private final Appendable output;
  private final Scanner sc;
  private boolean quit = false;

  /**
   * Constructor that instantiates a controller for a game of cs3500.klondike.Klondike.
   *
   * @param r input stream for the controller
   * @param a output stream for the controller
   * @throws IllegalArgumentException if either r or a is null
   */
  public KlondikeTextualController(Readable r, Appendable a) throws IllegalArgumentException {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Null readable or appendable");
    }
    this.output = a;
    this.sc = new Scanner(r);
  }

  @Override
  public void playGame(KlondikeModel model,
                       List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    attemptStart(model, deck, shuffle, numPiles, numDraw);
    tryRender(new KlondikeTextualView(model, this.output), model);
    while (sc.hasNext() && !this.quit) {
      String userInput = sc.next();
      if (gameOver(model, deck)) {
        break;
      }
      switch (userInput) {
        case "mpp":
          attemptMovePile(model);
          break;
        case "md":
          attemptMoveDraw(model);
          break;
        case "mpf":
          attemptMoveFoundation(model);
          break;
        case "mdf":
          attemptMoveDrawFoundation(model);
          break;
        case "dd":
          attemptDiscardDraw(model);
          break;
        case "q":
        case "Q":
          gameQuit(model);
          break;
        default:
          invalidMoveMessage(new IllegalArgumentException("Not a valid type of move."), model);
      }
      if (gameOver(model, deck)) {
        break;
      }
    }
    if (!sc.hasNext() && !quit && !model.isGameOver()) {
      throw new IllegalStateException("Out of inputs before quit.");
    }
  }


  // if the game is over or won, render the board and append the correct message
  private boolean gameOver(KlondikeModel model, List<Card> deck) {
    if (model.isGameOver() || model.getScore() == deck.size()) {
      if (model.getScore() == deck.size()) {
        tryAppend("You win!\n");
      } else {
        tryAppend(String.format("Game over. Score: %d\n", model.getScore()));
      }
      this.quit = true;
      return true;
    }
    return false;
  }

  // appends a message to this output
  private void tryAppend(String message) throws IllegalStateException {
    try {
      this.output.append(message);
    } catch (IOException ioException) {
      throw new IllegalStateException("Unable to render output");
    }
  }

  // renders a board to this output
  private void tryRender(KlondikeTextualView view, KlondikeModel model)
          throws IllegalStateException {
    try {
      view.render();
      this.output.append(String.format("\nScore: %d\n", model.getScore()));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to render game");
    }
  }

  // sets the quit variable to true, appends the game quit message and the final board
  private void gameQuit(KlondikeModel model) {
    tryAppend("Game quit!\nState of game when quit:\n");
    try {
      new KlondikeTextualView(model, this.output).render();
      this.output.append(String.format("\nScore: %d", model.getScore()));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to render game");
    }
    this.quit = true;
  }

  // if there is an invalid move, append the message and the current board
  private void invalidMoveMessage(Exception e, KlondikeModel model) throws IllegalStateException {
    try {
      this.output.append("Invalid move. Play again. ").append(e.getMessage()).append("\n");
      tryRender(new KlondikeTextualView(model, this.output), model);
    } catch (IOException ioException) {
      throw new IllegalStateException("Unable to render output");
    }
  }


  // searches for the next integer, or a q.
  // returns the integer if found, quits the game if it finds a q,
  // and throws if it runs out of input without encountering a q
  private int searchNext(KlondikeModel model) throws IllegalStateException {
    boolean found = false;
    while (sc.hasNext() && !found) {
      if (sc.hasNextInt()) {
        int nextInt = sc.nextInt();
        if (nextInt >= 0) {
          found = true;
          return nextInt;
        }
      }
      else {
        String next = sc.next();
        if (next.contains("q") || next.contains("Q")) {
          found = true;
          gameQuit(model);
          break;
        }
      }
    }
    throw new NoSuchElementException("Out of inputs before quit.");
  }

  // attempts to start the game of the given model
  private void attemptStart(KlondikeModel model,
                            List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (IllegalStateException | IllegalArgumentException e) {
      throw new IllegalStateException("Game unable to be started");
    }
  }

  // attempts to move a pile in the model with the controllers input
  private void attemptMovePile(KlondikeModel model) {
    try {
      int srcPile = searchNext(model);
      int numCard = searchNext(model);
      int destPile = searchNext(model);
      if (!this.quit) {
        model.movePile(srcPile - 1, numCard, destPile - 1);
        tryRender(new KlondikeTextualView(model, this.output), model);
      }
      else {
        gameQuit(model);
      }
    } catch (IllegalStateException | IllegalArgumentException e) {
      invalidMoveMessage(e, model);
    } catch (NoSuchElementException ignored) {
    }
  }

  // attempts to move a draw card in the model with the controllers input
  private void attemptMoveDraw(KlondikeModel model) {
    try {
      int destPile = searchNext(model);
      if (!this.quit) {
        model.moveDraw(destPile - 1);
        tryRender(new KlondikeTextualView(model, this.output), model);
      }
      else {
        gameQuit(model);
      }
    } catch (IllegalStateException | IllegalArgumentException e) {
      invalidMoveMessage(e, model);
    } catch (NoSuchElementException ignored) {
    }
  }

  // attempts to move a pile to foundation in the model, with the controllers current input
  private void attemptMoveFoundation(KlondikeModel model) {
    try {
      int srcPile = searchNext(model);
      int foundPile = searchNext(model);
      if (!this.quit) {
        model.moveToFoundation(srcPile - 1, foundPile - 1);
        tryRender(new KlondikeTextualView(model, this.output), model);
      }
      else {
        gameQuit(model);
      }
    } catch (IllegalStateException | IllegalArgumentException e) {
      invalidMoveMessage(e, model);
    } catch  (NoSuchElementException ignored) {
    }
  }

  // attempts to move a draw card to foundation in the model, with the controllers current input
  private void attemptMoveDrawFoundation(KlondikeModel model) {
    try {
      int foundPile = searchNext(model);
      if (!this.quit) {
        model.moveDrawToFoundation(foundPile - 1);
        tryRender(new KlondikeTextualView(model, this.output), model);
      }
      else {
        gameQuit(model);
      }
    } catch (IllegalStateException | IllegalArgumentException e) {
      invalidMoveMessage(e, model);
    } catch (NoSuchElementException ignored) {
    }
  }

  // attempts to discard draw in the model
  private void attemptDiscardDraw(KlondikeModel model) {
    try {
      model.discardDraw();
      tryRender(new KlondikeTextualView(model, this.output), model);
    } catch (IllegalStateException | IllegalArgumentException e) {
      invalidMoveMessage(e, model);
    }
  }




}
