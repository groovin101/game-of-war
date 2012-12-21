package com.groovin101.gow;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.WarInitializationException;
import com.groovin101.gow.model.*;

import java.util.*;

/**
 */
public class War {

    private Dealer dealer;
    private DeckExtended deck;
    private int numberOfRoundsPlayed = 0;
    private Set<Player> players;
    private Player winnerOfTheLastHandPlayed;
    private Player winnerOfTheGame;
    private boolean gameIsADraw;

    public Player getWinnerOfTheLastHandPlayed() {
        return winnerOfTheLastHandPlayed;
    }
    public void setWinnerOfTheLastHandPlayed(Player winnerOfTheLastHandPlayed) {
        this.winnerOfTheLastHandPlayed = winnerOfTheLastHandPlayed;
    }
    boolean isWarCalledFor() {
//        return winnerOfTheLastHandPlayed == null;
                                                    //todo: make a test for the draw scenario with no winner of war
        return winnerOfTheLastHandPlayed == null && atLeastOnePlayerHasCardsLeftToPlay();
    }
    void setGameIsADraw(boolean gameIsADraw) {
        this.gameIsADraw = gameIsADraw;
    }
    boolean atLeastOnePlayerHasCardsLeftToPlay() {
        for (Player player : getPlayers()) {
            if (player.getPlayerDeckSize() > 0) {
                return true;
            }
        }
        return false;
    }

    public Set<Player> getPlayers() {
        return players;
    }
    public void setPlayers(Collection<Player> players) {
        for (Player player : players) {
            this.players.add(player);
        }
    }

    public War() {
        dealer = new Dealer();
        players = new HashSet<Player>();
        winnerOfTheLastHandPlayed = null;
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
        System.out.println(gameIsADraw ? "Game is a draw. No winner." : winnerOfTheGame.getName() + " has won the game!");
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

        numberOfRoundsPlayed++;

        doBattle();
        while (isWarCalledFor()) {
            doWar();
        }

        if (getWinnerOfTheLastHandPlayed() == null) {
            gameIsADraw = true;
        }

        divySpoilsToWinner(getWinnerOfTheLastHandPlayed());

        logRound(getWinnerOfTheLastHandPlayed());
        clearCardsFromPreviousRound();
    }

    void clearCardsFromPreviousRound() {
        for (Player player : getPlayers()) {
            player.clearCardsFromPreviousRound();
        }
    }

    public void doBattle() {

        playBattleHands();
        evaluateHand();
    }

    public void doWar() {

        playWarHands();
        evaluateHand();
    }

    private void playBattleHands() {

        for (Player player : getPlayers()) {
            player.battle();
        }
    }

    private void playWarHands() {

        for (Player player : getPlayers()) {
            player.war();
        }
    }

    private void logRound(Player winnerOfTheRound) {

        System.out.println("---------------------------------------");
        for (Player player : getPlayers()) {
            System.out.println(player.getName() + " played " + toStringForListOfCards(player.getCardsPlayedThisRound()) + "; cards left: " + player.getPlayerDeckSize());
        }
        System.out.println((winnerOfTheRound == null ? "Nobody" : winnerOfTheRound.getName()) + " wins round [" + numberOfRoundsPlayed + "]");
        System.out.println("---------------------------------------\n");
    }

    private String toStringForListOfCards(List<Card> cards) {
        StringBuilder toStringForListOfCards = new StringBuilder();
        for (Card card : cards) {
            toStringForListOfCards.append(card.toString()).append(",");
        }
        toStringForListOfCards = toStringForListOfCards.deleteCharAt(toStringForListOfCards.length()-1);
        return toStringForListOfCards.toString();
    }

    List<Card> fetchAllCardsPlayedThisRound() {

        List<Card> cardsPlayedThisRound = new ArrayList<Card>();
        for (Player player : getPlayers()) {
            cardsPlayedThisRound.addAll(player.getCardsPlayedThisRound());
        }
        return cardsPlayedThisRound;
    }

    protected void divySpoilsToWinner(Player winner) {
        if (winner != null) {
            List<Card> allCardsPlayedThisRound = fetchAllCardsPlayedThisRound();
            Collections.shuffle(allCardsPlayedThisRound, new Random(Calendar.getInstance().getTimeInMillis()));
            for (Card card : allCardsPlayedThisRound) {
                winner.addToBottomOfPlayerDeck(card);
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
        return doesOnePlayerHaveAllTheCards(deck, getPlayers()) || gameIsADraw;
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

    void evaluateHand() {

        Player ownerOfHighestCard = null;
        Card highestCardPlayedDuringHand = null;

        for (Player player : getPlayers()) {
            if (player.getSignificantCard() != null) {
                if (player.getSignificantCard().compareTo(highestCardPlayedDuringHand) > 0) {
                    ownerOfHighestCard = player;
                    highestCardPlayedDuringHand = player.getSignificantCard();
                }
                else if (player.getSignificantCard().compareTo(highestCardPlayedDuringHand) == 0) {
    //                setWarCalledFor(true);
                    ownerOfHighestCard = null;
                }
            }
        }
        setWinnerOfTheLastHandPlayed(ownerOfHighestCard);
    }

}