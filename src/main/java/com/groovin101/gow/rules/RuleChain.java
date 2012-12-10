package com.groovin101.gow.rules;

import com.groovin101.gow.model.GameContext;

/**
 */
public interface RuleChain {

    public void fireNextRule(GameContext gameContext);
}
