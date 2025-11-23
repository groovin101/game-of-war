package com.groovin101.gow.web.dto;

import java.util.List;

/**
 * Data Transfer Object representing a player's state.
 */
public class PlayerStateDto {
    private String name;
    private int cardCount;
    private List<CardDto> cardsPlayedThisRound;
    private CardDto significantCard;

    public PlayerStateDto() {
    }

    public PlayerStateDto(String name, int cardCount, List<CardDto> cardsPlayedThisRound, CardDto significantCard) {
        this.name = name;
        this.cardCount = cardCount;
        this.cardsPlayedThisRound = cardsPlayedThisRound;
        this.significantCard = significantCard;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public List<CardDto> getCardsPlayedThisRound() {
        return cardsPlayedThisRound;
    }

    public void setCardsPlayedThisRound(List<CardDto> cardsPlayedThisRound) {
        this.cardsPlayedThisRound = cardsPlayedThisRound;
    }

    public CardDto getSignificantCard() {
        return significantCard;
    }

    public void setSignificantCard(CardDto significantCard) {
        this.significantCard = significantCard;
    }
}


