package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

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
        player.dealToTopOfPlayersDeck(cardForTestA);
        assertEquals(1, player.getPlayerDeckSize());
    }

    @Test
    public void testAddToTopOfPlayerDeck_orderIsCorrect() {
        player.dealToTopOfPlayersDeck(cardForTestB);
        player.dealToTopOfPlayersDeck(cardForTestA);
        assertEquals(cardForTestA, player.playACard());
    }

    @Test
    public void testAddToBottomOfPlayerDeck_orderIsCorrect() {
        player.addToBottomOfPlayerDeck(cardForTestB);
        player.addToBottomOfPlayerDeck(cardForTestA);
        assertEquals(cardForTestB, player.playACard());
    }

    @Test
    public void testPlayACard_singleCard() {
        player.dealToTopOfPlayersDeck(cardForTestA);
        assertEquals(cardForTestA, player.playACard());
    }

    @Test
    public void testPlayACard_emptyHandReturnsNoCard() {
        assertNull(player.playACard());
    }

    @Test
    public void testPlayACard_multipleCards() {
        player.dealToTopOfPlayersDeck(cardForTestA);
        player.dealToTopOfPlayersDeck(cardForTestB);
        List<Card> cardsThatWereExpectedToBePlayed = new ArrayList<Card>();
        cardsThatWereExpectedToBePlayed.add(cardForTestA);
        cardsThatWereExpectedToBePlayed.add(cardForTestB);
        assertTrue("Should contain all two of our cards since we played both", player.playCards(2).containsAll(cardsThatWereExpectedToBePlayed));
    }

    @Test
    public void testPlayACard_requestingMoreCardsThanWeHaveReturnsWhatWeDoHave() {
        player.dealToTopOfPlayersDeck(cardForTestA);
        List<Card> cardsThatWereExpectedToBePlayed = new ArrayList<Card>();
        cardsThatWereExpectedToBePlayed.add(cardForTestA);
        assertTrue("Should contain a single card even though we specified to deal 2", player.playCards(2).containsAll(cardsThatWereExpectedToBePlayed));
    }
}