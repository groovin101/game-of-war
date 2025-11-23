package com.groovin101.gow.web.dto;

import java.util.List;

/**
 * Data Transfer Object representing the result of a game round.
 */
public class RoundResultDto {
    private int roundNumber;
    private List<PlayerStateDto> playerStates;
    private String winnerName;
    private boolean wasWar;

    public RoundResultDto() {
    }

    public RoundResultDto(int roundNumber, List<PlayerStateDto> playerStates, String winnerName, boolean wasWar) {
        this.roundNumber = roundNumber;
        this.playerStates = playerStates;
        this.winnerName = winnerName;
        this.wasWar = wasWar;
    }

    // Getters and Setters
    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public List<PlayerStateDto> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(List<PlayerStateDto> playerStates) {
        this.playerStates = playerStates;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public boolean isWasWar() {
        return wasWar;
    }

    public void setWasWar(boolean wasWar) {
        this.wasWar = wasWar;
    }
}


