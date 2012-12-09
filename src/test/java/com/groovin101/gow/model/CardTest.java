package com.groovin101.gow.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

/**
 */
public class CardTest {

    private Card card;

    @Before
    public void setup() {
        card = new Card(Rank.ACE, Suit.CLUB);
    }

    @Test
    public void testGetRank_returnsValueCardWasInstantiatedWith() {
        assertEquals("Should have identified rank properly", Rank.ACE, card.getRank());
    }

    @Test
    public void testGetSuit_returnsValueCardWasInstantiatedWith() {
        assertEquals("Should have identified suit properly", Suit.CLUB, card.getSuit());
    }

    @Test
    public void testEquals_positive() {
        assertEquals("Should be same card", card, new Card(Rank.ACE, Suit.CLUB));
    }

    @Test
    public void testEquals_negative() {
        assertFalse("Should not be same card", card.equals(new Card(Rank.ACE, Suit.DIAMOND)));
    }

    @Test
    public void testToString() {
        assertEquals("Five of Diamonds", new Card(Rank.FIVE, Suit.DIAMOND).toString());
    }

    @Ignore
    @Test
    public void testCompareTo_faceCardIsGreaterThanNumber() {
        fail();
    }

    // test equal ranks are equal; 2nd thought, this should be configurable and not a fn of the card object itself - consider strategy pattern
}
