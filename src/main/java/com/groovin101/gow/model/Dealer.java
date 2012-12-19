package com.groovin101.gow.model;

import java.util.Collection;

/**
 */
public class Dealer {

    public void dealAllCards(DeckExtended deckToDealFrom, Collection<Player> players) {
        while (deckToDealFrom.hasMoreCardsAvailable()) {
            for (Player player : players) {
                if (deckToDealFrom.hasMoreCardsAvailable()) {
                    player.dealToTopOfPlayersDeck(deckToDealFrom.deal());
                }
                else {
                    break;
                }
            }
        }
    }
}