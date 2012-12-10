package com.groovin101.gow.model;

import com.groovin101.gow.test.utils.BaseModelTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 */
public class PlayerPileTest extends BaseModelTest {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void instantiation_mustHaveAPlayerAssociatedWithAPile() {
        try {
            PlayerPile pile = new PlayerPile(null, JACK_OF_DIAMONDS);
            fail("Must have a person associated with a pile");
        }
        catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCompareTo_usesHighestRankedCardInPileplayerPileWithASingleCardUsesCardCompareToForSorting() {
        Card cardMock = mock(Card.class);
        cardMock.setRank(Rank.ACE);
        cardMock.setSuit(Suit.CLUB);
        PlayerPile pileWithAce = new PlayerPile(CHEWY, cardMock);
        PlayerPile pileWithKing = new PlayerPile(JABBA, KING_OF_SPADES);
        pileWithAce.compareTo(pileWithKing);
        verify(cardMock).compareTo(KING_OF_SPADES);
    }

    @Test
    public void testCompareTo_playerPilesWithASingleEqualCardEvaluatesToZero() {
        PlayerPile pileWithAce = new PlayerPile(CHEWY, KING_OF_SPADES);
        PlayerPile pileWithKing = new PlayerPile(JABBA, KING_OF_SPADES);
        assertEquals(0, pileWithAce.compareTo(pileWithKing));
    }

    @Test
    public void testCompareTo_warScenarioUsesLastCardDealtInThePileForComparing() {
        List<Card> cardsInPile = new ArrayList<Card>();
        Card lastCardDealtInThePile = mock(Card.class);
        lastCardDealtInThePile.setRank(Rank.SEVEN);
        lastCardDealtInThePile.setSuit(Suit.DIAMOND);
        cardsInPile.add(ACE_OF_CLUBS);
        cardsInPile.add(KING_OF_SPADES);
        cardsInPile.add(QUEEN_OF_HEARTS);
        cardsInPile.add(lastCardDealtInThePile);
        PlayerPile pile = new PlayerPile(CHEWY, cardsInPile);
        pile.compareTo(new PlayerPile(JABBA, JACK_OF_DIAMONDS));
        verify(lastCardDealtInThePile).compareTo(JACK_OF_DIAMONDS);
    }

    @Test
    public void testCompareTo_handlesNullProperly() {
        PlayerPile pileWithAce = new PlayerPile(CHEWY, KING_OF_SPADES);
        assertEquals(1, pileWithAce.compareTo(null));
    }

    @Test
    public void testFetchLastCardDealt_emptyPileReturnsNullCard() {
        PlayerPile pile = new PlayerPile(CHEWY, new ArrayList<Card>());
        assertNull("There was no last card dealt because the pile is empty", pile.fetchLastCardDealt());
    }
    
    @Test
    public void testFetchLastCardDealt_singleCardPileReturnsTheOnlyCardPresent() {
        PlayerPile pile = new PlayerPile(CHEWY, ACE_OF_CLUBS);
        assertEquals("Last card dealt should have been the one card in the pile", ACE_OF_CLUBS, pile.fetchLastCardDealt());
    }
    
    @Test
    public void testFetchLastCardDealt_PileWithMultipleCardsReturnsTheOneThatWasAddedLast() {
        PlayerPile pile = new PlayerPile(TESLA, buildCardList(new Card[] {ACE_OF_CLUBS, JACK_OF_DIAMONDS, QUEEN_OF_HEARTS, KING_OF_SPADES}));
        assertEquals("Last card dealt should have been the king", KING_OF_SPADES, pile.fetchLastCardDealt());
    }

    @Test
    public void testShuffle_listsAreInDifferentOrderAfterShuffling() {
        PlayerPile pile = new PlayerPile(TESLA, largeCardList());
        List<Card> originalList = new ArrayList<Card>();
        originalList.addAll(pile.getCards());
        pile.shuffle();
        assertFalse("Lists of available cards should not be in same order after shuffling", originalList.equals(pile.getCards()));
    }

}
