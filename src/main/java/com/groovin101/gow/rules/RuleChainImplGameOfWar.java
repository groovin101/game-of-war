package com.groovin101.gow.rules;

import com.groovin101.gow.model.GameContext;
import com.groovin101.gow.model.GameTable;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class RuleChainImplGameOfWar implements RuleChain {

    private List<Rule> registeredRules;

    public RuleChainImplGameOfWar() {
        registeredRules = new ArrayList<Rule>();
    }

    public void registerRule(Rule rule) {
        registeredRules.add(rule);
    }

    boolean foundAWinner(GameTable table) {
//        return table.getWinnerOfTheLastRound() != null;
        return false;
    }

    @Override
    public void fireNextRule(GameContext gameContext) {

        GameTable gameTable = (GameTable)gameContext;

//        if (!foundAWinner(gameTable) && !registeredRules.isEmpty()) {
//            registeredRules.remove(0).fireRule(gameTable, this);
//        }
    }

    List<Rule> getRegisteredRules() {
        return registeredRules;
    }
}
