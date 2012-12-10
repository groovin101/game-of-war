package com.groovin101.gow;

import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.Player;
import com.groovin101.gow.model.PlayerPile;
import com.groovin101.gow.model.Table;
import com.groovin101.gow.test.utils.CardBuilder;
import com.groovin101.gow.test.utils.PlayerBuilder;
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
        players.add(PlayerBuilder.TESLA);
        game.setPlayers(players);
        game.setTable(tableMock);
        game.playCardsFromAllPlayers(2);
        verify(tableMock).receiveCardsFrom(PlayerBuilder.TESLA, PlayerBuilder.TESLA.playCards(2));
    }

    @Test
    public void testIdentifyWinningPile_twoSingleCardPiles() throws Exception {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        PlayerPile pileWithAce = new PlayerPile(PlayerBuilder.TESLA, CardBuilder.ACE_OF_CLUBS);
        PlayerPile pileWithKing = new PlayerPile(PlayerBuilder.THE_DUDE, CardBuilder.KING_OF_SPADES);
        playerPiles.add(pileWithAce);
        playerPiles.add(pileWithKing);
        assertEquals("Ace pile should've won", pileWithAce, game.identifyWinningPile(playerPiles));
    }

    @Test
    public void testIdentifyWinningPile_singleCardPileVsEmptyPile() throws Exception {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        List<Card> nullCardList = null;
        PlayerPile emptyPile = new PlayerPile(PlayerBuilder.THE_DUDE, nullCardList);
        PlayerPile pileWithAce = new PlayerPile(PlayerBuilder.TESLA, CardBuilder.ACE_OF_CLUBS);
        playerPiles.add(emptyPile);
        playerPiles.add(pileWithAce);
        assertEquals("Ace pile should've won", pileWithAce, game.identifyWinningPile(playerPiles));
    }

    @Test
    public void testIdentifyWinningPile_singleCardPileVsNoPile() throws Exception {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        PlayerPile pileWithAce = new PlayerPile(PlayerBuilder.TESLA, CardBuilder.ACE_OF_CLUBS);
        playerPiles.add(pileWithAce);
        assertEquals("Ace pile should've won since it is competing with nothing", pileWithAce, game.identifyWinningPile(playerPiles));
    }

    @Test
    public void testIdentifyWinningPile_twoMultiCardPiles() {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        PlayerPile pileWithQueenHigh = new PlayerPile(PlayerBuilder.TESLA, CardBuilder.buildCardList(new Card[] {CardBuilder.ACE_OF_CLUBS, CardBuilder.QUEEN_OF_HEARTS}));
        PlayerPile pileWithKingHigh = new PlayerPile(PlayerBuilder.CHEWY, CardBuilder.buildCardList(new Card[] {CardBuilder.KING_OF_SPADES}));
        playerPiles.add(pileWithQueenHigh);
        playerPiles.add(pileWithKingHigh);
        assertEquals("King pile should've won", pileWithKingHigh, game.identifyWinningPile(playerPiles));
    }
}