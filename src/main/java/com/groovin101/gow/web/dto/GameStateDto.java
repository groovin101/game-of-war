package com.groovin101.gow.web.dto;

import java.util.List;

/**
 * Data Transfer Object representing the current state of a game.
 */
public class GameStateDto {
    private String gameId;
    private List<PlayerStateDto> players;
    private List<RoundResultDto> roundHistory;
    private PlayerStateDto winner;
    private boolean gameOver;
    private boolean isDraw;
    private int roundNumber;
    private String status;

    public GameStateDto() {
    }

    public GameStateDto(String gameId, List<PlayerStateDto> players, List<RoundResultDto> roundHistory,
                        PlayerStateDto winner, boolean gameOver, boolean isDraw, int roundNumber, String status) {
        this.gameId = gameId;
        this.players = players;
        this.roundHistory = roundHistory;
        this.winner = winner;
        this.gameOver = gameOver;
        this.isDraw = isDraw;
        this.roundNumber = roundNumber;
        this.status = status;
    }

    // Getters and Setters
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<PlayerStateDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerStateDto> players) {
        this.players = players;
    }

    public List<RoundResultDto> getRoundHistory() {
        return roundHistory;
    }

    public void setRoundHistory(List<RoundResultDto> roundHistory) {
        this.roundHistory = roundHistory;
    }

    public PlayerStateDto getWinner() {
        return winner;
    }

    public void setWinner(PlayerStateDto winner) {
        this.winner = winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


