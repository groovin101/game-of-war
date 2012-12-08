package com.groovin101.gow.model;

import com.groovin101.gow.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 */
public class FrenchDeckTest {

    private FrenchDeckImpl cardDeck;

    @Before
    public void setup() {
        cardDeck = new FrenchDeckImpl();
    }

    @Test
    public void testCreate_invalidNumberOfSuitsThrowsException() {
        try {
            cardDeck.create(0, 13);
            fail("Expected an IllegalArgumentException to be thrown since this is an invalid number of suits for a French Deck");
        }
        catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCreate_invalidNumberOfRanksThrowsException() {
        try {
            cardDeck.create(4, 0);
            fail("Expected an IllegalArgumentException to be thrown since this is an invalid number of suits for a French Deck");
        }
        catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCreate_has52AvailableCards() {
        callCreateOnFrenchDeckAppropriately();
        assertEquals("Not right amount of cards", 52, cardDeck.countAvailableCards());
    }

    @Test
    public void testCreate_cardsAreUnique() {
        callCreateOnFrenchDeckAppropriately();
        Set<Card> uniqueCards = new HashSet<Card>();
        while (cardDeck.countAvailableCards() > 0) {
            uniqueCards.add(cardDeck.deal());
        }
        assertEquals("Should be 52 unique cards", 52, uniqueCards.size());
    }

    @Test
    public void testCreate_hasNoDealtCards() {
        callCreateOnFrenchDeckAppropriately();
        assertEquals(0, cardDeck.countDealtCards());
    }

    @Test
    public void testDeal_unshuffledDeckFirstCard() {
        Card firstCardInDeck = cardDeck.deal();
        assertEquals("The ace of clubs should be the first card", new Card(CardRank.ACE, CardSuit.CLUB), firstCardInDeck);
    }

    @Test
    public void testDeal_unshuffledDeckSecondCard() {
        cardDeck.deal();
        Card secondCard = cardDeck.deal();
        assertEquals("The two of clubs should be the second card", new Card(CardRank.TWO, CardSuit.CLUB), secondCard);
    }

    @Test
    public void testDeal_decrementsAvailableCards() {
        assertEquals(52, cardDeck.countAvailableCards());
        cardDeck.deal();
        assertEquals("Should be one less card available in the deck", 51, cardDeck.countAvailableCards());
    }

    @Test
    public void testDeal_incrementsDealtCards() {
        cardDeck.deal();
        assertEquals("Should have dealt a card", 1, cardDeck.countDealtCards());
    }

    @Test
    public void testAvailableCardsAsList_returnsCorrectNumberOfCards() {
        assertEquals(52, cardDeck.availableCardsAsList().size());
    }

    @Test
    public void testAvailableCardsAsList_returnsCorrectNumberOfCardsAfterDealing() {
        cardDeck.deal();
        assertEquals(51, cardDeck.availableCardsAsList().size());
    }

    @Test
    public void testShuffle_listsAreInDifferentOrderAfterShuffling() {
        List<Card> originalList = cardDeck.availableCardsAsList();
        cardDeck.shuffle();
        List<Card> shuffledList = cardDeck.availableCardsAsList();
        assertNotSame(originalList, shuffledList);
        assertFalse("Lists of available cards should not be in same order after shuffling", originalList.equals(shuffledList));
    }

    @Test
    public void testShuffle_listsContainSameContentsAfterShuffling() {
        List<Card> originalList = cardDeck.availableCardsAsList();
        cardDeck.shuffle();
        List<Card> shuffledList = cardDeck.availableCardsAsList();
        assertNotSame(originalList, shuffledList);
        assertTrue("Lists should have same content after shuffling", originalList.containsAll(shuffledList));
    }

    private void callCreateOnFrenchDeckAppropriately() {
        cardDeck.create(4, 13);
    }
}