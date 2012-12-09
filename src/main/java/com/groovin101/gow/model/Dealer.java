package com.groovin101.gow.model;

import java.util.List;

/**
 */
public class Dealer {

    public void deal(DeckExtended deckToDealFrom, List<Player> players) {
        while (deckToDealFrom.hasMoreCards()) {
            for (Player player : players) {
                if (deckToDealFrom.hasMoreCards()) {
                    player.addToTopOfPlayerDeck(deckToDealFrom.deal());
                }
                else {
                    break;
                }
            }
        }
    }
}