package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.NoCardsToPlayException;
import com.groovin101.gow.model.*;
import com.groovin101.gow.rules.HighestCardNoTieRule;

import java.util.*;

/**
 */
public class War {

    private List<Player> players;
    private Dealer dealer;
    private DeckExtended deck;
    private GameTable gameTable;
    private Player winner;
    private int numberOfRoundsPlayed = 0;

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
//todo: shuffle
deck.shuffle();
        dealer.dealAllCards(deck, players);
    }

    protected void playARound() {

        numberOfRoundsPlayed++;
        startABattle();
        new HighestCardNoTieRule().fireRule(gameTable, null);
        if (gameTable.getWinnerOfRound() == null) {
            startAWar();
            new HighestCardNoTieRule().fireRule(gameTable, null);
        }
        logRound(gameTable.getWinnerOfRound());
        divyWonCardsToWinner(gameTable.getWinnerOfRound());
    }

    public void startABattle() {
        playCardsFromAllPlayers(1, gameTable);
    }

    public void startAWar() {
        playCardsFromAllPlayers(4, gameTable);
    }

    private void logRound(Player winnerOfTheRound) {
        System.out.println("---------------------------------------");
        for (PlayerPile pileOnTable : gameTable.getAllPilesOnTheTable()) {
            System.out.println(pileOnTable + " ; cards left: " + pileOnTable.getPlayer().getPlayerDeckSize());
        }
        System.out.println((winnerOfTheRound == null ? "Nobody" : winnerOfTheRound.getName()) + " wins round [" + numberOfRoundsPlayed + "]");
        System.out.println("---------------------------------------\n");
    }

    protected void divyWonCardsToWinner(Player winner) {
        List<Card> cardsFromBothPiles = new ArrayList<Card>();
        List<PlayerPile> allPilesFromTable = gameTable.getAllPilesOnTheTable();
        for (PlayerPile pileFromTable : allPilesFromTable) {
            for (Card card : pileFromTable.getCards()) {
                cardsFromBothPiles.add(card);
            }
        }
        Collections.shuffle(cardsFromBothPiles, new Random(Calendar.getInstance().getTimeInMillis()));
        for (Card card : cardsFromBothPiles) {
            winner.addToBottomOfPlayerDeck(card);
        }
        gameTable.clearAllPilesFromTheTable();
    }

    //todo: delete this
    protected PlayerPile identifyWinningPile(List<PlayerPile> piles) {
        Collections.sort(piles);
        return piles.get(piles.size() - 1);
    }

    protected void playCardsFromAllPlayers(int howMany, GameTable gameTable) {
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            try {
                Player player = it.next();
                player.playCards(howMany, gameTable);
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
}