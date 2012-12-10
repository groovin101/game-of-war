package com.groovin101.gow.rules;

import com.groovin101.gow.model.GameContext;
import com.groovin101.gow.model.Player;
import com.groovin101.gow.model.WarTable;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class RuleChainImplGameOfWar implements RuleChain {

    private List<Rule> registeredRules;
    private Player winner; //todo: should move this to the table itself, but for ease of development we'll keep it here for now

    public RuleChainImplGameOfWar() {
        registeredRules = new ArrayList<Rule>();
    }

    public void registerRule(Rule rule) {
        registeredRules.add(rule);
    }

    private void setWinner(Player winner) {
        this.winner = winner;
    }

    private boolean foundAWinner() {
        return false;
    }

    @Override
    public void fireNextRule(GameContext gameContext) {

        //Game of war uses the warTable class as its game context
        WarTable warTable = (WarTable)gameContext;

        if (!foundAWinner()) {
            registeredRules.remove(0).fireRule(warTable, this);
        }
    }

    List<Rule> getRegisteredRules() {
        return registeredRules;
    }
}
