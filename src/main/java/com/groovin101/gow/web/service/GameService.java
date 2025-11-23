package com.groovin101.gow.web.service;

import com.groovin101.gow.War;
import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.Player;
import com.groovin101.gow.web.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing War game instances and their state.
 */
@Service
public class GameService {

    private final Map<String, GameInstance> activeGames = new ConcurrentHashMap<>();

    /**
     * Creates a new game instance.
     */
    public GameStateDto createGame(int numberOfPlayers, int numberOfSuits, int numberOfRanks) {
        String gameId = UUID.randomUUID().toString();
        War game = new War();
        game.startTheGame(numberOfSuits, numberOfRanks, numberOfPlayers);

        GameInstance instance = new GameInstance(game, numberOfSuits, numberOfRanks);
        activeGames.put(gameId, instance);

        return buildGameState(gameId, instance);
    }

    /**
     * Plays a single round of the game.
     */
    public GameStateDto playRound(String gameId) {
        GameInstance instance = activeGames.get(gameId);
        if (instance == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        War game = instance.getGame();
        if (!game.gameOver()) {
            int roundsBefore = instance.getRoundHistory().size();
            boolean wasWar = false;

            // Capture state before round
            game.doBattle();
            wasWar = game.isWarCalledFor();

            while (game.isWarCalledFor()) {
                game.doWar();
            }

            Player winner = game.getWinnerOfTheLastHandPlayed();
            if (winner == null) {
                game.setGameIsADraw(true);
            }

            game.divySpoilsToWinner(winner);
            game.removePlayersWithNoCards();

            // Record round result
            RoundResultDto roundResult = new RoundResultDto(
                    instance.getRoundHistory().size() + 1,
                    buildPlayerStates(game.getPlayers()),
                    winner != null ? winner.getName() : "Draw",
                    wasWar
            );
            instance.getRoundHistory().add(roundResult);

            game.clearCardsFromPreviousRound();
        }

        return buildGameState(gameId, instance);
    }

    /**
     * Gets the current state of a game.
     */
    public GameStateDto getGameState(String gameId) {
        GameInstance instance = activeGames.get(gameId);
        if (instance == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return buildGameState(gameId, instance);
    }

    /**
     * Lists all active games.
     */
    public List<String> listGames() {
        return new ArrayList<>(activeGames.keySet());
    }

    /**
     * Deletes a game.
     */
    public void deleteGame(String gameId) {
        activeGames.remove(gameId);
    }

    private GameStateDto buildGameState(String gameId, GameInstance instance) {
        War game = instance.getGame();
        List<PlayerStateDto> playerStates = buildPlayerStates(game.getPlayers());

        PlayerStateDto winnerDto = null;
        if (game.gameOver() && !instance.getGame().gameIsADraw) {
            // Find the player with all the cards
            for (Player player : game.getPlayers()) {
                if (player.getPlayerDeckSize() == instance.getNumberOfSuits() * instance.getNumberOfRanks()) {
                    winnerDto = new PlayerStateDto(player.getName(), player.getPlayerDeckSize(), null, null);
                    break;
                }
            }
        }

        String status = game.gameOver() 
            ? (instance.getGame().gameIsADraw ? "Draw" : "Game Over") 
            : "In Progress";

        return new GameStateDto(
                gameId,
                playerStates,
                instance.getRoundHistory(),
                winnerDto,
                game.gameOver(),
                instance.getGame().gameIsADraw,
                instance.getRoundHistory().size(),
                status
        );
    }

    private List<PlayerStateDto> buildPlayerStates(Set<Player> players) {
        return players.stream()
                .map(player -> {
                    List<CardDto> cardsPlayed = player.getCardsPlayedThisRound().stream()
                            .map(this::convertToCardDto)
                            .collect(Collectors.toList());
                    CardDto significantCard = player.getSignificantCard() != null 
                        ? convertToCardDto(player.getSignificantCard()) 
                        : null;
                    return new PlayerStateDto(
                            player.getName(),
                            player.getPlayerDeckSize(),
                            cardsPlayed,
                            significantCard
                    );
                })
                .collect(Collectors.toList());
    }

    private CardDto convertToCardDto(Card card) {
        return new CardDto(
                card.getRank().toString(),
                card.getSuit().toString(),
                card.toString()
        );
    }

    /**
     * Internal class to track game instance data.
     */
    private static class GameInstance {
        private final War game;
        private final List<RoundResultDto> roundHistory;
        private final int numberOfSuits;
        private final int numberOfRanks;

        public GameInstance(War game, int numberOfSuits, int numberOfRanks) {
            this.game = game;
            this.roundHistory = new ArrayList<>();
            this.numberOfSuits = numberOfSuits;
            this.numberOfRanks = numberOfRanks;
        }

        public War getGame() {
            return game;
        }

        public List<RoundResultDto> getRoundHistory() {
            return roundHistory;
        }

        public int getNumberOfSuits() {
            return numberOfSuits;
        }

        public int getNumberOfRanks() {
            return numberOfRanks;
        }
    }
}

