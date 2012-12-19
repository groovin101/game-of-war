package com.groovin101.gow;

import com.groovin101.gow.model.*;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 */
public class WarTest extends BaseTest {

    private War game;
    private List<Player> players;
    private GameTable gameTable;

    @Before
    public void setup() {
        super.setup();
        game = new War();
        players = new ArrayList<Player>();
        gameTable = new GameTable();
    }

    @Test
    public void testBuildPlayersList_namesAreUnique() {
        players = new ArrayList<Player>(game.buildPlayerList(2));
        assertEquals("Should be 2 users", 2, players.size());
        assertFalse("Names of users should have been unique", players.get(0).getName().equals(players.get(1).getName()));
    }

    @Test
    public void testBuildPlayersList_namesReflectUserNumberCorrectly() {
        players = new ArrayList<Player>(game.buildPlayerList(2));
        assertEquals("Names should reflect player number", "Player 2", players.get(0).getName());
        assertEquals("Names should reflect player number", "Player 1", players.get(1).getName());
    }

    @Test
    public void testRemovePlayersWithNoCards_doesNotRemoveAPlayerWhoHasCards() {
        game.addPlayer(ROSENCRANTZ);
        dealTo(game.getPlayer(ROSENCRANTZ.getName()), ACE_OF_CLUBS);
        assertEquals(1, game.getPlayers().size());
        game.removePlayersWithNoCards();
        assertEquals(1, game.getPlayers().size());
    }

    @Test
    public void testRemovePlayersWithNoCards_doesRemoveAPlayerWhoHasNoCards() {
        game.addPlayer(ROSENCRANTZ);
        assertEquals(1, game.getPlayers().size());
        game.removePlayersWithNoCards();
        assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testPlayCardFromAllPlayers_singlePlayerCallForSingleCardInvokesPlayersPlayACardOnce() throws Exception {
        Player playerMock = mock(Player.class);
        when(playerMock.getPlayerDeckSize()).thenReturn(1);
        players.add(playerMock);
        game.setPlayers(players);
        game.playCardsFromAllPlayers(2, gameTable);
        verify(playerMock).playCards(2, gameTable);
    }

    @Test
    public void testPlayCardFromAllPlayers_callForTwoCardsInvokesPlayCardOnAllPlayers() throws Exception {
        Player playerOneMock = mock(Player.class);
        Player playerTwoMock = mock(Player.class);
        when(playerOneMock.getPlayerDeckSize()).thenReturn(1);
        when(playerTwoMock.getPlayerDeckSize()).thenReturn(1);
        players.add(playerOneMock);
        players.add(playerTwoMock);
        game.setPlayers(players);
        game.playCardsFromAllPlayers(2, gameTable);
        verify(playerOneMock).playCards(2, gameTable);
        verify(playerTwoMock).playCards(2, gameTable);
    }

    @Test
    public void testIdentifyWinningPile_twoSingleCardPiles() throws Exception {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        PlayerPile pileWithAce = new PlayerPile(TESLA, ACE_OF_CLUBS);
        PlayerPile pileWithKing = new PlayerPile(THE_DUDE, KING_OF_SPADES);
        playerPiles.add(pileWithAce);
        playerPiles.add(pileWithKing);
        assertEquals("Ace pile should've won", pileWithAce, game.identifyWinningPile(playerPiles));
    }


    @Test
    public void testIdentifyWinningPile_singleCardPileVsEmptyPile() throws Exception {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        List<Card> nullCardList = null;
        PlayerPile emptyPile = new PlayerPile(THE_DUDE, nullCardList);
        PlayerPile pileWithAce = new PlayerPile(TESLA, ACE_OF_CLUBS);
        playerPiles.add(emptyPile);
        playerPiles.add(pileWithAce);
        assertEquals("Ace pile should've won", pileWithAce, game.identifyWinningPile(playerPiles));
    }

    @Test
    public void testIdentifyWinningPile_singleCardPileVsNoPile() throws Exception {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        PlayerPile pileWithAce = new PlayerPile(TESLA, ACE_OF_CLUBS);
        playerPiles.add(pileWithAce);
        assertEquals("Ace pile should've won since it is competing with nothing", pileWithAce, game.identifyWinningPile(playerPiles));
    }

    @Test
    public void testIdentifyWinningPile_twoMultiCardPiles() {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        PlayerPile pileWithQueenHigh = new PlayerPile(TESLA, buildCardList(new Card[]{ACE_OF_CLUBS, QUEEN_OF_HEARTS}));
        PlayerPile pileWithKingHigh = new PlayerPile(CHEWY, buildCardList(new Card[]{KING_OF_SPADES}));
        playerPiles.add(pileWithQueenHigh);
        playerPiles.add(pileWithKingHigh);
        assertEquals("King pile should've won", pileWithKingHigh, game.identifyWinningPile(playerPiles));
    }

    @Test
    public void testDivyWonCardsToWinner_allCardsPlayedAreGivenToWinner() {

        List<PlayerPile> allPilesOnTheTable = new ArrayList<PlayerPile>();
        allPilesOnTheTable.add(buildPlayerPile(ROSENCRANTZ, 10));
        allPilesOnTheTable.add(buildPlayerPile(GILDENSTERN, 15));

        GameTable gameTableMock = mock(GameTable.class);
        when(gameTableMock.getAllPilesOnTheTable()).thenReturn(allPilesOnTheTable);
        game.setGameTable(gameTableMock);

        assertEquals("Should start off with no cards (presume they're all on the table)", 0, GILDENSTERN.getPlayerDeckSize());
        game.divyWonCardsToWinner(GILDENSTERN);
        assertEquals("Should have divied all cards from both piles to Gildenstern", 25, GILDENSTERN.getPlayerDeckSize());
    }

    @Test
    public void testDivyWonCardsToWinner_noCardsLeftOnTable() {

        GameTable gameTableMock = mock(GameTable.class);
        game.setGameTable(gameTableMock);
        game.divyWonCardsToWinner(GILDENSTERN);
        verify(gameTableMock).clearAllPilesFromTheTable();
    }

    @Ignore
    @Test
    public void testDivyWonCardsToWinner_shufflesPilesBeforeDivyingOut() {
        GameTable gameTableMock = mock(GameTable.class);
        PlayerPile pileMock = mock(PlayerPile.class);
        List<PlayerPile> piles = new ArrayList<PlayerPile>();
        piles.add(pileMock);
        when(gameTableMock.getAllPilesOnTheTable()).thenReturn(piles);
        game.setGameTable(gameTableMock);
        game.divyWonCardsToWinner(GILDENSTERN);
        verify(pileMock).shuffle();

    }

    @Test
    public void testDoesOnePlayerHaveAllCards_noBecauseNoPlayersHaveAnyCardsYet() {
        DeckExtended deck = new DeckImpl();
        assertFalse("No player should have all the cards at the start of the game",
                game.doesOnePlayerHaveAllTheCards(deck, buildPlayerList(new Player[]{ROSENCRANTZ, GILDENSTERN})));
    }

    @Test
    public void testDoesOnePlayerHaveAllCards_noBecauseCardsAreEvenlySplit() {
        DeckExtended deck = new DeckImpl();
        Dealer dealer = new Dealer();
        players = buildPlayerList(new Player[] {ROSENCRANTZ, GILDENSTERN});
        dealer.dealAllCards(deck, players);
        assertFalse("No player should have all the cards right after dealing",
                game.doesOnePlayerHaveAllTheCards(deck, players));
    }

    @Test
    public void testDoesOnePlayerHaveAllCards_yesBecauseDealerJustDealtThemAllToASinglePlayer() {
        DeckExtended deck = new DeckImpl();
        Dealer dealer = new Dealer();
        players = buildPlayerList(new Player[] {JABBA});
        dealer.dealAllCards(deck, players);
        assertTrue("Jabba the Hut is holding all of the cards after the dealer Solo'd him out ;)",
                game.doesOnePlayerHaveAllTheCards(deck, players));
    }

    @Test
    public void testShouldStartAWar_notIfABatteHasBeenWon() {
        War game = new War();
        game.setWinnerOfTheLastRound(TESLA);
        assertFalse("Don't start a war if somebody won a battle", game.shouldStartAWar(gameTable));
    }

    @Test
    public void testShouldStartAWar_ifABatteHasBeenWon() {
        War game = new War();
        game.setWinnerOfTheLastRound(null);
        assertTrue("If nobody won the battle, then we must war!", game.shouldStartAWar(gameTable));
    }

    private void dealTo(Player player, Card card) {
        player.dealToTopOfPlayersDeck(card);
    }

}