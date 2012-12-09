package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class War {

    private List<Player> players;
    private Dealer dealer;
    private DeckExtended deck;

    //todo: add a play method that takes a list of usernames so that we have a chance to throw our InvalidUsernameException, allowing it to bubble


    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void play(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {

        initializeGame(numberOfSuits, numberOfRanks, numberOfPlayers);

        playCardFromAllPlayers(1);

        //flipped cards are compared
            //player with highest card adds all cards to bottom of their pile

        //randomize pickups to ensure no endless games

    }

    //todo: send these to the table
    protected void playCardFromAllPlayers(int howMany) {
        for (Player player : players) {
            player.playACard(howMany);
        }
    }

    private void initializeGame(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {
        dealer = new Dealer();
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
}