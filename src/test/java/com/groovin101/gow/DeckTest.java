package com.groovin101.gow;

import com.groovin101.gow.model.FrenchDeckImpl;
import com.groovin101.gow.model.ExtendedDeck;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 */
public class DeckTest {


    @Test
    public void testCreate_ensureThereAre52Cards() {
        ExtendedDeck cardDeck = new FrenchDeckImpl(4, 14);
        assertEquals("Not right amount of cards", 52, cardDeck.getCards().size());
    }

//    @Test
//    public void testCreate_shouldBeSixCards() {
//        ExtendedDeck cardDeck = new FrenchDeckImpl(2, 3);
//        assertEquals("Not right amount of cards", 6, cardDeck.getCards().size());
//    }


//    public void testDeal() {
//        Deck deck = new FrenchDeckImpl();
//        deck.
//    }
}
