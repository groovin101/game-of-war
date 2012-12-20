package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 */
public class Player {

    private Deque<Card> playerDeck;
    private String name;
    private List<Card> throwdownCards; //todo: consider renaming to throwdownPile, not to be confused with mult piles on table
    private List<Card> currentHand;
    private List<Card> cardsPlayedThisRound;

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
        cardsPlayedThisRound = new ArrayList<Card>();
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

    Deque<Card> getPlayerDeck() {
        return playerDeck;
    }

    public void battle() {
        currentHand.clear();
        currentHand.add(playerDeck.pop());
        cardsPlayedThisRound.addAll(currentHand);
    }

    public void war() {
        currentHand.clear();
        try {
            currentHand.add(playerDeck.pop());
            currentHand.add(playerDeck.pop());
            currentHand.add(playerDeck.pop());
        }
        catch (NoSuchElementException e) {
//            new NoCardsToPlayException("Player: " + getName() + " is out of cards", e).printStackTrace();
            System.out.println("Player: " + getName() + " is out of cards");
        }
        cardsPlayedThisRound.addAll(currentHand);
    }

    /**
     * The most recent hand played in a round
     * @return
     */
    public List<Card> getCurrentHand() {
        return currentHand;
    }
    void setCurrentHand(List<Card> currentHand) {
        this.currentHand = currentHand;
    }
    public List<Card> getCardsPlayedThisRound() {
        return cardsPlayedThisRound;
    }
    void setCardsPlayedThisRound(List<Card> cardsPlayedThisRound) {
        this.cardsPlayedThisRound = cardsPlayedThisRound;
    }

    /**
     * Returns the significant card to use when comparing against other players hands in order to determine a winner
     * @return
     */
    public Card getSignificantCard() {
        return currentHand.get(currentHand.size()-1);
    }

    public void clearCardsFromPreviousRound() {
        cardsPlayedThisRound.clear();
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "\'}";
    }

}