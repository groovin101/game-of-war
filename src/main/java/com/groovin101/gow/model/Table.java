package com.groovin101.gow.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Table {

    private Map<Player, PlayerPile> playerPiles = new HashMap<Player, PlayerPile>();

    public void receiveCardsFrom(Player player, List<Card> cardsPassed) {
        PlayerPile pile = playerPiles.get(player);
        if (pile != null) {
            pile.getCards().addAll(cardsPassed);
        }
        else {
            pile = new PlayerPile(player, cardsPassed);
            playerPiles.put(player, pile);
        }
    }

    public List<Card> retrieveCardsDealtFrom(Player player) {
        return playerPiles.get(player) == null ? null : playerPiles.get(player).getCards();
    }

    public Map<Player, PlayerPile> getPlayerPiles() {
        return playerPiles;
    }
}