package com.groovin101.gow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an active pile of cards, in play, for a particular player
 */
public class PlayerPile {

    private Player player;
    private List<Card> cards;

    public PlayerPile(Player player) {
        this.player = player;
        cards = new ArrayList<Card>();
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public List<Card> getCards() {
        return cards;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
