package com.groovin101.gow.model;

import com.groovin101.gow.exception.NoCardsLeftException;

import java.util.*;

/**
 * Represents a standard 52 card "French Style" deck
 * <p/>
 * See <a href="http://en.wikipedia.org/wiki/French_deck">French Deck</a>
 */
public class DeckImpl implements DeckExtended {

    private Deque<Card> availableCards;
    private Deque<Card> dealtCards;
    private int totalCardCount;

    /**
     * CONSTRUCTOR
     */
    public DeckImpl() {
        create(4, 13);
    }

    @Override
    public void create(int numberOfSuits, int numberOfRanks) {
        validateNumberOfSuitsToCreate(numberOfSuits);
        validateNumberOfRanksToCreate(numberOfRanks);
        initializeAvailableCards(numberOfSuits, numberOfRanks);
        initializeDealtCards();
        totalCardCount = availableCards.size();
    }

    private void validateNumberOfSuitsToCreate(int numberOfSuits) {
        if (numberOfSuits > 4) {
            throw new IllegalArgumentException("Can only have up to 4 suits in a single deck");
        }
    }

    private void validateNumberOfRanksToCreate(int numberOfRanks) {
        if (numberOfRanks > 13) {
            throw new IllegalArgumentException("Can only have up to 13 ranks in a single deck");
        }
    }

    private void initializeAvailableCards(int numberOfSuits, int numberOfRanks) {
        availableCards = new ArrayDeque<Card>();
        for (Suit suit : determineWhichSuitsToCreate(numberOfSuits)) {
            for (Rank rank : determineWhichRanksToCreate(numberOfRanks)) {
                    availableCards.push(new Card(rank, suit));
            }
        }
    }

    private void initializeDealtCards() {
        dealtCards = new ArrayDeque<Card>();
    }

    protected List<Suit> determineWhichSuitsToCreate(int numberOfSuitsToCreate) {
        List<Suit> suits = new ArrayList<Suit>();
        int i = 0;
        for (Suit suit : Suit.values()) {
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

    protected List<Rank> determineWhichRanksToCreate(int numberOfRanksToCreate) {
        List<Rank> ranks = new ArrayList<Rank>();
        int i = 0;
        for (Rank rank : Rank.values()) {
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
        try {
            Card dealt = availableCards.pop();
            dealtCards.push(dealt);
            return dealt;
        }
        catch (NoSuchElementException e) {
            throw new NoCardsLeftException("There are no more cards left in the deck");
        }
    }

    @Override
    public boolean hasMoreCardsAvailable() {
        return !availableCards.isEmpty();
    }

    @Override
    public int getTotalCardCount() {
        return totalCardCount;
    }

    public List<Card> deal(int howMany) {

        List<Card> dealt = new ArrayList<Card>();
        while (howMany > 0) {
            dealt.add(deal());
            howMany--;
        }
        return dealt;
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

