package com.groovin101.gow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Table {

    private Map<Player, PlayerPile> allPilesOnTheTable = new HashMap<Player, PlayerPile>();

    public void receiveCardsFrom(Player player, List<Card> cardsPassed) {
        PlayerPile pile = allPilesOnTheTable.get(player);
        if (pile != null) {
            pile.getCards().addAll(cardsPassed);
        }
        else {
            pile = new PlayerPile(player, cardsPassed);
            allPilesOnTheTable.put(player, pile);
        }
    }

    public List<Card> retrieveCardsDealtFrom(Player player) {
        return allPilesOnTheTable.get(player) == null ? new ArrayList<Card>() : allPilesOnTheTable.get(player).getCards();
    }

    public List<PlayerPile> getAllPilesOnTheTable() {
        return (List<PlayerPile>) allPilesOnTheTable.values();
    }
}