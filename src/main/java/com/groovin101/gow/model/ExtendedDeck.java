package com.groovin101.gow.model;

/**
 * Provides additional methods useful to our implementation while honoring the Deck interface contract (as opposed to
 * modifying the Deck interface directly; in real life, we may not have that option).
 */
public interface ExtendedDeck extends Deck {

    public int countAvailableCards();

    public int countDealtCards();
}
