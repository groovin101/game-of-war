package com.groovin101.gow.rules;

import com.groovin101.gow.model.WarTable;

/**
 */
public class RuleHighestCard implements Rule {

    @Override
    public void fireRule(WarTable gameWarTable, RuleChainImplGameOfWar ruleChain) {

        ruleChain.fireNextRule(gameWarTable);
    }
}
