package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.NoCardsToPlayException;
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

    public int getPlayerDeckSize() {
        return playerDeck.size();
    }

    //todo: remove return type
    public Card playACard(GameTable gameTable) throws NoCardsToPlayException {
        List<Card> playedCards = playCards(1, gameTable);
        return playedCards.isEmpty() ? null : playedCards.get(0);
    }

    //todo: remove return type
    public List<Card> playCards(int howMany, GameTable gameTable) throws NoCardsToPlayException {
        List<Card> playedCards = new ArrayList<Card>();
        try {
            while (howMany > 0) {
                playedCards.add(playerDeck.pop());
                howMany--;
            }
        }
        catch (NoSuchElementException e) {
            if (playedCards.isEmpty()) {
                throw new NoCardsToPlayException(getName() + " is out of cards");
            }
        }
        gameTable.playAHand(this, playedCards);
        return playedCards;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "\'}";
    }
}