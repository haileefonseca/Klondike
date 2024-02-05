package cs3500.klondike.model.hw02;


/**
 * Class that represents a card to be used in a cs3500.klondike.Klondike game.
 */
public class KlondikeCard implements Card {
  private final Suit suit;
  private final Rank rank;

  /**
   * Constructor for a card in a cs3500.klondike.Klondike game. All cards default to be facing down.
   * @param suit the suit of the card
   * @param rank the rank of the card, 1 being Ace, 11 being Jack, 12 being Queen, 13 being King
   */
  public KlondikeCard(Suit suit, Rank rank) {
    if (suit == null) {
      throw new IllegalArgumentException("Invalid suit");
    }
    this.suit = suit;
    if (rank == null) {
      throw new IllegalArgumentException("Invalid rank");
    }
    this.rank = rank;
  }

  @Override
  public String toString() {
    return this.rank.toString() + this.suit.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof KlondikeCard) {
      KlondikeCard that = (KlondikeCard)other;
      return that.suit.equals(this.suit) && that.rank == this.rank;
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (this.suit.equals(Suit.SPADE)) {
      return this.rank.toInt() * 5 + 1;
    }
    if (this.suit.equals(Suit.DIAMOND)) {
      return this.rank.toInt() * 5 + 2;
    }
    if (this.suit.equals(Suit.HEART)) {
      return this.rank.toInt() * 5 + 3;
    }
    return this.rank.toInt() * 5 + 4;
  }

  @Override
  public int numerical() {
    return this.rank.toInt();
  }

  @Override
  public boolean isRed() {
    return this.suit.equals(Suit.DIAMOND) || this.suit.equals(Suit.HEART);
  }

  @Override
  public Suit suit() {
    return this.suit;
  }
}

