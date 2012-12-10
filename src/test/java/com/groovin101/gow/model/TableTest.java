package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 */
public class TableTest extends BaseTest {

    private Table table;
    private Player felix;
    private Player oscar;
    private Card ace;
    private Card king;
    private List<Card> expectedCardsFromOscarsHand;
    private List<Card> expectedCardsFromFelixsHand;

    @Before
    public void setup() {
        super.setup();
        table = new Table();
        try {
            oscar = new Player("oscar");
            felix = new Player("felix");
        }
        catch (InvalidUsernameException e) {}
        ace = new Card(Rank.ACE, Suit.CLUB);
        king = new Card(Rank.KING, Suit.HEART);
        expectedCardsFromOscarsHand = new ArrayList<Card>();
        expectedCardsFromFelixsHand = new ArrayList<Card>();
    }

    @Test
    public void testRetrieveCardsDealtFrom_returnsNoCardsWhenNobodyHasPlayedACardYet() throws Exception {
        assertTrue("No hands have been played yet, so table should have none", table.retrieveCardsDealtFrom(oscar).isEmpty());
    }

    @Test
    public void testReceiveCardFrom_canHoldASingleCardPlayedByASinglePlayer() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(1));
        assertEquals("Should have the only card that was played by Oscar", expectedCardsFromOscarsHand, table.retrieveCardsDealtFrom(oscar));
    }

    @Test
    public void testReceiveCardFrom_canHoldMultipleCardsPlayedByASinglePlayer() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(oscar, king, expectedCardsFromOscarsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(1));
        table.receiveCardsFrom(oscar, oscar.playCards(1));
        assertTrue("Should have all cards played by Oscar", table.retrieveCardsDealtFrom(oscar).containsAll(expectedCardsFromOscarsHand));
    }

    @Test
    public void testReceiveCardFrom_canHoldASingleCardPlayedByEachOfTwoPlayers() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(felix, king, expectedCardsFromFelixsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(1));
        table.receiveCardsFrom(felix, felix.playCards(1));
        assertEquals("Should have Oscar's card", expectedCardsFromOscarsHand, table.retrieveCardsDealtFrom(oscar));
        assertEquals("Should have Felix's card", expectedCardsFromFelixsHand, table.retrieveCardsDealtFrom(felix));
    }

    @Test
    public void testReceiveCardFrom_canHoldMultipleCardsPlayedByEachOfTwoPlayers() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(oscar, new Card(Rank.THREE, Suit.DIAMOND), expectedCardsFromOscarsHand);
        dealTo(felix, king, expectedCardsFromFelixsHand);
        dealTo(felix, new Card(Rank.NINE, Suit.SPADE), expectedCardsFromFelixsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(2));
        table.receiveCardsFrom(felix, felix.playCards(2));
        assertTrue("Should have all of Oscar's cards", table.retrieveCardsDealtFrom(oscar).containsAll(expectedCardsFromOscarsHand));
        assertTrue("Should have all of Felix's cards", table.retrieveCardsDealtFrom(felix).containsAll(expectedCardsFromFelixsHand));
    }

    @Test
    public void testGetPlayerPiles_incorporatesPlayedCardsFromSinglePlayer() {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(1));
        assertEquals(ace, table.retrieveCardsDealtFrom(oscar).get(0));
    }

    @Test
    public void testClearAllPilesFromTheTable_leavesNoPiles() {
        DeckImpl deck = new DeckImpl();
        table.receiveCardsFrom(CHEWY, deck.deal(11)); //pile 1
        table.receiveCardsFrom(JABBA, deck.deal(12)); //pile 2
        assertEquals("Should be 2 piles on the table before clearing", 2, table.getAllPilesOnTheTable().size());
        table.clearAllPilesFromTheTable();
        assertEquals("Should be no more piles on the table", 0, table.getAllPilesOnTheTable().size());
    }

    @Test
    public void testAreThereTiesPresent_noBecauseAllCardsAreDifferent() {
        table.putAPlayerPileOnTheTable(new PlayerPile(TESLA, ACE_OF_CLUBS));
        table.putAPlayerPileOnTheTable(new PlayerPile(THE_DUDE, KING_OF_SPADES));
        assertFalse("All cards are different; there should not be a tie", table.areThereTiesPresent());
    }

    @Test
    public void testAreThereTiesPresent_yesBecauseWeHaveTwoCardsWithTheSameRank() {
        table.putAPlayerPileOnTheTable(new PlayerPile(ROSENCRANTZ, ACE_OF_CLUBS));
        table.putAPlayerPileOnTheTable(new PlayerPile(GILDENSTERN, ACE_OF_SPADES));
        assertTrue("Ranks match; there should be a tie", table.areThereTiesPresent());
    }

    private void dealTo(Player player, Card card, List<Card> expectedCardsToUseInTest) {
        player.dealToTopOfPlayersDeck(card);
        expectedCardsToUseInTest.add(card);
    }
}