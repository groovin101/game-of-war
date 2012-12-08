package com.groovin101.gow.model;

import java.util.*;

/**
 * Represents a standard 52 card "French Style" deck
 * <p/>
 * See <a href="http://en.wikipedia.org/wiki/French_deck">French Deck</a>
 */
public class DeckImpl implements DeckExtended {

    private Deque<Card> availableCards;
    private Deque<Card> dealtCards;

    /**
     * CONSTRUCTOR
     */
    public DeckImpl() {
        create(4, 13);
    }

    @Override
    public void create(int numberOfSuits, int numberOfRanks) {
        availableCards = new ArrayDeque<Card>();
        dealtCards = new ArrayDeque<Card>();
        for (CardSuit suit : determineWhichSuitsToCreate(numberOfSuits)) {
            for (CardRank rank : determineWhichRanksToCreate(numberOfRanks)) {
                    availableCards.push(new Card(rank, suit));
            }
        }
    }

    protected List<CardSuit> determineWhichSuitsToCreate(int numberOfSuitsToCreate) {
        List<CardSuit> suits = new ArrayList<CardSuit>();
        int i = 0;
        for (CardSuit suit : CardSuit.values()) {
            if (i < numberOfSuitsToCreate) {
                suits.add(suit);
            }
            else {
                break;
            }
            i++;
        }
        return suits;
    }

    protected List<CardRank> determineWhichRanksToCreate(int numberOfRanksToCreate) {
        List<CardRank> ranks = new ArrayList<CardRank>();
        int i = 0;
        for (CardRank rank : CardRank.values()) {
            if (i < numberOfRanksToCreate) {
                ranks.add(rank);
            }
            else {
                break;
            }
            i++;
        }
        return ranks;
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

    @Override
    public boolean hasNext() {
        return true;
    }

    protected int countAvailableCards() {
        return availableCards.size();
    }

    protected int countDealtCards() {
        return dealtCards.size();
    }

    protected List<Card> availableCardsAsList() {
        List<Card> cardsAsList = new ArrayList<Card>();
        for (Card availableCard : availableCards) {
            cardsAsList.add(availableCard);
        }
        return cardsAsList;
    }

    private void setAvailableCardsAfterShuffling(List<Card> availableCardsInShuffledForm) {
        availableCards = new ArrayDeque<Card>(availableCardsInShuffledForm);
    }
}

//    public Card[] deal(int numberOfCardsToDeal) {
//        Card[] dealt = new Card[52];
//        int cardIndex = 0;
//        while (numberOfCardsToDeal > 0) {
//            dealt[cardIndex++] = this.deal();
//        }
//        return dealt;
//    }

