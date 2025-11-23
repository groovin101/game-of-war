package com.groovin101.gow.web.dto;

/**
 * Data Transfer Object for game creation requests.
 */
public class CreateGameRequest {
    private int numberOfPlayers = 2;
    private int numberOfSuits = 4;
    private int numberOfRanks = 13;

    public CreateGameRequest() {
    }

    public CreateGameRequest(int numberOfPlayers, int numberOfSuits, int numberOfRanks) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfSuits = numberOfSuits;
        this.numberOfRanks = numberOfRanks;
    }

    // Getters and Setters
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfSuits() {
        return numberOfSuits;
    }

    public void setNumberOfSuits(int numberOfSuits) {
        this.numberOfSuits = numberOfSuits;
    }

    public int getNumberOfRanks() {
        return numberOfRanks;
    }

    public void setNumberOfRanks(int numberOfRanks) {
        this.numberOfRanks = numberOfRanks;
    }
}


