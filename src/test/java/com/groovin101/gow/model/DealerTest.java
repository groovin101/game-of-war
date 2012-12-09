package com.groovin101.gow.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 */
public class DealerTest {

    private Dealer dealer;
    private DeckExtended deck;
    private List<Player> players;
    private Player rosencrantz;

    @Before
    public void setup() throws Exception {
        dealer = new Dealer();
        deck = new DeckImpl();
        rosencrantz = new Player("rosencrantz");
        players = new ArrayList<Player>();
        players.add(rosencrantz);
    }

    @Test
    public void testDealCardsToPlayers_singlePlayerGetsAllCards() {
        dealer.deal(deck, players);
        assertEquals("Single player should have all the cards", 52, rosencrantz.getPlayerDeckSize());
    }

    @Test
    public void testDealCardsToPlayers_singlePlayerSingleCardDeckIsDealtToPlayer() throws Exception {
        deck.create(1,1);
        Card onlyCardInSingleCardDeck = deck.deal();
        deck.create(1,1);
        dealer.deal(deck, players);
        assertEquals("Single player should have the same card as was in a single-card deck", onlyCardInSingleCardDeck, rosencrantz.playACard());
    }

    @Test
    public void testDealCardsToPlayers_twoPlayersSplitDeckEvenly() throws Exception {
        Player guildenstern = new Player("guildenstern");
        players.add(guildenstern);
        deck.create(2,13);
        dealer.deal(deck, players);
        assertEquals("Players should have same number of cards", rosencrantz.getPlayerDeckSize(), guildenstern.getPlayerDeckSize());
    }

    @Test
    public void testDealCardsToPlayers_threePlayersHandlesUnevenDistributionGracefully() throws Exception {
        Player guildenstern = new Player("guildenstern");
        Player enzo = new Player("enzo");
        players.add(guildenstern);
        players.add(enzo);
        deck.create(1,13);
        dealer.deal(deck, players);
        assertEquals("First player should have 5 cards", 5, rosencrantz.getPlayerDeckSize());
        assertEquals("Second player should have 4 cards", 4, guildenstern.getPlayerDeckSize());
        assertEquals("Third player should have 4 cards", 4, enzo.getPlayerDeckSize());
    }

}