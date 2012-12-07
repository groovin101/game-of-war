package com.groovin101.gow.model;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

/**
 */
public class Card {

    private CardRank rank;
    private CardSuit suit;

    public Card(CardRank rank, CardSuit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public CardRank getRank() {
        return rank;
    }
    public void setRank(CardRank rank) {
        this.rank = rank;
    }
    public CardSuit getSuit() {
        return suit;
    }
    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(rank.toString().toLowerCase())
                + " of "
                + StringUtils.capitalize(suit.toString().toLowerCase()) + "s";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (rank != card.rank) return false;
        if (suit != card.suit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (suit != null ? suit.hashCode() : 0);
        return result;
    }
}
