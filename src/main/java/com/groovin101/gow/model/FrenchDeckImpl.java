package com.groovin101.gow.model;

import java.util.*;

/**
 * Represents a standard 52 card "French Style" deck
 * <p/>
 * See <a href="http://en.wikipedia.org/wiki/French_deck">French Deck</a>
 */
public class FrenchDeckImpl implements Deck {

    private Deque<Card> availableCards;
    private Deque<Card> dealtCards;

    /**
     * CONSTRUCTOR
     */
    public FrenchDeckImpl() {
        create(4, 13);
    }

    /**
     * Will do the expected if a typical 52 card deck (french style) is intended. This implementation does not support
     * partial decks. If anything other than a 52 card deck is attempted to be created, an IllegalArgumentException will
     * be thrown
     *
     * @param numberOfSuits - the number of suits desired
     * @param numberOfRanks - the number of ranks desired
     * @throws IllegalArgumentException - thrown if a client attempts to instantiate a non-french-style deck
     */
    @Override
    public void create(int numberOfSuits, int numberOfRanks) {
        if (numberOfSuits != 4) {
            throw new IllegalArgumentException("A French Deck must have 4 suits");
        }
        if (numberOfRanks != 13) {
            throw new IllegalArgumentException("A French Deck must have 13 ranks");
        }
        initializeAvailableCards();
        dealtCards = new ArrayDeque<Card>();
    }

    @Override
    public void shuffle() {
        List<Card> availableCardsInDeck = availableCardsAsList();
        Collections.shuffle(availableCardsInDeck);
        setAvailableCardsAfterShuffling(availableCardsInDeck);
    }

    @Override
    public Card deal() {
        Card dealt = availableCards.pop();
        dealtCards.push(dealt);
        return dealt;
    }

    protected int countAvailableCards() {
        return availableCards.size();
    }

    protected int countDealtCards() {
        return dealtCards.size();
    }

//    public Card[] deal(int numberOfCardsToDeal) {
//        Card[] dealt = new Card[52];
//        int cardIndex = 0;
//        while (numberOfCardsToDeal > 0) {
//            dealt[cardIndex++] = this.deal();
//        }
//        return dealt;
//    }

    protected List<Card> availableCardsAsList() {
        List<Card> cardsAsList = new ArrayList<Card>();
        for (Card availableCard : availableCards) {
            cardsAsList.add(availableCard);
        }
        return cardsAsList;
    }

    private void initializeAvailableCards() {
        availableCards = new ArrayDeque<Card>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank rank : CardRank.values()) {
                availableCards.push(new Card(rank, suit));
            }
        }
    }

    private void setAvailableCardsAfterShuffling(List<Card> availableCardsInShuffledForm) {
        availableCards = new ArrayDeque<Card>(availableCardsInShuffledForm);
    }
}
