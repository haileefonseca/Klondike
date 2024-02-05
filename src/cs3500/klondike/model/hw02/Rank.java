package cs3500.klondike.model.hw02;

/**
 * Class that represents possible ranks in a deck of cards.
 */
public enum Rank {
  ACE("A"), TWO("2"), THREE( "3"), FOUR("4"),
  FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
  NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K");

  private final String stringRep;

  /**
   * constructor for a Rank enum with its value as a string.
   * @param stringRep string to represent the cards rank, either a number or A, J, Q, K
   */
  Rank(String stringRep) {
    this.stringRep = stringRep;
  }

  @Override
  public String toString() {
    return stringRep;
  }


  /**
   * Converts this rank to its integer value,
   * Ace being 1, jack being 11, queen being 12, and king being 13.
   * @return an integer representation of this rank
   */
  public int toInt() {
    return this.ordinal() + 1;
  }
}
