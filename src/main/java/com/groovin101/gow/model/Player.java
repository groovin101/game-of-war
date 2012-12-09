package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 */
public class Player {

    private Deque<Card> playerDeck;
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) throws InvalidUsernameException {
        if (StringUtils.isBlank(name)) {
            throw new InvalidUsernameException("Player's name must not be blank");
        }
        this.name = name;
    }

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

    public void dealToTopOfPlayersDeck(Card card) {
        playerDeck.addFirst(card);
    }

    protected int getPlayerDeckSize() {
        return playerDeck.size();
    }

    public Card playACard() {
        List<Card> playedCards = playCards(1);
        return playedCards.isEmpty() ? null : playedCards.get(0);
    }

    public List<Card> playCards(int howMany) {
        List<Card> playedCards = new ArrayList<Card>();
        try {
            while (howMany > 0) {
                playedCards.add(playerDeck.pop());
                howMany--;
            }
        }
        catch (NoSuchElementException e) {
            System.out.println(toString() + " is out of cards");
        }
        return playedCards;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "\'}";
    }
}