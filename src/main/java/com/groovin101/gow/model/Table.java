package com.groovin101.gow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Table {

    private Map<Player, List<Card>> playedCards = new HashMap<Player, List<Card>>();

    public void receiveCardFrom(Player player) {
        List<Card> cards = playedCards.get(player) == null ? new ArrayList<Card>() : playedCards.get(player);
        cards.add(player.playACard());
        playedCards.put(player, cards);
    }

    public List<Card> retrieveCardsDealtFrom(Player player) {
        return playedCards.get(player);
    }
}
