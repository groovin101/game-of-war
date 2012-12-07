package com.groovin101.gow.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Represents a standard 52 card "French Style" deck
 *
 * See <a href="http://en.wikipedia.org/wiki/French_deck">French Deck</a>
 */
public class FrenchDeckImpl implements ExtendedDeck {

    private Deque<Card> availableCards;
    private Deque<Card> dealtCards;

    public FrenchDeckImpl(int numberOfSuits, int numberOfRanks) {
        create(numberOfSuits, numberOfRanks);
    }

    /**
     * Will do the expected if a typical 52 card deck (french style) is intended. This implementation does not support
     * partial decks. If anything other than a 52 card deck is attempted to be created, an IllegalArgumentException will
     * be thrown
     * @param numberOfSuits - the number of suits desired
     * @param numberOfRanks - the number of ranks desired
     * @exception IllegalArgumentException - thrown if a client attempts to instantiate a non-french-style deck
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

    private void initializeAvailableCards() {
        availableCards = new ArrayDeque<Card>();
        CardRank[] ranks = CardRank.values();
        CardSuit[] suits = CardSuit.values();
        for (int i=0;i<suits.length;i++) {
            for (int j=0;j<ranks.length;j++) {
                availableCards.push(new Card(ranks[j], suits[i]));
            }
        }
    }

    @Override
    public void shuffle() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Card deal() {
        return availableCards.pop();
    }

    @Override
    public int countAvailableCards() {
        return availableCards.size();
    }

    @Override
    public int countDealtCards() {
        return dealtCards.size();
    }
}
