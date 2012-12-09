package com.groovin101.gow.model;

/**
 */
public enum Rank {

    KING(13),
    QUEEN(12),
    JACK(11),
    TEN(10),
    NINE(9),
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2),
    ACE(14);

    private final int cardValue;

    /**
     * Constructor
     *
     * @param cardValue
     */
    private Rank(int cardValue) {
        this.cardValue = cardValue;
    }

    public int getCardValue() {
        return cardValue;
    }
}
