package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 */
public class PlayerTest {

    private Player player;
    private Card cardForTestA;
    private Card cardForTestB;

    @Before
    public void setup() throws Exception {
        player = new Player("Chewbacca");
        cardForTestA = new Card(Rank.ACE, Suit.CLUB);
        cardForTestB = new Card(Rank.EIGHT, Suit.SPADE);
    }

    @Test
    public void testInstantiation_playerMustNotHaveANullName() {
        try {
            new Player(null);
            fail("Should have told us that this player needs a name");
        }
        catch (InvalidUsernameException e) {
        }
    }

    @Test
    public void testInstantiation_playerMustNotHaveAnEmptyStringForAName() {
        try {
            new Player("       ");
            fail("Should have told us that this player needs a name");
        }
        catch (InvalidUsernameException e) {
        }
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