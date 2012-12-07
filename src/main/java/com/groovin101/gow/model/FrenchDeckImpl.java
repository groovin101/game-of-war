package com.groovin101.gow.model;

import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.Deck;
import com.groovin101.gow.model.ExtendedDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a standard 52 card "French Style" deck
 *
 * See <a href="http://en.wikipedia.org/wiki/French_deck">French Deck</a>
 */
public class FrenchDeckImpl implements ExtendedDeck {

    List<Card> cardsInDeck;

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
        initializeDeck();
    }

    private void initializeDeck() {
        cardsInDeck = new ArrayList<Card>();
        cardsInDeck.add(new Card(CardRank.ACE, CardSuit.CLUB));
        cardsInDeck.add(new Card(CardRank.TWO, CardSuit.CLUB));
        for (int i=0;i<50;i++) {
            cardsInDeck.add(new Card(CardRank.ACE, CardSuit.CLUB));
        }
    }

    @Override
    public void shuffle() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Card deal() {
        return cardsInDeck.remove(0);
    }

    public List<Card> getCards() {
        return cardsInDeck;
    }
}
