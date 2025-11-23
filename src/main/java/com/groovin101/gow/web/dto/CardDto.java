package com.groovin101.gow.web.dto;

/**
 * Data Transfer Object representing a playing card.
 */
public class CardDto {
    private String rank;
    private String suit;
    private String display;

    public CardDto() {
    }

    public CardDto(String rank, String suit, String display) {
        this.rank = rank;
        this.suit = suit;
        this.display = display;
    }

    // Getters and Setters
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}


