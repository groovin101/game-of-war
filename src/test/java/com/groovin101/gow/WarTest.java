package com.groovin101.gow;

import com.groovin101.gow.model.*;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
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
    public void testFetchAllCardsPlayedThisRound_cardsPlayedByTwoPlayersAreBothReturned() {

        Player mockPlayerOne = Mockito.mock(Player.class);
        Player mockPlayerTwo = Mockito.mock(Player.class);
        when(mockPlayerOne.getCardsPlayedThisRound()).thenReturn(buildCardList(new Card[]{ACE_OF_SPADES,KING_OF_SPADES}));
        when(mockPlayerTwo.getCardsPlayedThisRound()).thenReturn(buildCardList(new Card[]{QUEEN_OF_HEARTS}));
        game.addPlayer(mockPlayerOne);
        game.addPlayer(mockPlayerTwo);

        Collection<Card> allCardsThatShouldHaveBeenPlayedThisRound = new ArrayList<Card>(buildCardList(new Card[]{QUEEN_OF_HEARTS,KING_OF_SPADES,ACE_OF_SPADES}));
        assertTrue("Should have grabbed cards played by both players but instead grabbed " + game.fetchAllCardsPlayedThisRound(),
                game.fetchAllCardsPlayedThisRound().containsAll(allCardsThatShouldHaveBeenPlayedThisRound));
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
        assertFalse("Don't start a war if somebody won a battle", game.shouldStartAWar());
    }

    @Test
    public void testShouldStartAWar_ifABatteHasBeenWon() {
        War game = new War();
        game.setWinnerOfTheLastRound(null);
        assertTrue("If nobody won the battle, then we must war!", game.shouldStartAWar());
    }

    @Test
    public void testClearCardsFromPreviousRound_callsClearCardsOnAllPlayers() {
        Player playerMockOne = mock(Player.class);
        Player playerMockTwo = mock(Player.class);
        game.addPlayer(playerMockOne);
        game.addPlayer(playerMockTwo);
        game.clearCardsFromPreviousRound();
        verify(playerMockOne).clearCardsFromPreviousRound();
        verify(playerMockTwo).clearCardsFromPreviousRound();
    }

    private void dealTo(Player player, Card card) {
        player.dealToTopOfPlayersDeck(card);
    }

}