package com.groovin101.gow.model;

import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 */
public class CardTest extends BaseTest {

    private Card card;

    @Before
    public void setup() {
        super.setup();
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

    @Test
    public void testCompareTo_faceCardIsGreaterThanNumber() {
        Card king = new Card(Rank.KING, Suit.CLUB);
        Card three = new Card(Rank.THREE, Suit.CLUB);
        assertEquals("King should be worth more than a three", 1, king.compareTo(three));
    }

    @Test
    public void testCompareTo_NumberIsLessThanFaceCard() {
        Card king = new Card(Rank.KING, Suit.CLUB);
        Card three = new Card(Rank.THREE, Suit.CLUB);
        assertEquals("Three should be less than a king", -1, three.compareTo(king));
    }

    @Test
    public void testCompareTo_cardsOfEqualRankButDifferentSuitAreEqual() {
        Card kingOfClubs = new Card(Rank.KING, Suit.CLUB);
        Card kingOfSpades = new Card(Rank.KING, Suit.SPADE);
        assertEquals("Kings should be equal irrespective of suit", 0, kingOfClubs.compareTo(kingOfSpades));
    }

    @Test
    public void testCompareTo_cardThatIsNotNullIsGreaterThanNullCard() {
        assertEquals("Card that is not null should be greater than nothing", 1, KING_OF_SPADES.compareTo(null));
    }

    // test equal ranks are equal; 2nd thought, this should be configurable and not a fn of the card object itself - consider strategy pattern
}
