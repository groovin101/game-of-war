package com.groovin101.gow.model;

/**
 * Implements a no-shuffle strategy
 */
public class ShufflerImplNoShuffle implements Shuffler {

    @Override
    public ExtendedDeck shuffle(ExtendedDeck deckToShuffle) {
        return deckToShuffle;
    }
}
