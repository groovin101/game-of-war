package com.groovin101.gow;

import com.groovin101.gow.model.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 */
public class ShufflerImplShuffleForRealzTest {

    @Test
    public void testShuffle_multipleDataPointsAreDifferent() {
        ExtendedDeck unshuffledDeck = new FrenchDeckImpl();
        ExtendedDeck shuffledDeck = new FrenchDeckImpl();
        Card firstCardInDeck = shuffledDeck.deal();
        assertEquals("The ace of clubs should be the first card before shuffling",
                new Card(CardRank.ACE, CardSuit.CLUB), firstCardInDeck);

        Shuffler shuffler = new ShufflerImplShuffleForRealz();
        shuffledDeck = (ExtendedDeck)shuffler.shuffle(shuffledDeck);
        assertTrue("Odds to have multiple card orders same after a shuffle are ridiculously low - deck does not appear shuffled!",
                shuffleSufficientlyChangedOrder(unshuffledDeck, shuffledDeck));
    }

    private boolean shuffleSufficientlyChangedOrder(ExtendedDeck unshuffledDeck, ExtendedDeck deckThatWasShuffled) {
        boolean firstCardsEqual = unshuffledDeck.deal().equals(deckThatWasShuffled.deal());
        boolean secondCardsEqual = unshuffledDeck.deal().equals(deckThatWasShuffled.deal());
        boolean thirdCardsEqual = unshuffledDeck.deal().equals(deckThatWasShuffled.deal());
        return firstCardsEqual && secondCardsEqual && thirdCardsEqual;
    }
}
