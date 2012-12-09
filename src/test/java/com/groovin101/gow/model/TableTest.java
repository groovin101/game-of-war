package com.groovin101.gow.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 */
public class TableTest {

    private Table table;
    private Player felix;
    private Player oscar;
    private Card ace;
    private Card king;
    private List<Card> expectedCardsFromOscarsHand;
    private List<Card> expectedCardsFromFelixsHand;

    @Before
    public void setup() throws Exception {
        table = new Table();
        oscar = new Player("oscar");
        felix = new Player("felix");
        ace = new Card(Rank.ACE, Suit.CLUB);
        king = new Card(Rank.KING, Suit.HEART);
        expectedCardsFromOscarsHand = new ArrayList<Card>();
        expectedCardsFromFelixsHand = new ArrayList<Card>();
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
        Map<Player, PlayerPile> playerPiles = table.getPlayerPiles();
        PlayerPile oscarsPile = playerPiles.get(oscar);
        assertEquals(oscar, oscarsPile.getPlayer());
        assertEquals(ace, oscarsPile.getCards().get(0));
    }

    private void dealTo(Player player, Card card, List<Card> expectedCardsToUseInTest) {
        player.dealToTopOfPlayersDeck(card);
        expectedCardsToUseInTest.add(card);
    }
}