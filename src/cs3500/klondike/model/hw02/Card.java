package cs3500.klondike.model.hw02;

/**
 * This (essentially empty) interface marks the idea of cards.  You will need to
 * implement this interface in order to use your model.
 * 
 * <p>The only behavior guaranteed by this class is its {@link Card#toString()} method,
 * which will render the card as specified in the assignment.
 * 
 * <p>In particular, you <i>do not</i> know what implementation of this interface is
 * used by the Examplar wheats and chaffs, so your tests must be defined sufficiently
 * broadly that you do not rely on any particular constructors or methods of cards.
 */
public interface Card {

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   * @return the formatted card
   */
  String toString();

  /**
   * Returns an integer representation of the cards value, Ace being one, King being 13.
   * @return int of the cards value
   */
  int numerical();

  /**
   * Returns if this card is red.
   * @return true if the cards suit is Heart or Diamond, false otherwise
   */
  boolean isRed();

  /**
   * Returns the suit of this card.
   * @return enum representation of this cards suit
   */
  Suit suit();
}
