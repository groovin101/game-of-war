package com.groovin101.gow;

import com.groovin101.gow.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.*;

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

    @Test
    public void testPlayCardFromAllPlayers_singlePlayerCallForSingleCardInvokesPlayersPlayACardOnce() {
        players = new ArrayList<Player>();
        Player mockedPlayer = mock(Player.class);
        players.add(mockedPlayer);
        game.setPlayers(players);
        game.playCardFromAllPlayers(2);
        verify(mockedPlayer).playACard(2);
    }

    @Test
    public void testPlayCardFromAllPlayers_callForTwoCardsInvokesPlayersPlayACardTwice() {
        players = new ArrayList<Player>();
        Player mockedPlayerOne = mock(Player.class);
        Player mockedPlayerTwo = mock(Player.class);
        players.add(mockedPlayerOne);
        players.add(mockedPlayerTwo);
        game.setPlayers(players);
        game.playCardFromAllPlayers(2);
        verify(mockedPlayerOne).playACard(2);
        verify(mockedPlayerTwo).playACard(2);
    }

//    @Test
//    public void testFlipTopCards_singlePlayersTopCardsAreAddedToTheEvaluationArea() {
//        game.initializePlay(1, 1, 1);
//        game.flipTopCards();
//        assertTrue("Should contain player 1 cards", game.evaluationArea().containsAll());
//    }

}
