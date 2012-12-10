package com.groovin101.gow.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an active pile of cards, in play, for a particular player
 */
public class PlayerPile implements Comparable<PlayerPile>, Deck {

    private Player player;
    private List<Card> cards;

    public PlayerPile(Player player, List<Card> cards) {
        if (player == null) {
            throw new IllegalArgumentException("You must have a person associated with a pile");
        }
        this.player = player;
        this.cards = cards;
    }

    public PlayerPile(Player player, Card card) {
        this(player, new ArrayList<Card>());
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
    public void create(int numberOfSuits, int numberOfRanks) {
        throw new UnsupportedOperationException("Cannot create a PlayerPile directly");
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card deal() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
                player.getName() +
                " played: " + cards +
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
