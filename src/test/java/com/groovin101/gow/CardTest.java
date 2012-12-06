package com.groovin101.gow;

import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.CardRank;
import com.groovin101.gow.model.CardSuit;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 */
public class CardTest {

    private Card card;

    @Before
    public void setup() {
        card = new Card(CardRank.ACE, CardSuit.CLUB);
    }

    @Test
    public void test_getRank_returnsValueCardWasInstantiatedWith() {
        assertEquals("Should have identified rank properly", CardRank.ACE, card.getRank());
    }

    @Test
    public void test_getSuit_returnsValueCardWasInstantiatedWith() {
        assertEquals("Should have identified suit properly", CardSuit.CLUB, card.getSuit());
    }
}
