package com.groovin101.gow.model;

/**
 */
public interface Deck {

    public void create(int numberOfSuits, int numberOfRanks);

    public void shuffle();

    public Card deal();
}
