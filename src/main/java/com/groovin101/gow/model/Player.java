package com.groovin101.gow.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 */
public class Player {

    private Deque<Card> playerDeck;

    public Player() {
        playerDeck = new LinkedList<Card>();
    }

    public void addToBottomOfPlayerDeck(Card card) {
        playerDeck.addLast(card);
    }

    public void addToTopOfPlayerDeck(Card card) {
        playerDeck.addFirst(card);
    }

    protected int getPlayerDeckSize() {
        return playerDeck.size();
    }

    public Card revealTopCardOfPlayerDeck() {
        return playerDeck.pop();
    }
}
