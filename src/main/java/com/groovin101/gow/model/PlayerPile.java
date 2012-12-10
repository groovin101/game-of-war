package com.groovin101.gow.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an active pile of cards, in play, for a particular player
 */
public class PlayerPile implements Comparable<PlayerPile> {

    private Player player;
    private List<Card> cards;

    public PlayerPile(Player player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    public PlayerPile(Player player, Card card) {
        this.player = player;
        cards = new ArrayList<Card>();
        cards.add(card);
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

    protected Card fetchLastCardDealt() {
        return CollectionUtils.isEmpty(getCards()) ? null : getCards().get(getCards().size()-1);
    }

    @Override
    public int compareTo(PlayerPile o) {
        if (fetchLastCardDealt() == null) {
            if (o.fetchLastCardDealt() == null) {
                return 0;
            }
            return -1;
        }
        return fetchLastCardDealt().compareTo(o == null ? null : o.fetchLastCardDealt());
    }

    @Override
    public String toString() {
        return "PlayerPile{" +
                "player=" + player +
                ", cards=" + cards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerPile that = (PlayerPile) o;
        if (cards != null ? !cards.equals(that.cards) : that.cards != null) {
            return false;
        }
        if (player != null ? !player.equals(that.player) : that.player != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (cards != null ? cards.hashCode() : 0);
        return result;
    }
}
