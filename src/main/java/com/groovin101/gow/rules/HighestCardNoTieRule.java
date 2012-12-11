package com.groovin101.gow.rules;

import com.groovin101.gow.model.PileCard;
import com.groovin101.gow.model.GameTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Highest card found will determine the winner. If more than one card is equal, there is not a guarantee as to which
 * owning player will be determined the winner
 */
public class HighestCardNoTieRule implements RuleForUseWithRuleChain {

    @Override
    public void fireRule(GameTable gameTable, RuleChainImplGameOfWar ruleChain) {

        List<PileCard> highestRankingCardsInPlay = findHighestRankingCards(gameTable.fetchSignificantCards());

        if (!wasThereATieAmongTheHighestCards(highestRankingCardsInPlay)) {
            gameTable.setWinner(highestRankingCardsInPlay.get(0).getCardOwner());
        }
        ruleChain.fireNextRule(gameTable);
    }

    private boolean wasThereATieAmongTheHighestCards(List<PileCard> highestCardsAmongTheSignificantCardsInPlay) {
        return highestCardsAmongTheSignificantCardsInPlay.size() > 1;
    }

    List<PileCard> findHighestRankingCards(List<PileCard> pileCards) {
        List<PileCard> highestCards = new ArrayList<PileCard>();
        Collections.sort(pileCards);

        for (int i = pileCards.size()-1; i >= 0; i--) {
            if (highestCards.isEmpty()) {
                highestCards.add(pileCards.get(i));
            }
            else {
                if (pileCards.get(i).compareTo(highestCards.get(highestCards.size()-1)) == 0) {
                    highestCards.add(pileCards.get(i));
                }
            }
        }
        return highestCards;
    }
}
