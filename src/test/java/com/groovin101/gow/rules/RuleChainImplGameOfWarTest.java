package com.groovin101.gow.rules;

import com.groovin101.gow.model.WarTable;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

/**
 */
public class RuleChainImplGameOfWarTest {

    private RuleForUseWithRuleChain ruleA;
    private RuleForUseWithRuleChain ruleB;
    private RuleChainImplGameOfWar chain;
    private WarTable table;

    @Before
    public void setup() {

        ruleA = mock(RuleForUseWithRuleChain.class);
        ruleB = mock(RuleForUseWithRuleChain.class);
        chain = new RuleChainImplGameOfWar();
        table = new WarTable();
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

        chain.fireNextRule(table);
        verify(ruleA).fireRule(table, chain);

        chain.fireNextRule(table);
        verify(ruleB).fireRule(table, chain);
    }

    @Test
    public void foundAWinner_allRulesAreShortCircuitedOnceAWinnerIsFound() {

        chain.registerRule(mockRuleThatJustFiresNextInChain());
        chain.registerRule(mockRuleThatSetsAWinner());
        chain.registerRule(ruleB);

        chain.fireNextRule(table);
        verifyZeroInteractions(ruleB);
    }

//    @Test
//    public void fireNextRule_shouldNotBeAbleToBeInvokedByARuleThatIsNotRegisteredWithThisChain() {
//        try {
//            RuleChainImplGameOfWar chain = new RuleChainImplGameOfWar();
//            mockRuleThatJustFiresNextInChain().fireRule(null, chain);
//            fail("RuleForUseWithRuleChain should not be able to fire using the chain if it has not registered with the chain");
//        }
//        catch (RuleNotRegisteredException e) {
//        }
//    }
//

    @Test
    public void fireNextRule_noRulesLeftInChainExitsChain() {
        try {
            chain.fireNextRule(table);
        }
        catch (IndexOutOfBoundsException e) {
            fail("Should have stopped trying to fire more rules when we reached the end of the chain");
        }
    }

    private RuleForUseWithRuleChain mockRuleThatJustFiresNextInChain() {

        return new RuleForUseWithRuleChain() {
            @Override
            public void fireRule(WarTable gameWarTable, RuleChainImplGameOfWar ruleChain) {
                ruleChain.fireNextRule(gameWarTable);
            }
        };
    }

    private RuleForUseWithRuleChain mockRuleThatSetsAWinner() {

        return new RuleForUseWithRuleChain() {
            @Override
            public void fireRule(WarTable gameWarTable, RuleChainImplGameOfWar ruleChain) {
                gameWarTable.setWinner(BaseTest.CHEWY);
                ruleChain.fireNextRule(gameWarTable);
            }
        };
    }

}