package com.groovin101.gow.web.controller;

import com.groovin101.gow.web.dto.CreateGameRequest;
import com.groovin101.gow.web.dto.GameStateDto;
import com.groovin101.gow.web.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Game of War operations.
 */
@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Creates a new game.
     * POST /api/games
     */
    @PostMapping
    public ResponseEntity<GameStateDto> createGame(@RequestBody CreateGameRequest request) {
        try {
            GameStateDto gameState = gameService.createGame(
                    request.getNumberOfPlayers(),
                    request.getNumberOfSuits(),
                    request.getNumberOfRanks()
            );
            return ResponseEntity.ok(gameState);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets the current state of a game.
     * GET /api/games/{gameId}
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateDto> getGameState(@PathVariable String gameId) {
        try {
            GameStateDto gameState = gameService.getGameState(gameId);
            return ResponseEntity.ok(gameState);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Plays a single round of the game.
     * POST /api/games/{gameId}/play-round
     */
    @PostMapping("/{gameId}/play-round")
    public ResponseEntity<GameStateDto> playRound(@PathVariable String gameId) {
        try {
            GameStateDto gameState = gameService.playRound(gameId);
            return ResponseEntity.ok(gameState);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Lists all active games.
     * GET /api/games
     */
    @GetMapping
    public ResponseEntity<List<String>> listGames() {
        List<String> games = gameService.listGames();
        return ResponseEntity.ok(games);
    }

    /**
     * Deletes a game.
     * DELETE /api/games/{gameId}
     */
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }
}


