package com.groovin101.gow.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 */
public class TableTest {

    private Table table;
    private Player oscar;
    private Card ace;
    private Card king;
    private List<Card> expectedCardsFromOscarsHand;

    @Before
    public void setup() throws Exception {
        table = new Table();
        oscar = new Player("oscar");
        ace = new Card(Rank.ACE, Suit.CLUB);
        king = new Card(Rank.KING, Suit.HEART);
        expectedCardsFromOscarsHand = new ArrayList<Card>();
    }

    @Test
    public void testRetrieveCardsDealtFrom_returnsNoCardsWhenNobodyHasPlayedACardYet() throws Exception {
        assertNull("No hands have been played yet, so table should have none", table.retrieveCardsDealtFrom(oscar));
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
        Player felix = new Player("felix");
        List<Card> expectedCardsFromFelixsHand = new ArrayList<Card>();
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(felix, king, expectedCardsFromFelixsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(1));
        table.receiveCardsFrom(felix, felix.playCards(1));
        assertEquals("Should have Oscar's card", expectedCardsFromOscarsHand, table.retrieveCardsDealtFrom(oscar));
        assertEquals("Should have Felix's card", expectedCardsFromFelixsHand, table.retrieveCardsDealtFrom(felix));
    }

    @Test
    public void testReceiveCardFrom_canHoldMultipleCardsPlayedByEachOfTwoPlayers() throws Exception {
        Player felix = new Player("felix");
        List<Card> expectedCardsFromFelixsHand = new ArrayList<Card>();
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(oscar, new Card(Rank.THREE, Suit.DIAMOND), expectedCardsFromOscarsHand);
        dealTo(felix, king, expectedCardsFromFelixsHand);
        dealTo(felix, new Card(Rank.NINE, Suit.SPADE), expectedCardsFromFelixsHand);
        table.receiveCardsFrom(oscar, oscar.playCards(2));
        table.receiveCardsFrom(felix, felix.playCards(2));
        assertTrue("Should have all of Oscar's cards", table.retrieveCardsDealtFrom(oscar).containsAll(expectedCardsFromOscarsHand));
        assertTrue("Should have all of Felix's cards", table.retrieveCardsDealtFrom(felix).containsAll(expectedCardsFromFelixsHand));
    }

    private void dealTo(Player player, Card card, List<Card> expectedCardsToUseInTest) {
        player.dealToTopOfPlayersDeck(card);
        expectedCardsToUseInTest.add(card);
    }
}