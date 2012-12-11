package com.groovin101.gow.rules;

import com.groovin101.gow.model.WarTable;
import com.groovin101.gow.test.utils.BaseModelTest;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

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
    }

    @Test
    public void testRegisterRule() {

        chain.registerRule(ruleA);
        chain.registerRule(ruleB);

        assertTrue("Should be aware of rule A that we registered", chain.getRegisteredRules().contains(ruleA));
        assertTrue("Should be aware of rule B that we registered", chain.getRegisteredRules().contains(ruleB));
    }

    @Test
    public void testFireNextRule_firesRulesInOrderTheyWereRegistered() {

        chain.registerRule(ruleA);
        chain.registerRule(ruleB);

        chain.fireNextRule(null);
        verify(ruleA).fireRule(null, chain);

        chain.fireNextRule(null);
        verify(ruleB).fireRule(null, chain);
    }

    @Test
    public void foundAWinner_returnsFalseIfNoRulesHaveFired() {
        assertFalse(chain.foundAWinner());
    }

    @Test
    public void foundAWinner_returnsTrueIfWeSetAWinner() {

        chain.registerRule(mockRuleThatSetsAWinner());
        chain.fireNextRule(null);
        assertTrue(chain.foundAWinner());
    }

    @Test
    public void foundAWinner_allRulesAreShortCircuitedOnceAWinnerIsFound() {

        chain.registerRule(mockRuleThatJustFiresNextInChain());
        chain.registerRule(mockRuleThatSetsAWinner());
        chain.registerRule(ruleB);

        chain.fireNextRule(null);
        verifyZeroInteractions(ruleB);
    }

    private Rule mockRuleThatJustFiresNextInChain() {

        return new Rule() {
            @Override
            public void fireRule(WarTable gameWarTable, RuleChainImplGameOfWar ruleChain) {
                ruleChain.fireNextRule(gameWarTable);
            }
        };
    }

    private Rule mockRuleThatSetsAWinner() {

        return new Rule() {
            @Override
            public void fireRule(WarTable gameWarTable, RuleChainImplGameOfWar ruleChain) {
                ruleChain.setWinner(BaseModelTest.CHEWY);
                ruleChain.fireNextRule(gameWarTable);
            }
        };
    }

}