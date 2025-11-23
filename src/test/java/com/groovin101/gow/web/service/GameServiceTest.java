package com.groovin101.gow.web.service;

import com.groovin101.gow.web.dto.GameStateDto;
import com.groovin101.gow.web.dto.PlayerStateDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for GameService web layer functionality.
 */
public class GameServiceTest {

    private GameService gameService;

    @Before
    public void setUp() {
        gameService = new GameService();
    }

    @Test
    public void testCreateGame_createsGameWithPlayers() {
        GameStateDto gameState = gameService.createGame(2, 4, 13);

        assertNotNull("Game state should not be null", gameState);
        assertNotNull("Game ID should not be null", gameState.getGameId());
        assertEquals("Should have 2 players", 2, gameState.getPlayers().size());
        assertEquals("Round number should be 0", 0, gameState.getRoundNumber());
        assertFalse("Game should not be over", gameState.isGameOver());
    }

    @Test
    public void testPlayRound_playersHaveCardsPlayedThisRound() {
        // Create a game
        GameStateDto initialState = gameService.createGame(2, 4, 13);
        String gameId = initialState.getGameId();

        // Play a round
        GameStateDto stateAfterRound = gameService.playRound(gameId);

        // Verify round was played
        assertEquals("Round number should be 1", 1, stateAfterRound.getRoundNumber());

        // Critical test: Verify players have cards in cardsPlayedThisRound
        boolean atLeastOnePlayerHasCards = false;
        for (PlayerStateDto player : stateAfterRound.getPlayers()) {
            if (player.getCardsPlayedThisRound() != null && !player.getCardsPlayedThisRound().isEmpty()) {
                atLeastOnePlayerHasCards = true;
                
                // Verify card data is complete
                player.getCardsPlayedThisRound().forEach(card -> {
                    assertNotNull("Card rank should not be null", card.getRank());
                    assertNotNull("Card suit should not be null", card.getSuit());
                    assertNotNull("Card display should not be null", card.getDisplay());
                });
            }
        }

        assertTrue("At least one player should have cards played this round", atLeastOnePlayerHasCards);
    }

    @Test
    public void testPlayRound_significantCardIsSet() {
        // Create a game
        GameStateDto initialState = gameService.createGame(2, 4, 13);
        String gameId = initialState.getGameId();

        // Play a round
        GameStateDto stateAfterRound = gameService.playRound(gameId);

        // At least one player should have a significant card (the battle card)
        boolean atLeastOnePlayerHasSignificantCard = false;
        for (PlayerStateDto player : stateAfterRound.getPlayers()) {
            if (player.getSignificantCard() != null) {
                atLeastOnePlayerHasSignificantCard = true;
                assertNotNull("Significant card rank should not be null", player.getSignificantCard().getRank());
                assertNotNull("Significant card suit should not be null", player.getSignificantCard().getSuit());
            }
        }

        assertTrue("At least one player should have a significant card", atLeastOnePlayerHasSignificantCard);
    }

    @Test
    public void testPlayRound_cardCountDecreases() {
        // Create a game
        GameStateDto initialState = gameService.createGame(2, 4, 13);
        String gameId = initialState.getGameId();
        
        int initialTotalCards = initialState.getPlayers().stream()
                .mapToInt(PlayerStateDto::getCardCount)
                .sum();

        // Play a round
        GameStateDto stateAfterRound = gameService.playRound(gameId);

        int finalTotalCards = stateAfterRound.getPlayers().stream()
                .mapToInt(PlayerStateDto::getCardCount)
                .sum();

        // Total cards should remain the same (cards just moved around)
        assertEquals("Total cards should remain constant", initialTotalCards, finalTotalCards);
    }

    @Test
    public void testPlayRound_roundHistoryIsRecorded() {
        // Create a game
        GameStateDto initialState = gameService.createGame(2, 4, 13);
        String gameId = initialState.getGameId();

        // Play a round
        GameStateDto stateAfterRound = gameService.playRound(gameId);

        // Verify round history
        assertNotNull("Round history should not be null", stateAfterRound.getRoundHistory());
        assertEquals("Round history should have 1 entry", 1, stateAfterRound.getRoundHistory().size());
        
        assertEquals("Round history should show round 1", 1, stateAfterRound.getRoundHistory().get(0).getRoundNumber());
        assertNotNull("Round history should have a winner", stateAfterRound.getRoundHistory().get(0).getWinnerName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayRound_nonExistentGameThrowsException() {
        gameService.playRound("non-existent-id");
    }

    @Test
    public void testGetGameState_returnsCurrentState() {
        // Create a game
        GameStateDto initialState = gameService.createGame(2, 4, 13);
        String gameId = initialState.getGameId();

        // Get state
        GameStateDto retrievedState = gameService.getGameState(gameId);

        assertNotNull("Retrieved state should not be null", retrievedState);
        assertEquals("Game IDs should match", gameId, retrievedState.getGameId());
        assertEquals("Player count should match", 2, retrievedState.getPlayers().size());
    }

    @Test
    public void testListGames_includesCreatedGame() {
        // Initially should be empty or have existing games
        int initialCount = gameService.listGames().size();

        // Create a game
        GameStateDto gameState = gameService.createGame(2, 4, 13);

        // List should now include the new game
        assertEquals("Game list should include new game", initialCount + 1, gameService.listGames().size());
        assertTrue("Game list should contain created game ID", 
                   gameService.listGames().contains(gameState.getGameId()));
    }

    @Test
    public void testDeleteGame_removesGame() {
        // Create a game
        GameStateDto gameState = gameService.createGame(2, 4, 13);
        String gameId = gameState.getGameId();

        // Verify it exists
        assertTrue("Game should exist", gameService.listGames().contains(gameId));

        // Delete it
        gameService.deleteGame(gameId);

        // Verify it's gone
        assertFalse("Game should be deleted", gameService.listGames().contains(gameId));
    }
}

