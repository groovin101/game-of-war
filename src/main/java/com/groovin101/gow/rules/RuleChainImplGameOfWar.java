package com.groovin101.gow.rules;

import com.groovin101.gow.model.GameContext;
import com.groovin101.gow.model.Player;
import com.groovin101.gow.model.GameTable;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class RuleChainImplGameOfWar implements RuleChain {

    private List<RuleForUseWithRuleChain> registeredRules;
    private Player winner; //todo: should move this to the table itself, but for ease of development we'll keep it here for now

    public RuleChainImplGameOfWar() {
        registeredRules = new ArrayList<RuleForUseWithRuleChain>();
        winner = null;
    }

    public void registerRule(RuleForUseWithRuleChain rule) {
        registeredRules.add(rule);
    }

    boolean foundAWinner(GameTable table) {
        return table.getWinner() != null;
    }

    @Override
    public void fireNextRule(GameContext gameContext) {

        //Game of war uses the gameTable class as its game context
        GameTable gameTable = (GameTable)gameContext;

        if (!foundAWinner(gameTable) && !registeredRules.isEmpty()) {
            registeredRules.remove(0).fireRule(gameTable, this);
        }
    }

    List<RuleForUseWithRuleChain> getRegisteredRules() {
        return registeredRules;
    }
}
