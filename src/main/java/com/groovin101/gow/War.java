package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.NoCardsToPlayException;
import com.groovin101.gow.exception.WarInitializationException;
import com.groovin101.gow.model.*;
import com.groovin101.gow.rules.HighestCardNoTieRule;

import java.util.*;

/**
 */
public class War {

    private Dealer dealer;
    private DeckExtended deck;
    private GameTable gameTable;
    private Player winnerOfTheGame;
    private int numberOfRoundsPlayed = 0;
    private Set<Player> players;
    private Player winnerOfTheLastRound;


    public Player getWinnerOfTheLastRound() {
        return winnerOfTheLastRound;
    }
    public void setWinnerOfTheLastRound(Player winnerOfTheLastRound) {
        this.winnerOfTheLastRound = winnerOfTheLastRound;
    }


    //todo: add a play method that takes a list of usernames so that we have a chance to throw our InvalidUsernameException, allowing it to bubble

    public static void main(String[] args) throws WarInitializationException {

        try {
            War game = new War();
            InputArguments arguments = new InputArguments(args);
            System.out.println(arguments.buildGameIsStartingMessage());
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

    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    public War() {
        dealer = new Dealer();
        gameTable = new GameTable();
        players = new HashSet<Player>();
        winnerOfTheLastRound = null;
    }

    public void play(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {

        startTheGame(numberOfSuits, numberOfRanks, numberOfPlayers);

        while (!gameOver()) {
            playARound();
            removePlayersWithNoCards();
        }

        announceWinnerOfGame();
    }

    private void announceWinnerOfGame() {
        System.out.println("*************************************/n");
        System.out.println(winnerOfTheGame.getName() + " has won the game!");
        System.out.println("*************************************/n");
    }

    void startTheGame(int numberOfSuits, int numberOfRanks, int numberOfPlayers) {
        winnerOfTheGame = null;
        setPlayers(buildPlayerList(numberOfPlayers));
        deck = new DeckImpl(); //todo - instantiate deck properly using args from above
        deck.create(numberOfSuits, numberOfRanks);
        deck.shuffle();
        dealer.dealAllCards(deck, getPlayers());
    }

    protected void playARound() {

        //doBattle();
        //while (shouldHaveAWar) {
        //  doWar();
        //}
        //divy cards to winner

        numberOfRoundsPlayed++;
        startABattle();
        setWinnerOfTheLastRound(new HighestCardNoTieRule().fireRule(gameTable));
        while (shouldStartAWar(gameTable)) {
            if (getWinnerOfTheLastRound() == null) {
                startAWar();
                new HighestCardNoTieRule().fireRule(gameTable);
            }
        }
        logRound(getWinnerOfTheLastRound());
        divyWonCardsToWinner(getWinnerOfTheLastRound());
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
        for (Player player : getPlayers()) {
            if (player.getPlayerDeckSize() > 0) {
                try {
                    player.playCards(howMany, gameTable);
                }
                catch (NoCardsToPlayException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    protected Collection<Player> buildPlayerList(int numberOfPlayers) {
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

    boolean doesOnePlayerHaveAllTheCards(DeckExtended deck, Collection<Player> players) {
        for (Player player : players) {
            if (player.getPlayerDeckSize() == deck.getTotalCardCount()) {
                winnerOfTheGame = player;
                return true;
            }
        }
        return false;
    }

    boolean gameOver() {
        return doesOnePlayerHaveAllTheCards(deck, getPlayers());
    }

    boolean shouldStartAWar(GameTable gameTable) {
        return getWinnerOfTheLastRound() == null;
    }

    public Set<Player> getPlayers() {
        return players;
    }
    public void setPlayers(Collection<Player> players) {
        for (Player player : players) {
            this.players.add(player);
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public Player getPlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public void removePlayersWithNoCards() {

        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            if (it.next().getPlayerDeckSize() == 0) {
                it.remove();
            }
        }
    }

}