package com.groovin101.gow.rules;

import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.PileCard;
import com.groovin101.gow.model.WarTable;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 */
public class HighestCardNoTieRuleTest extends BaseTest {

    @Test
    public void testFireRule_highestCardWinsWhenThereAreNoTies() {

        WarTable table = new WarTable();

        table.addSignificantCard(new PileCard(CHEWY, KING_OF_SPADES));
        table.addSignificantCard(new PileCard(JABBA, ACE_OF_CLUBS));
        table.addSignificantCard(new PileCard(THE_DUDE, QUEEN_OF_HEARTS));

        HighestCardNoTieRule highestCardNoTieRule = new HighestCardNoTieRule();
        RuleChainImplGameOfWar chain = new RuleChainImplGameOfWar();
        chain.registerRule(highestCardNoTieRule);

        highestCardNoTieRule.fireRule(table, chain);

        assertEquals("Jabba should have won", JABBA, table.getWinner());
//        RuleChainImplGameOfWar ruleChainMock = mock(RuleChainImplGameOfWar.class);
//        rule.fireRule(table, ruleChainMock);
//        verify(ruleChainMock).setWinner(JABBA);
    }

    //todo
    @Ignore
    @Test
    public void testFireRule_noWinnerIsDeclaredIfThereIsATieAmongTheHighestCards() {

    }

    @Test
    public void findAllHighestCards_returnsGreatestWhenThereAreTwoCards() {
        HighestCardNoTieRule rule = new HighestCardNoTieRule();
        List<PileCard> highestCards = rule.findHighestRankingCards(buildPileCardList(new Card[]{ACE_OF_CLUBS, KING_OF_SPADES}));
        assertEquals(1, highestCards.size());
        assertEquals(ACE_OF_CLUBS, highestCards.get(0).getCard());
    }

    @Test
    public void findAllHighestCards_returnsGreatestCardWhenThereAreThreeCards() {

        HighestCardNoTieRule rule = new HighestCardNoTieRule();

        List<PileCard> highestCards = rule.findHighestRankingCards(buildPileCardList(new Card[]{JACK_OF_DIAMONDS, ACE_OF_SPADES, KING_OF_SPADES}));

        assertEquals(1, highestCards.size());
        assertEquals(ACE_OF_SPADES, highestCards.get(0).getCard());
    }

    @Test
    public void findAllHighestCards_returnsBothCardsWhenTheyAreEqualAndThereAreTwoCards() {

        HighestCardNoTieRule rule = new HighestCardNoTieRule();
        List<PileCard> pileCardsToBeEvaluated = buildPileCardList(new Card[]{ACE_OF_CLUBS, ACE_OF_SPADES});

        List<PileCard> highestCards = rule.findHighestRankingCards(pileCardsToBeEvaluated);

        assertEquals(2, highestCards.size());
        assertTrue(highestCards.contains(pileCardsToBeEvaluated.get(0)));
        assertTrue(highestCards.contains(pileCardsToBeEvaluated.get(1)));
    }

    @Test
    public void findAllHighestCards_returnsBothCardsWhenTheyAreEqualAndThereAreThreeCards() {

        HighestCardNoTieRule rule = new HighestCardNoTieRule();
        List<PileCard> pileCardsToBeEvaluated = buildPileCardList(new Card[]{JACK_OF_DIAMONDS, ACE_OF_SPADES, ACE_OF_CLUBS});

        List<PileCard> highestCards = rule.findHighestRankingCards(pileCardsToBeEvaluated);

        assertEquals(2, highestCards.size());
        assertTrue(highestCards.contains(pileCardsToBeEvaluated.get(1)));
        assertTrue(highestCards.contains(pileCardsToBeEvaluated.get(2)));
    }

}
