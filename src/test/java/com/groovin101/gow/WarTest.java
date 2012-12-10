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

    @Before
    public void setup() {
        super.setup();
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
        players.add(TESLA);
        game.setPlayers(players);
        game.setTable(tableMock);
        game.playCardsFromAllPlayers(2);
        verify(tableMock).receiveCardsFrom(TESLA, TESLA.playCards(2));
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

    @Ignore
    @Test
    public void testPlayARound_playerWithWinningHandReceivesCards() {
        Player playerMock = mock(Player.class);
        game.setPlayers(buildPlayerList(new Player[]{playerMock, JABBA}));
        game.playARound();
        //verify winning player get cards
//verify(playerMock).addToBottomOfPlayerDeck();
    }

//    public void testDecideNextAction_oneCardHigherThanOther() {
//        game = mock(War.class);
//        game.dealAHand();
//        game.decideNextAction();
//        verify(game).addAllCardsToPlayersDeck(GILDENSTERN);
//    }

    @Test
    public void testDetermineWinner_returnsOwnerOfWinningPile() {

        List<PlayerPile> allPilesOnTable = new ArrayList<PlayerPile>();
        allPilesOnTable.add(new PlayerPile(CHEWY, ACE_OF_CLUBS));

        Table tableMock = mock(Table.class);
        when(tableMock.getAllPilesOnTheTable()).thenReturn(allPilesOnTable);
        game.setTable(tableMock);

        assertEquals("Owner of winning pile should have been identified as winner", CHEWY, game.determineWinnerOfRound());
    }


    @Test
    public void testDivyWonCardsToWinner_allCardsPlayedAreGivenToWinner() {

        List<PlayerPile> allPilesOnTheTable = new ArrayList<PlayerPile>();
        allPilesOnTheTable.add(buildPlayerPile(ROSENCRANTZ, 10));
        allPilesOnTheTable.add(buildPlayerPile(GILDENSTERN, 15));

        Table tableMock = mock(Table.class);
        when(tableMock.getAllPilesOnTheTable()).thenReturn(allPilesOnTheTable);
        game.setTable(tableMock);

        assertEquals("Should start off with no cards (presume they're all on the table)", 0, GILDENSTERN.getPlayerDeckSize());
        game.divyWonCardsToWinner(GILDENSTERN);
        assertEquals("Should have divied all cards from both piles to Gildenstern", 25, GILDENSTERN.getPlayerDeckSize());
    }

    @Test
    public void testDivyWonCardsToWinner_noCardsLeftOnTable() {

        Table tableMock = mock(Table.class);
        game.setTable(tableMock);
        game.divyWonCardsToWinner(GILDENSTERN);
        verify(tableMock).clearAllPilesFromTheTable();
    }

    @Test
    public void testDivyWonCardsToWinner_shufflesPilesBeforeDivyingOut() {
        Table tableMock = mock(Table.class);
        PlayerPile pileMock = mock(PlayerPile.class);
        List<PlayerPile> piles = new ArrayList<PlayerPile>();
        piles.add(pileMock);
        when(tableMock.getAllPilesOnTheTable()).thenReturn(piles);
        game.setTable(tableMock);
        game.divyWonCardsToWinner(GILDENSTERN);
        verify(pileMock).shuffle();

    }

//    protected void divyWonCardsToWinner(Player winner) {
//        List<PlayerPile> allPilesFromTable = table.getAllPilesOnTheTable();
//        for (PlayerPile pileFromTable : allPilesFromTable) {
//            for (Card card : pileFromTable.getCards()) {
//                winner.addToBottomOfPlayerDeck(card);
//            }
//        }
//        table.clearAllPilesFromTheTable();
//    }


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
}