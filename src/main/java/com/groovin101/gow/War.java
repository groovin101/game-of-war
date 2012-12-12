package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.NoCardsToPlayException;
import com.groovin101.gow.exception.WarInitializationException;
import com.groovin101.gow.model.*;
import com.groovin101.gow.rules.HighestCardNoTieRule;

import java.net.InetAddress;
import java.util.*;

/**
 */
public class War {

    private List<Player> players;
    private Dealer dealer;
    private DeckExtended deck;
    private GameTable gameTable;
    private Player winnerOfTheGame;
    private int numberOfRoundsPlayed = 0;

    //todo: add a play method that takes a list of usernames so that we have a chance to throw our InvalidUsernameException, allowing it to bubble

    public static void main(String[] args) throws WarInitializationException {

        try {
            War game = new War();
            InputArguments arguments = new InputArguments(args);
            game.play(arguments.getNumberOfSuits(), arguments.getNumberOfRanks(), arguments.getNumberOfPlayers());
        }
        catch (WarInitializationException e) {
            if (InputArguments.isTheExceptionReportingFlagPresent(args)) {
                System.out.println(InputArguments.buildErrorMessage(false));
                throw e;
            }
            else {
                System.out.println(InputArguments.buildErrorMessage(true));
            }
        }

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
        System.out.println(winnerOfTheGame.getName() + " has won the game!");
        System.out.println("*************************************/n");
    }

    void startTheGame(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {
        winnerOfTheGame = null;
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
        while (shouldStartAWar(gameTable)) {
            if (gameTable.getWinnerOfTheLastRound() == null) {
                startAWar();
                new HighestCardNoTieRule().fireRule(gameTable, null);
            }
        }
        logRound(gameTable.getWinnerOfTheLastRound());
        divyWonCardsToWinner(gameTable.getWinnerOfTheLastRound());
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
System.out.println(winner == null ? "winner was null" : winner.getName());
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
                e.printStackTrace();
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
                winnerOfTheGame = player;
                return true;
            }
        }
        return false;
    }

    boolean gameOver() {
        return doesOnePlayerHaveAllTheCards(deck, players);
    }

    boolean shouldStartAWar(GameTable gameTable) {
        return gameTable.getWinnerOfTheLastRound() == null;
    }
}