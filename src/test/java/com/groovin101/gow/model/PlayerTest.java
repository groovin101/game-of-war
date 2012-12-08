package com.groovin101.gow.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 */
public class PlayerTest {

    private Player player;
    private Card cardForTestA;
    private Card cardForTestB;

    @Before
    public void setup() {
        player = new Player();
        cardForTestA = new Card(CardRank.ACE, CardSuit.CLUB);
        cardForTestB = new Card(CardRank.EIGHT, CardSuit.SPADE);
    }

    @Test
    public void testGetPlayerDeckSize_newPlayerDoesNotHaveADeck() {
        assertEquals(0, player.getPlayerDeckSize());
    }

    @Test
    public void testAddToBottomOfPlayerDeck_incrementsDeckSize() {
        player.addToBottomOfPlayerDeck(cardForTestA);
        assertEquals(1, player.getPlayerDeckSize());
    }

    @Test
    public void testAddToTopOfPlayerDeck_incrementsDeckSize() {
        player.addToTopOfPlayerDeck(cardForTestA);
        assertEquals(1, player.getPlayerDeckSize());
    }

    @Test
    public void testAddToTopOfPlayerDeck_orderIsCorrect() {
        player.addToTopOfPlayerDeck(cardForTestB);
        player.addToTopOfPlayerDeck(cardForTestA);
        assertEquals(cardForTestA, player.revealTopCardOfPlayerDeck());
    }

    @Test
    public void testAddToBottomOfPlayerDeck_orderIsCorrect() {
        player.addToBottomOfPlayerDeck(cardForTestB);
        player.addToBottomOfPlayerDeck(cardForTestA);
        assertEquals(cardForTestB, player.revealTopCardOfPlayerDeck());
    }

    @Test
    public void testRevealTopCard_singleCard() {
        player.addToTopOfPlayerDeck(cardForTestA);
        assertEquals(cardForTestA, player.revealTopCardOfPlayerDeck());
    }
}