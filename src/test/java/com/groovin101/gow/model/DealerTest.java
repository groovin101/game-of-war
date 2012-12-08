package com.groovin101.gow.model;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 */
public class DealerTest {

    @Ignore
    @Test
    public void testDealCardsToPlayers_singlePlayerGetsAllCards() throws Exception {
        List<Player> players = new ArrayList<Player>();
        Dealer dealer = new Dealer();
        Player jimBob = new Player("JimBob");
        players.add(jimBob);
        dealer.deal(new DeckImpl(), players);
        assertEquals("Single player should have all the cards", 52, jimBob.getPlayerDeckSize());
    }

}
