package cs3500.klondike.model.hw02;

/**
 * class to represent the suit of a card.
 */
public enum Suit {
  SPADE("♠"), CLUB("♣"), DIAMOND("♢"), HEART("♡");
  private final String suit;

  /**
   * constructs a Suit enum with its suit stored as a String.
   * @param suit the suit of the card
   */
  Suit(final String suit) {
    this.suit = suit;
  }

  @Override
  public String toString() {
    return suit;
  }

}

