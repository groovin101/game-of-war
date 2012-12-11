package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.NoCardsToPlayException;
import com.groovin101.gow.model.*;
import com.groovin101.gow.rules.HighestCardNoTieRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 */
public class War {

    private List<Player> players;
    private Dealer dealer;
    private DeckExtended deck;
    private GameTable gameTable;
    private Player winner;

    //todo: add a play method that takes a list of usernames so that we have a chance to throw our InvalidUsernameException, allowing it to bubble


    //todo: validate args
    public static void main(String[] args) {
        War game = new War();
//        game.play(4, 14, 5); //doesnt like these inputs for some reason...
                game.play(4, 13, 2);
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    public War() {
        dealer = new Dealer();
        gameTable = new GameTable();
    }

    public void play(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {

        startTheGame(numberOfSuits, numberOfRanks, numberOfPlayers);

        while (!gameOver()) {
            playARound();
        }

        announceWinner();
    }

    private void announceWinner() {
        System.out.println("*************************************/n");
        System.out.println(winner.getName() + " has won the game!");
        System.out.println("*************************************/n");
    }

    void startTheGame(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {
        winner = null;
        players = buildPlayerList(numberOfPlayers);
        deck = new DeckImpl(); //todo - instantiate deck properly using args from above
        deck.create(numberOfSuits, numberOfRanks);
        deck.shuffle();
        dealer.dealAllCards(deck, players);
    }

    protected void playARound() {


        playAHand(HandType.SINGLE_CARD_HAND);

        HighestCardNoTieRule highestCardNoTieRule = new HighestCardNoTieRule();
        highestCardNoTieRule.fireRule(gameTable, null);

        if (gameTable.getWinner() == null) {
            playAHand(HandType.WAR_STYLE_HAND);
            highestCardNoTieRule.fireRule(gameTable, null);
        }
        logRound(gameTable.getWinner());
//        if (gameTable.shouldGoToWar()) {
//
//        }
//        while (shouldGotoWar()) {
//            playAHand(HandType.WAR_STYLE_HAND);
//            logRound(null);
//        }
//
//        Player winnerOfRound = determineWinnerOfRound();
//
//logRound(winnerOfRound);

divyWonCardsToWinner(gameTable.getWinner());
    }

    private void logRound(Player winnerOfTheRound) {
        System.out.println("---------------------------------------");
        for (PlayerPile pileOnTable : gameTable.getAllPilesOnTheTable()) {
            System.out.println(pileOnTable + " ; cards left: " + pileOnTable.getPlayer().getPlayerDeckSize());
        }
//System.out.println(winnerOfTheRound.getName() + " wins the round");
        System.out.println("---------------------------------------\n");
    }

    protected void divyWonCardsToWinner(Player winner) {
        List<PlayerPile> allPilesFromTable = gameTable.getAllPilesOnTheTable();
        for (PlayerPile pileFromTable : allPilesFromTable) {
            pileFromTable.shuffle();
            for (Card card : pileFromTable.getCards()) {
                winner.addToBottomOfPlayerDeck(card);
            }
        }
        gameTable.clearAllPilesFromTheTable();
    }

    protected PlayerPile identifyWinningPile(List<PlayerPile> piles) {
        Collections.sort(piles);
        return piles.get(piles.size()-1);
    }

    protected Player determineWinnerOfRound() {
        return identifyWinningPile(gameTable.getAllPilesOnTheTable()).getPlayer();
    }

    protected void playCardsFromAllPlayers(int howMany) {
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            try {
                Player player = it.next();
                gameTable.playAHand(player, player.playCards(howMany));
            }
            catch (NoCardsToPlayException e) {
                System.out.println(e.getMessage());
                it.remove();
            }
        }
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

    boolean doesOnePlayerHaveAllTheCards(DeckExtended deck, List<Player> players) {
        for (Player player : players) {
            if (player.getPlayerDeckSize() == deck.getTotalCardCount()) {
                winner = player;
                return true;
            }
        }
        return false;
    }

    boolean gameOver() {
        return doesOnePlayerHaveAllTheCards(deck, players);
    }

    boolean shouldGotoWar() {
        //return gameTable.areThereTiesPresent(gameTable.getPilesFromMostRecentPlay());
        return true;
    }

    public enum HandType {
        SINGLE_CARD_HAND,
        WAR_STYLE_HAND
    }
}