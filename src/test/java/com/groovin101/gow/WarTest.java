package com.groovin101.gow;

import com.groovin101.gow.model.Player;
import com.groovin101.gow.model.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 */
public class WarTest {

    private War game;
    private List<Player> players;

    @Before
    public void setup() {
        game = new War();
        players = new ArrayList<Player>();
    }

    @Test
    public void testBuildPlayersList_namesAreUnique() {
        players = game.buildPlayerList(2);
        assertEquals("Should be 2 users", 2, players.size());
        assertFalse("Names of users should have been unique", players.get(0).getName().equals(players.get(1).getName()));
    }

    @Test
    public void testBuildPlayersList_namesReflectUserNumberCorrectly() {
        players = game.buildPlayerList(2);
        assertEquals("Names should reflect player number", "Player 2", players.get(0).getName());
        assertEquals("Names should reflect player number", "Player 1", players.get(1).getName());
    }

    @Test
    public void testPlayCardFromAllPlayers_singlePlayerCallForSingleCardInvokesPlayersPlayACardOnce() {
        Player playerMock = mock(Player.class);
        players.add(playerMock);
        game.setPlayers(players);
        game.playCardsFromAllPlayers(2);
        verify(playerMock).playCards(2);
    }

    @Test
    public void testPlayCardFromAllPlayers_callForTwoCardsInvokesPlayersPlayACardTwice() {
        Player playerOneMock = mock(Player.class);
        Player playerTwoMock = mock(Player.class);
        players.add(playerOneMock);
        players.add(playerTwoMock);
        game.setPlayers(players);
        game.playCardsFromAllPlayers(2);
        verify(playerOneMock).playCards(2);
        verify(playerTwoMock).playCards(2);
    }

    @Test
    public void testPlayCardFromAllPlayers_sendsCardsToTheTable() throws Exception {
        Table tableMock = mock(Table.class);
        Player nikola = new Player("tesla");
        players.add(nikola);
        game.setPlayers(players);
        game.setTable(tableMock);
        game.playCardsFromAllPlayers(2);
        verify(tableMock).receiveCardsFrom(nikola, nikola.playCards(2));
    }
//    @Test
//    public void testFlipTopCards_singlePlayersTopCardsAreAddedToTheEvaluationArea() {
//        game.initializePlay(1, 1, 1);
//        game.flipTopCards();
//        assertTrue("Should contain player 1 cards", game.evaluationArea().containsAll());
//    }

}
