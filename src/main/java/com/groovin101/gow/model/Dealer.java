package com.groovin101.gow.model;

import java.util.List;

/**
 */
public class Dealer {

    public void deal(DeckExtended deckToDealFrom, List<Player> players) {
        while (deckToDealFrom.hasNext()) {
            players.get(0).addToTopOfPlayerDeck(deckToDealFrom.deal());
        }
    }
}