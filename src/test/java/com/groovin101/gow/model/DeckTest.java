package com.groovin101.gow.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.*;

/**
 */
public class DeckTest {

    private DeckImpl cardDeck;

    @Before
    public void setup() {
        cardDeck = new DeckImpl();
    }

    @Test
    public void testCreate_has52AvailableCards() {
        callCreateOnFrenchDeckAppropriately();
        assertEquals("Not right amount of cards", 52, cardDeck.countAvailableCards());
    }

    @Test
    public void testDetermineWhichSuitsToCreate_returnsCorrectNumberOfSuits() {
        assertEquals(2, (cardDeck.determineWhichSuitsToCreate(2)).size());
    }

    @Test
    public void testDetermineWhichRanksToCreate_returnsCorrectNumberOfRanks() {
        assertEquals(9, cardDeck.determineWhichRanksToCreate(9).size());
    }

    @Test
    public void testDetermineWhichRanksToCreate_returnsActualRanks() {
        assertTrue("Should contain an Eight", cardDeck.determineWhichRanksToCreate(9).contains(Rank.EIGHT));
    }

    @Test
    public void testCreate_hasCorrectNumberOfSuits() {
        cardDeck.create(1, 1);
        assertEquals("Should have a single suit with one card", 1, cardDeck.countAvailableCards());
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
    public void testCreate_moreThank13Ranks() {
        try {
            cardDeck.create(4, 14);
            fail("Should have thrown an exception; there were too many ranks specified");
        }
        catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCreate_allowUpTo13Ranks() {
        try {
            cardDeck.create(4, 13);
        }
        catch (IllegalArgumentException e) {
            fail("Should have allowed creation because ranks specified was <= 13");
        }
    }

    @Test
    public void testCreate_moreThanFourSuits() {
        try {
            cardDeck.create(5, 1);
            fail("Should have thrown an exception; there were too many suits specified");
        }
        catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCreate_allowUpToFourSuits() {
        try {
            cardDeck.create(4, 1);
        }
        catch (IllegalArgumentException e) {
            fail("Should have allowed creation because suits specified was <= 13");
        }
    }
    @Test
    public void testCreate_hasNoDealtCardsOnInit() {
        callCreateOnFrenchDeckAppropriately();
        assertEquals(0, cardDeck.countDealtCards());
    }

    @Test
    public void testDeal_unshuffledDeckFirstCard() {
        Card firstCardInDeck = cardDeck.deal();
        assertEquals("The ace of clubs should be the first card", new Card(Rank.ACE, Suit.CLUB), firstCardInDeck);
    }

    @Test
    public void testDeal_unshuffledDeckSecondCard() {
        cardDeck.deal();
        Card secondCard = cardDeck.deal();
        assertEquals("The two of clubs should be the second card", new Card(Rank.TWO, Suit.CLUB), secondCard);
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

    @Test
    public void testHasMoreCards_yesWhenUsingAFullDeck() {
        assertTrue("Full deck should have a next card", cardDeck.hasMoreCards());
    }

    @Test
    public void testHasMoreCards_shouldNotWhenUsingAnEmptyDeck() {
        cardDeck.create(0, 0);
        assertFalse("Empty deck should not have a next card", cardDeck.hasMoreCards());
    }

//    @Test
//    public void testDeal_whenWeHaveNoCardsLeft() {
//        cardDeck.create(1, 1);
//        cardDeck.deal();
//        assertNull(cardDeck.deal());
//    }

    private void callCreateOnFrenchDeckAppropriately() {
        cardDeck.create(4, 13);
    }
}