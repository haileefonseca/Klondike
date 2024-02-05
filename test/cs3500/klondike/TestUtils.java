package cs3500.klondike;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Suit;

/**
 * package-private class that holds methods to manipulate lists of cards for testing purposes.
 */
public class TestUtils {

  /**
   * Places a card at a specific index in a list.
   * @param deck the list of Cards to be manipulated
   * @param card the Card to move
   * @param index the 0-based index to place the card at
   */
  void placeAtIndex(List<Card> deck, String card, int index) {
    int cardIndex = 0;
    for (int i = 0; i < deck.size(); i++) {
      if (deck.get(i).toString().equals(card)) {
        cardIndex = i;
      }
    }
    deck.add(index, deck.remove(cardIndex));
  }

  /**
   * filters a list of card to include all ranks under a threshold.
   * @param deck list of Card to be filtered
   * @param rank rank to retrieve
   * @return
   */
  List<Card> getRank(List<Card> deck, String rank) {
    List<Card> result = new ArrayList<>();
    for (Card c: deck) {
      if (c.toString().contains(rank)) {
        result.add(c);
      }
    }
    return result;
  }

  /**
   * Returns a list filtered by suit.
   * @param deck list of cards to be filtered
   * @param suit suit to filter by
   * @return the filtered list
   */
  List<Card> filterSuit(List<Card> deck, String suit) {
    List<Card> result = new ArrayList<>();
    for (Card c: deck) {
      if (c.toString().contains(suit)) {
        result.add(c);
      }
    }
    return result;
  }

  // alternate method using Suit enums
  List<Card> filterSuit(List<Card> deck, Suit suit) {
    List<Card> result = new ArrayList<>();
    for (Card c: deck) {
      if (c.toString().contains(suit.toString())) {
        result.add(c);
      }
    }
    return result;
  }

  /**
   * returns a new list that has filtered cards to include all ranks under a threshold.
   * @param deck list of Card to be filtered
   * @param rank rank to retrieve
   * @return
   */
  List<Card> underRank(List<Card> deck, int rank) {
    List<Card> result = new ArrayList<>();
    for (Card c: deck) {
      if (c.numerical() <= rank) {
        result.add(c);
      }
    }
    return result;
  }


}
