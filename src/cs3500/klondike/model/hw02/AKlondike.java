package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * abstract class implements the basic functionality of a game of Klondike.
 * Protected methods can be overriden by subclasses to change features of the game;
 * in this implementation I chose to only make methods protected if they were needed for
 * LimitedDraw or WhiteHead and left the others private, but any additional versions may require
 * more helpers to be slightly more visible.
 */
public abstract class AKlondike implements KlondikeModel {
  protected boolean gameStarted;

  // Each element in this list is a cascade pile, and the stack at that index contains the cards
  //in that pile. the first element is the card at the "top" of the deck, as in first dealt.
  protected final List<Stack<Card>> gameTable;

  // Each element in this list is a foundation pile, and each elements stack contains the contents.
  // the first element of the stack is the first card down, must be an Ace.
  protected final List<Stack<Card>> foundation;

  // list of integers representing how many cards are visible in each cascade pile
  protected final List<Integer> visibleCards;
  protected int maxDraw;

  // cards currently visible in the draw
  protected List<Card> currentDraw;

  // cards to be cycled through the draw
  protected List<Card> restOfDraw;
  private List<Card> aces;
  private Card highestRank;

  /**
   * constructor for an Abstract Klondike game, responsible initializing fields.
   */
  public AKlondike() {
    this.gameStarted = false;
    this.gameTable = new ArrayList<Stack<Card>>();
    this.foundation = new ArrayList<Stack<Card>>();
    this.visibleCards = new ArrayList<Integer>();
  }

  @Override
  public List<Card> getDeck() {
    List<Suit> suits = Arrays.asList(Suit.SPADE, Suit.DIAMOND, Suit.HEART, Suit.CLUB);
    List<Rank> ranks = Arrays.asList(Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX,
            Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING);
    List<Card> result = new ArrayList<>();
    for (Suit suit : suits) {
      for (Rank rank : ranks) {
        result.add(new KlondikeCard(suit, rank));
      }
    }
    return result;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    checkStartConditions(deck, numPiles, numDraw);
    List<Card> aces = new ArrayList<>();
    for (Card c : deck) {
      if (c.toString().charAt(0) == 'A') {
        aces.add(c);
      }
    }
    if (aces.isEmpty()) {
      throw new IllegalArgumentException("Invalid deck, no aces");
    }
    this.aces = aces;
    List<Card> sortedDeck = sortedDeck(deck);
    this.highestRank = sortedDeck.get(sortedDeck.size() - 1);
    validateConsecutiveEqual(deck);
    if (shuffle) {
      Collections.shuffle(deck);
    }
    initializePiles(deck, numPiles, numDraw, aces);
    this.gameStarted = true;
  }

  private void initializePiles(List<Card> deck, int numPiles, int numDraw, List<Card> aces) {
    for (int i = 0; i < numPiles; i++) {
      this.gameTable.add(new Stack<Card>());
      this.visibleCards.add(1);
    }
    for (int i = 0; i < aces.size(); i++) {
      this.foundation.add(new Stack<Card>());
    }
    List<Card> copyDeck = new ArrayList<>(deck);
    for (int row = 0; row < numPiles; row++) {
      for (int stack = row; stack < numPiles; stack++) {
        this.gameTable.get(stack).add(copyDeck.remove(0));
      }
    }
    this.maxDraw = numDraw;
    this.currentDraw = new ArrayList<Card>();
    for (int i = 0; i < maxDraw; i++) {
      if (!(copyDeck.isEmpty())) {
        this.currentDraw.add(copyDeck.remove(0));
      }
    }
    this.restOfDraw = new ArrayList<Card>();
    this.restOfDraw.addAll(copyDeck);
  }

  private void checkStartConditions(List<Card> deck, int numPiles, int numDraw) {
    if (this.gameStarted) {
      throw new IllegalStateException("Game has already been started");
    }
    if (numPiles <= 0 || numDraw <= 0) {
      throw new IllegalArgumentException("Invalid number of piles");
    }
    if (deck == null) {
      throw new IllegalArgumentException("Null deck");
    }
    if (((numPiles * (numPiles + 1.0)) / 2.0) > deck.size()) {
      throw new IllegalArgumentException("Deck is not big enough for cascade");
    }
    for (Card c : deck) {
      if (c == null) {
        throw new IllegalArgumentException("Null card in deck");
      }
    }
  }

  // checks the deck is consecutive and equal length
  private void validateConsecutiveEqual(List<Card> deck) {
    List<Card> sortDeck = sortedDeck(deck);
    HashMap<Card, Integer> countSpade = new HashMap<>();
    HashMap<Card, Integer> countClub = new HashMap<>();
    HashMap<Card, Integer> countDiamond = new HashMap<>();
    HashMap<Card, Integer> countHeart = new HashMap<>();
    List<Card> spades = allSuit("♠", sortDeck);
    List<Card> clubs = allSuit("♣", sortDeck);
    List<Card> diamonds = allSuit("♢", sortDeck);
    List<Card> hearts = allSuit("♡", sortDeck);

    validCEHelp(countSpade, spades);
    validCEHelp(countClub, clubs);
    validCEHelp(countDiamond, diamonds);
    validCEHelp(countHeart, hearts);

    List<Integer> highestValue = new ArrayList<>();
    List<List<Card>> allCards = Arrays.asList(spades, clubs, diamonds, hearts);
    for (List<Card> cardList : allCards) {
      if (!(cardList.isEmpty())) {
        highestValue.add(cardList.get(cardList.size() - 1).numerical());
      }
    }

    for (int highVals = 0; highVals < highestValue.size() - 1; highVals++) {
      if (highestValue.get(highVals) != highestValue.get(highVals + 1)) {
        throw new IllegalArgumentException("Runs not of same length");
      }
    }
  }

  // helper for validateConsecutiveEqual, creates and tests hashmaps
  private void validCEHelp(HashMap<Card, Integer> map, List<Card> cards) {
    checkConsecutive(cards);
    boolean hasAce = false;
    for (Card card : cards) {
      map.merge(card, 1, Integer::sum);
      if (card.numerical() == 1) {
        hasAce = true;
      }
    }
    if (!cards.isEmpty() && !hasAce) {
      throw new IllegalArgumentException("No ace for this suit");
    }
    for (int currentCard = 0; currentCard < cards.size() - 1; currentCard++) {
      if (!(map.get(cards.get(currentCard)).equals(map.get(cards.get(currentCard + 1))))) {
        throw new IllegalArgumentException("Count of cards is not consistent");
      }
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    startChecker();
    validPile(this.gameTable, srcPile);
    validPile(this.gameTable, destPile);
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Source and destination are the same");
    }
    if (this.gameTable.get(srcPile).size() < numCards || numCards < 0) {
      throw new IllegalArgumentException("Invalid card number");
    }
    int cardHeight = this.gameTable.get(srcPile).size() - numCards;
    if (!(this.isCardVisible(srcPile, cardHeight))) {
      throw new IllegalArgumentException("Card is not visible");
    }
    for (int i = 0; i < numCards; i++) {
      moveCascade(this.gameTable.get(srcPile), this.gameTable.get(destPile), cardHeight);
    }
    this.visibleCards.set(destPile, this.visibleCards.get(destPile) + numCards);
    this.visibleCards.set(srcPile, this.visibleCards.get(destPile) - numCards);
    if (this.visibleCards.get(srcPile) < 1 && !this.gameTable.get(srcPile).isEmpty()) {
      this.visibleCards.set(srcPile, 1);
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    startChecker();
    checkEmpty(this.currentDraw);
    validPile(this.gameTable, destPile);
    moveCascade(this.currentDraw, this.gameTable.get(destPile), 0);
    refreshDraw();
    this.visibleCards.set(destPile, this.visibleCards.get(destPile) + 1);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalStateException {
    startChecker();
    validPile(this.gameTable, srcPile);
    validPile(this.foundation, foundationPile);
    checkEmpty(this.gameTable.get(srcPile));
    moveFoundation(this.gameTable.get(srcPile), this.foundation.get(foundationPile),
            this.gameTable.get(srcPile).size() - 1);
    this.visibleCards.set(srcPile, this.visibleCards.get(srcPile) + 1);
    if (this.visibleCards.get(srcPile) < 1 && !this.gameTable.get(srcPile).isEmpty()) {
      this.visibleCards.set(srcPile, 1);
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalArgumentException,
          IllegalStateException {
    startChecker();
    validPile(this.foundation, foundationPile);
    checkEmpty(this.currentDraw);
    moveFoundation(this.currentDraw, this.foundation.get(foundationPile), 0);
    refreshDraw();
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    startChecker();
    if (this.currentDraw.isEmpty() && this.restOfDraw.isEmpty()) {
      throw new IllegalStateException("No more cards to discard");
    }
    this.restOfDraw.add(this.currentDraw.remove(0));
    refreshDraw();
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    startChecker();
    int longestRow = 0;
    for (Stack<Card> stack : this.gameTable) {
      if (stack.size() > longestRow) {
        longestRow = stack.size();
      }
    }
    return longestRow;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    startChecker();
    return this.gameTable.size();
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    startChecker();
    return this.maxDraw;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    startChecker();
    if (!this.currentDraw.isEmpty()) {
      return false;
    }
    boolean emptyDraw = this.currentDraw.isEmpty() && this.restOfDraw.isEmpty();
    boolean allEmptyPiles = true;
    for (int stack = 0; stack < this.gameTable.size(); stack++) {
      if (!this.gameTable.get(stack).isEmpty()) {
        allEmptyPiles = false;
      }
    }
    if (allEmptyPiles) {
      return true;
    }

    int legalMoves = 0;
    legalMoves = checkForLegalCascadeMoves(legalMoves);
    legalMoves = checkForLegalMovesToFoundation(legalMoves);
    return legalMoves == 0 && emptyDraw;
  }


  protected int checkForLegalCascadeMoves(int legalMoves) {
    for (int stackPlay = 0; stackPlay < this.getNumPiles(); stackPlay++) {
      for (int rowPlay = 0; rowPlay < this.gameTable.get(stackPlay).size(); rowPlay++) {
        for (int stackTry = 0; stackTry < this.getNumPiles(); stackTry++) {
          if (stackPlay == stackTry) {
            break;
          }
          List<Card> cardSource = new ArrayList<>(this.gameTable.get(stackPlay));
          List<Card> cardDest = new ArrayList<>(this.gameTable.get(stackTry));
          try {
            if (this.isCardVisible(stackPlay, rowPlay)
                    && this.isCardVisible(stackTry, cardDest.size() - 1)) {
              moveCascade(cardSource, cardDest, rowPlay);
              legalMoves += 1;
            }
          } catch (IllegalStateException ignored) {
          }
        }
      }
    }
    return legalMoves;
  }

  //adds the number of legal moves to foundation to legalMoves
  protected int checkForLegalMovesToFoundation(int legalMoves) {
    for (int stack = 0; stack < this.gameTable.size(); stack++) {
      List<Card> cardSource = new ArrayList<>(this.gameTable.get(stack));
      List<Stack<Card>> copyFoundation = new ArrayList<>(this.foundation);
      if (!cardSource.isEmpty()) {
        for (int foundPile = 0; foundPile < copyFoundation.size(); foundPile++) {
          try {
            moveFoundation(cardSource,
                    copyFoundation.get(foundPile), cardSource.size() - 1);
            legalMoves += 1;
          } catch (IllegalStateException ignored) {
          }
        }
      }
    }
    return legalMoves;
  }

  @Override
  public int getScore() throws IllegalStateException {
    startChecker();
    int score = 0;
    for (Stack<Card> stack : this.foundation) {
      if (!(stack.isEmpty())) {
        score += stack.size();
      }
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    startChecker();
    validPile(this.gameTable, pileNum);
    if (this.gameTable.get(pileNum).isEmpty()) {
      return 0;
    }
    return this.gameTable.get(pileNum).size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalArgumentException,
          IllegalStateException {
    startChecker();
    validPile(this.gameTable, pileNum);
    validateCard(pileNum, card);
    int invisible = this.getPileHeight(pileNum) - this.visibleCards.get(pileNum);
    return card >= invisible;
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalArgumentException,
          IllegalStateException {
    startChecker();
    validPile(this.gameTable, pileNum);
    validateCard(pileNum, card);
    if (!(this.isCardVisible(pileNum, card))) {
      throw new IllegalArgumentException("Card not visible");
    }
    return this.gameTable.get(pileNum).get(card);
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException,
          IllegalStateException {
    startChecker();
    validPile(this.foundation, foundationPile);
    if (this.foundation.get(foundationPile).isEmpty()) {
      return null;
    }
    return this.foundation.get(foundationPile).lastElement();
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    startChecker();
    return new ArrayList<>(this.currentDraw);
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    startChecker();
    return this.aces.size();
  }

  // checks if the game has been started, throws if it has not been.
  protected void startChecker() throws IllegalStateException {
    if (!(this.gameStarted)) {
      throw new IllegalStateException("Game has not been started");
    }
  }

  // checks if these card coordinates are valid in cascade piles, throws if it is not
  private void validateCard(int pileNum, int card) throws IllegalArgumentException {
    if (card < 0) {
      throw new IllegalArgumentException("No negative cards");
    }
    if (this.gameTable.get(pileNum).size() <= card) {
      throw new IllegalArgumentException("Invalid card number");
    }
  }

  // checks if the pile is empty, throws if it is
  private void checkEmpty(List<Card> pile) throws IllegalStateException {
    if (pile.isEmpty()) {
      throw new IllegalStateException("Pile is empty");
    }
  }

  // checks if this pileNum is valid in given pile, throws if it is not
  protected void validPile(List<Stack<Card>> pile, int pileNum) throws IllegalArgumentException {
    if (pile.size() <= pileNum || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile");
    }
  }

  // checks if this move is legal for cascade piles
  protected boolean legalPlay(Card desCard, Card sourceCard) {
    if (desCard.isRed() && !sourceCard.isRed()) {
      return oneLarger(desCard, sourceCard);
    }
    if (!desCard.isRed() && sourceCard.isRed()) {
      return oneLarger(desCard, sourceCard);
    }
    return false;
  }

  // checks if this move is legal for a foundation pile
  protected boolean legalFoundation(Card desCard, Card sourceCard) {
    for (Card c : this.aces) {
      String suit = c.toString().substring(1);
      if (desCard.toString().contains(suit)
              && sourceCard.toString().contains(suit)) {
        return desCard.numerical() + 1 == sourceCard.numerical();
      }
    }
    return false;
  }

  // refills the draw pile if it is empty
  protected void refreshDraw() {
    for (int draw = 0; draw < maxDraw - this.currentDraw.size(); draw++) {
      if (!(this.restOfDraw.isEmpty())) {
        this.currentDraw.add(this.restOfDraw.remove(0));
      }
    }
  }

  /**
   * Moves the card at the given coordinate from the given source pile to
   * a given destination pile within cascades.
   *
   * @param source      the List of card source pile to move the card from
   * @param destination the list of card destination pile for the card to go to
   * @param cardHeight  the height of the card to be moved, 0-indexed from bottom
   * @throws IllegalStateException if a card that is not the highest in the run is moved to an
   *                               empty pile, or the move is otherwise illegal.
   */
  protected void moveCascade(List<Card> source, List<Card> destination, int cardHeight) throws
          IllegalStateException {
    if (destination.isEmpty()) {
      if (source.get(cardHeight).numerical() == this.highestRank.numerical()) {
        destination.add(source.remove(cardHeight));
      } else {
        throw new IllegalStateException("Can only move the highest card into an empty space");
      }
    }
    else if (legalPlay(destination.get(destination.size() - 1), source.get(cardHeight))) {
      destination.add(source.remove(cardHeight));
    }
    else {
      throw new IllegalStateException(("Illegal play"));
    }
  }

  /**
   * Moves the card at the given coordinate from the given source pile to
   * a given destination pile in foundation.
   *
   * @param source      the List of card source pile to move the card from
   * @param destination the list of card destination pile for the card to go to
   * @param cardHeight  the height of the card to be moved
   * @throws IllegalStateException if an ace is moved into an empty foundation pile or the move
   *                               is otherwise illegal
   */
  private void moveFoundation(List<Card> source, List<Card> destination, int cardHeight) throws
          IllegalStateException {
    if (destination.isEmpty()) {
      if (source.get(cardHeight).toString().charAt(0) == 'A') {
        destination.add(source.remove(cardHeight));
      } else {
        throw new IllegalStateException("Can only move an ace into an empty foundation");
      }
    } else if (legalFoundation(destination.get(destination.size() - 1),
            source.get(cardHeight))) {
      destination.add(source.remove(cardHeight));
    } else {
      throw new IllegalStateException("illegal play");
    }
  }

  // checks if Card c1 has a numerical value one larger than c2
  private boolean oneLarger(Card c1, Card c2) {
    return c1.numerical() - 1 == c2.numerical();
  }

  // checks if all elements in this list are consecutive, throws if they are not
  private void checkConsecutive(List<Card> cards) {
    for (int currentCard = 0; currentCard < cards.size() - 1; currentCard++) {
      if (!(cards.get(currentCard).numerical() == cards.get(currentCard + 1).numerical()
              || cards.get(currentCard).numerical() + 1
              == cards.get(currentCard + 1).numerical())) {
        throw new IllegalArgumentException("Invalid deck");
      }
    }
  }

  // returns a new list of the original list of cards, sorted by numerical value
  private List<Card> sortedDeck(List<Card> original) {
    List<Card> copyDeck = new ArrayList<>(original);
    for (int i = 0; i < copyDeck.size() - 1; i++) {
      for (int j = i + 1; j > 0; j--) {
        if (copyDeck.get(j).numerical() < copyDeck.get(j - 1).numerical()) {
          Collections.swap(copyDeck, j, j - 1);
        } else {
          break;
        }
      }
    }
    return copyDeck;
  }

  // returns a new list of only the given suit
  private List<Card> allSuit(String suit, List<Card> deck) {
    List<Card> res = new ArrayList<>();
    for (Card card : deck) {
      if (card.toString().contains(suit)) {
        res.add(card);
      }
    }
    return res;
  }
}
