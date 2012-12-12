package com.groovin101.gow.rules;

import com.groovin101.gow.model.PileCard;
import com.groovin101.gow.model.GameTable;
import com.groovin101.gow.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Will set a winner if there is a single card that has the highest rank. If there is a tie _among the highest ranking_
 * cards in the round, no winner will be selected.
 */
public class HighestCardNoTieRule implements Rule {

    @Override
    public void fireRule(GameTable gameTable, RuleChainImplGameOfWar ruleChain) {

        List<PileCard> highestRankingCardsInPlay = findHighestRankingCards(gameTable.fetchSignificantCards());

        if (!wasThereATie(highestRankingCardsInPlay)) {
            gameTable.setWinner(determineWinner(highestRankingCardsInPlay));
        }
        else {
            gameTable.setWinner(null);
        }

//ruleChain.fireNextRule(gameTable);
    }

    Player determineWinner(List<PileCard> highestRankingCardsInPlay) {
        return highestRankingCardsInPlay.isEmpty() ? null : highestRankingCardsInPlay.get(0).getCardOwner();
    }

    boolean wasThereATie(List<PileCard> highestCardsAmongTheSignificantCardsInPlay) {

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
