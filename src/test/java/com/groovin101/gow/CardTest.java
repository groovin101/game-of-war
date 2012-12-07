package com.groovin101.gow;

import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.CardRank;
import com.groovin101.gow.model.CardSuit;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 */
public class CardTest {

    private Card card;

    @Before
    public void setup() {
        card = new Card(CardRank.ACE, CardSuit.CLUB);
    }

    @Test
    public void testGetRank_returnsValueCardWasInstantiatedWith() {
        assertEquals("Should have identified rank properly", CardRank.ACE, card.getRank());
    }

    @Test
    public void testGetSuit_returnsValueCardWasInstantiatedWith() {
        assertEquals("Should have identified suit properly", CardSuit.CLUB, card.getSuit());
    }

    @Test
    public void testEquals_positive() {
        assertEquals("Should be same card", card, new Card(CardRank.ACE, CardSuit.CLUB));
    }

    @Test
    public void testEquals_negative() {
        assertFalse("Should not be same card", card.equals(new Card(CardRank.ACE, CardSuit.DIAMOND)));
    }

    @Test
    public void testToString() {
        assertEquals("Five of Diamonds", new Card(CardRank.FIVE, CardSuit.DIAMOND).toString());
    }
}
