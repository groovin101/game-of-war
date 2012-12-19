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
    private List<Card> throwdownCards; //todo: consider renaming to throwdownPile, not to be confused with mult piles on table
    private List<Card> currentHand;

    public String getName() {
        return name;
    }

    public Player(String name) throws InvalidUsernameException {
        if (StringUtils.isBlank(name)) {
            throw new InvalidUsernameException("Player's name must not be blank");
        }
        this.name = name;
        playerDeck = new LinkedList<Card>();
        throwdownCards = new ArrayList<Card>();
        currentHand = new ArrayList<Card>();
    }

    public List<Card> getThrowdownCards() {
        return throwdownCards;
    }
    public void setThrowdownCards(List<Card> throwdownCards) {
        this.throwdownCards = throwdownCards;
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
        throwdownCards = playCards(1, gameTable);
        return throwdownCards.isEmpty() ? null : throwdownCards.get(0);
    }

    //todo: rename to playACard
    public Card throwNextCard() {
        Card played = playerDeck.pop();
        throwdownCards.add(played);
        return played;
    }

//    public List<Card> throwCards(int howMany) {
//
//    }

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

    public void battle() {
        currentHand.clear();
        currentHand.add(throwNextCard());
    }

    public void war() {

    }

    /**
     * The most recent hand played in a round
     * @return
     */
    public List<Card> getCurrentHand() {
        return currentHand;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "\'}";
    }

}