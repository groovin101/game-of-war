package com.groovin101.gow;

import com.groovin101.gow.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 */
public class FrenchDeckTest {

    private ExtendedDeck cardDeck;

    @Before
    public void setup() {
        cardDeck = new FrenchDeckImpl(4, 13);
    }

    @Test
    public void testCreate_ensureThereAre52Cards() {
        cardDeck.create(4, 13);
        assertEquals("Not right amount of cards", 52, cardDeck.getCards().size());
    }

    @Test
    public void testDeal_unshuffledDeckFirstCard() {
        Card firstCardInDeck = cardDeck.deal();
        assertEquals("The ace of clubs should be the first card", new Card(CardRank.ACE, CardSuit.CLUB), firstCardInDeck);
    }

    @Test
    public void testDeal_unshuffledDeckSecondCard() {
        cardDeck.deal();
        Card secondCard = cardDeck.deal();
        assertEquals("The two of clubs should be the second card", new Card(CardRank.TWO, CardSuit.CLUB), secondCard);
    }

    @Test
    public void testCreate_invalidNumberOfSuitsThrowsException() {
        try {
            cardDeck.create(0, 13);
            fail("Expected an IllegalArgumentException to be thrown since this is an invalid number of suits for a French Deck");
        }
        catch (IllegalArgumentException e) {}
    }

    @Test
    public void testCreate_invalidNumberOfRanksThrowsException() {
        try {
            cardDeck.create(4, 0);
            fail("Expected an IllegalArgumentException to be thrown since this is an invalid number of suits for a French Deck");
        }
        catch (IllegalArgumentException e) {}
    }

}
