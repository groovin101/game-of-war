package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;
import java.util.LinkedList;

/**
 */
public class Player {

    private Deque<Card> playerDeck;
    private String name;

    public Player(String name) throws InvalidUsernameException {
        if (StringUtils.isBlank(name)) {
            throw new InvalidUsernameException("Player's name must not be blank");
        }
        this.name = name;
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

    @Override
    public String toString() {
        return "Player{name='" + name + "\'}";
    }
}