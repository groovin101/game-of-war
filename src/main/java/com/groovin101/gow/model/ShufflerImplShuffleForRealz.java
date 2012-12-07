package com.groovin101.gow.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

/**
 */
public class ShufflerImplShuffleForRealz implements Shuffler {

    @Override
    public ExtendedDeck shuffle(ExtendedDeck deckToShuffle) {
        Collections.shuffle(deckToShuffle.asList());
        return deckToShuffle;
    }
}
