package com.groovin101.gow.model;

import com.groovin101.gow.test.utils.CardBuilder;
import com.groovin101.gow.test.utils.PlayerBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 */
public class PlayerPileTest {

    @Test
    public void testCompareTo_playerPileWithASingleCardUsesCardCompareToForSorting() {
        Card cardMock = mock(Card.class);
        cardMock.setRank(Rank.ACE);
        cardMock.setSuit(Suit.CLUB);
        PlayerPile pileWithAce = new PlayerPile(PlayerBuilder.CHEWY, cardMock);
//        PlayerPile pileWithKing = new PlayerPile(PlayerBuilder.JABBA, CardBuilder.KING_OF_SPADES);
        pileWithAce.compareTo(null);
//        verify(cardMock).compareTo(CardBuilder.KING_OF_SPADES);
        verify(cardMock).compareTo(null);
    }


    @Test
    public void testCompareTo_usesHighestRankedCardInPileplayerPileWithASingleCardUsesCardCompareToForSorting() {
        Card cardMock = mock(Card.class);
        cardMock.setRank(Rank.ACE);
        cardMock.setSuit(Suit.CLUB);
        PlayerPile pileWithAce = new PlayerPile(PlayerBuilder.CHEWY, cardMock);
        PlayerPile pileWithKing = new PlayerPile(PlayerBuilder.JABBA, CardBuilder.KING_OF_SPADES);
        pileWithAce.compareTo(pileWithKing);
        verify(cardMock).compareTo(CardBuilder.KING_OF_SPADES);
    }

    @Test
    public void testCompareTo_playerPilesWithASingleEqualCardEvaluatesToZero() {
        PlayerPile pileWithAce = new PlayerPile(PlayerBuilder.CHEWY, CardBuilder.KING_OF_SPADES);
        PlayerPile pileWithKing = new PlayerPile(PlayerBuilder.JABBA, CardBuilder.KING_OF_SPADES);
        assertEquals(0, pileWithAce.compareTo(pileWithKing));
    }

    @Test
    public void testCompareTo_warScenarioUsesLastCardDealtInThePileForComparing() {
        List<Card> cardsInPile = new ArrayList<Card>();
        Card lastCardDealtInThePile = mock(Card.class);
        lastCardDealtInThePile.setRank(Rank.SEVEN);
        lastCardDealtInThePile.setSuit(Suit.DIAMOND);
        cardsInPile.add(CardBuilder.ACE_OF_CLUBS);
        cardsInPile.add(CardBuilder.KING_OF_SPADES);
        cardsInPile.add(CardBuilder.QUEEN_OF_HEARTS);
        cardsInPile.add(lastCardDealtInThePile);
        PlayerPile pile = new PlayerPile(PlayerBuilder.CHEWY, cardsInPile);
        pile.compareTo(new PlayerPile(PlayerBuilder.JABBA, CardBuilder.JACK_OF_DIAMONDS));
        verify(lastCardDealtInThePile).compareTo(CardBuilder.JACK_OF_DIAMONDS);
    }
    
    @Test
    public void testFetchLastCardDealt_emptyPileReturnsNullCard() {
        PlayerPile pile = new PlayerPile(PlayerBuilder.CHEWY, new ArrayList<Card>());
        assertNull("There was no last card dealt because the pile is empty", pile.fetchLastCardDealt());
    }
    
    @Test
    public void testFetchLastCardDealt_singleCardPileReturnsTheOnlyCardPresent() {
        PlayerPile pile = new PlayerPile(PlayerBuilder.CHEWY, CardBuilder.ACE_OF_CLUBS);
        assertEquals("Last card dealt should have been the one card in the pile", CardBuilder.ACE_OF_CLUBS, pile.fetchLastCardDealt());
    }
    
    @Test
    public void testFetchLastCardDealt_PileWithMultipleCardsReturnsTheOneThatWasAddedLast() {
        List<Card> cardsInPile = new ArrayList<Card>();
        cardsInPile.add(CardBuilder.ACE_OF_CLUBS);
        cardsInPile.add(CardBuilder.JACK_OF_DIAMONDS);
        cardsInPile.add(CardBuilder.QUEEN_OF_HEARTS);
        cardsInPile.add(CardBuilder.KING_OF_SPADES);
        PlayerPile pile = new PlayerPile(PlayerBuilder.TESLA, cardsInPile);
        assertEquals("Last card dealt should have been the king", CardBuilder.KING_OF_SPADES, pile.fetchLastCardDealt());
    }
    
}
