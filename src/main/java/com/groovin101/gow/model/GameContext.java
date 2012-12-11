package com.groovin101.gow.model;

import java.util.List;

/**
 */
public interface GameContext {

    public void playAHand(Player player, List<Card> cardsPassed);

    public List<Card> retrieveCardsDealtFrom(Player player);

    public Player getWinnerOfRound();

    public void clearAllPilesFromTheTable();
}
