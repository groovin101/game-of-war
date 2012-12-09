package com.groovin101.gow;

import com.groovin101.gow.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 */
public class WarTest {

    private War game;
    private List<Player> players;

    @Before
    public void setup() {
        game = new War();
        players = game.buildPlayerList(2);
    }

    @Test
    public void testBuildPlayersList_namesAreUnique() {
        assertEquals("Should be 2 users", 2, players.size());
        assertFalse("Names of users should have been unique", players.get(0).getName().equals(players.get(1).getName()));
    }

    @Test
    public void testBuildPlayersList_namesReflectUserNumberCorrectly() {
        assertEquals("Names should reflect player number", "Player 2", players.get(0).getName());
        assertEquals("Names should reflect player number", "Player 1", players.get(1).getName());
    }

//    @Test
//    public void testFlipTopCards_singlePlayersTopCardsAreAddedToTheEvaluationArea() {
//        game.initializePlay(1, 1, 1);
//        game.flipTopCards();
//        assertTrue("Should contain player 1 cards", game.evaluationArea().containsAll());
//    }

}
