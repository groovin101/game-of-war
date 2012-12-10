package com.groovin101.gow.rules;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 */
public class RuleChainImplGameOfWarTest {


    private Rule ruleA;
    private Rule ruleB;
    private RuleChainImplGameOfWar chain;

    @Before
    public void setup() {

        ruleA = mock(Rule.class);
        ruleB = mock(Rule.class);

        chain = new RuleChainImplGameOfWar();
        chain.registerRule(ruleA);
        chain.registerRule(ruleB);
    }

    @Test
    public void testRegisterRule() {

        assertTrue("Should be aware of rule A that we registered", chain.getRegisteredRules().contains(ruleA));
        assertTrue("Should be aware of rule B that we registered", chain.getRegisteredRules().contains(ruleB));
    }

    @Test
    public void testFireNextRule_firesRulesInOrderTheyWereRegistered() {

        chain.fireNextRule(null);
        verify(ruleA).fireRule(null, chain);
        chain.fireNextRule(null);
        verify(ruleB).fireRule(null, chain);
    }
}