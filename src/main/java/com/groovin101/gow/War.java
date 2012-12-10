package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class War {

    private List<Player> players;
    private Dealer dealer;
    private DeckExtended deck;
    private Table table;

    //todo: add a play method that takes a list of usernames so that we have a chance to throw our InvalidUsernameException, allowing it to bubble


    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void setTable(Table table) {
        this.table = table;
    }

    public War() {
        dealer = new Dealer();
        table = new Table();
    }

    public void play(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {

        startTheGame(numberOfSuits, numberOfRanks, numberOfPlayers);

        //first lets pretend theres only a single round of play.........

        playARound();

        //flipped cards are compared
//        comparePlayedCards();
            //player with highest card adds all cards to bottom of their pile

        //randomize pickups to ensure no endless games

    }

    protected void playARound() {
        playCardsFromAllPlayers(1);
    }

    protected PlayerPile identifyWinningPile(List<PlayerPile> piles) {
        Collections.sort(piles);
        return piles.get(piles.size()-1);
    }

    protected Player determineWinner() {
        return identifyWinningPile(table.getAllPilesOnTheTable()).getPlayer();
    }

    protected void playCardsFromAllPlayers(int howMany) {
        for (Player player : players) {
            table.receiveCardsFrom(player, player.playCards(howMany));
        }
    }

    private void startTheGame(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {
        players = buildPlayerList(numberOfPlayers);
        deck = new DeckImpl();
        deck.shuffle();
        dealToPlayers();
    }

    protected void dealToPlayers() {
        dealer.deal(deck, players);
    }

    protected List<Player> buildPlayerList(int numberOfPlayers) {
        List<Player> players = new ArrayList<Player>();
        while (numberOfPlayers > 0) {
            try {
                players.add(new Player("Player " + numberOfPlayers--));
            }
            catch (InvalidUsernameException e) {
                e.printStackTrace();
            }
        }
        return players;
    }

    public void playAHand(HandType handType) {
        if (handType.equals(HandType.SINGLE_CARD_HAND)) {
            playCardsFromAllPlayers(1);
        }
        else {
            playCardsFromAllPlayers(4);
        }
    }

    public enum HandType {
        SINGLE_CARD_HAND,
        WAR_STYLE_HAND
    }
}