package com.groovin101.gow;

import com.groovin101.gow.model.Deck;
import com.groovin101.gow.model.DeckImpl;
import com.groovin101.gow.model.Player;

/**
 */
public class War {

    private Player[] players;

    //todo: assume standard play first, then explore alternative scenarios: 52 card, 2 players
    public void play(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {
        players = new Player[numberOfPlayers];
        Deck deck = new DeckImpl();
        deck.shuffle();

        //deal cards to players

        //all players flip their top card

        //flipped cards are compared
            //player with highest card adds all cards to bottom of their pile

        //randomize pickups to ensure no endless games

    }

}
