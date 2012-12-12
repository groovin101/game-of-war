package com.groovin101.gow;

import com.groovin101.gow.exception.*;
import com.groovin101.gow.model.DeckImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 */
public class InputArguments {

    private int numberOfPlayers = 0;
    private int numberOfSuits = 0;
    private int numberOfRanks = 0;

    /**
     * Args should be specified in the following order:
     *  1) number of players
     *  2) number of suits
     *  3) number of ranks
     *
     *  If no arguments are supplied, a default configuration for standard war is assumed using 2 players and a 52 card
     *  deck.
     *
     * @param args
     * @throws WarInitializationException
     */
    public InputArguments(String[] args) throws WarInitializationException {

        args = removeDashEArgument(args);

        if (args.length == 0) {
            assignDefaultValues();
        }
        else if (args.length != 3) {
            throw new IncorrectNumberOfArgumentsException("Incorrect number of arguments provided: " + args.length);
        }
        else {
            parseValues(args);
            if (numberOfRanks < 1) {
                throw new InvalidNumberOfRanksException("Not enough ranks were specified");
            }
            if (numberOfRanks > 13) {
                throw new InvalidNumberOfRanksException("Too many ranks were specified");
            }
            if (numberOfSuits < 1) {
                throw new InvalidNumberOfSuitsException("Not enough suits were specified");
            }
            if (numberOfSuits > 4) {
                throw new InvalidNumberOfSuitsException("Too many suits were specified");
            }
            if (numberOfPlayersExceedsNumberOfCards(numberOfPlayers, numberOfSuits, numberOfRanks)) {
                throw new InvalidNumberOfPlayersException("Number of players should not exceed number of cards");
            }
            if (numberOfPlayers < 2) {
                throw new InvalidNumberOfPlayersException("Not enough players were specified");
            }
        }
    }

    private static List<String> argsAsList(String[] args) {
        List<String> argsAsList = new ArrayList<String>();
        Collections.addAll(argsAsList, args);
        return argsAsList;
    }

    public static boolean isTheExceptionReportingFlagPresent(String[] args) {
        return argsAsList(args).contains("-e");
    }

    String[] removeDashEArgument(String[] argsToModify) {

        List<String> argsToModifyAsList = new ArrayList<String>();
        Collections.addAll(argsToModifyAsList, argsToModify);

        Iterator<String> it = argsToModifyAsList.iterator();
        while (it.hasNext()) {
            String arg = it.next();
            if (arg.equals("-e")) {
                it.remove();
            }
        }
        return argsToModifyAsList.toArray(new String[argsToModifyAsList.size()]);
    }

    private void assignDefaultValues() {
        numberOfPlayers = 2;
        numberOfRanks = 13;
        numberOfSuits = 4;
    }

    void parseValues(String[] args) throws WarInitializationException {

        numberOfPlayers = parsePlayer(args[0]);
        numberOfSuits = parseSuit(args[1]);
        numberOfRanks = parseRank(args[2]);
    }

    private int parsePlayer(String arg) throws InvalidNumberOfPlayersException {
        try {
            return parseInt(arg);
        }
        catch (NumberFormatException e) {
            throw new InvalidNumberOfPlayersException("Player arg should be an int");
        }
    }

    private int parseSuit(String arg) throws InvalidNumberOfSuitsException {
        try {
            return parseInt(arg);
        }
        catch (NumberFormatException e) {
            throw new InvalidNumberOfSuitsException("Suit arg should be an int");
        }
    }

    private int parseRank(String arg) throws InvalidNumberOfRanksException {
        try {
            return parseInt(arg);
        }
        catch (NumberFormatException e) {
            throw new InvalidNumberOfRanksException("Rank arg should be an int");
        }
    }

    private int parseInt(String intAsString) {
        intAsString = intAsString.replaceAll("\"", "");
        return Integer.parseInt(intAsString.trim());
    }

    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    int getNumberOfRanks() {
        return numberOfRanks;
    }

    int getNumberOfSuits() {
        return numberOfSuits;
    }

    boolean numberOfPlayersExceedsNumberOfCards(int numberOfPlayers, int numberOfSuits, int numberOfRanks) {
        return numberOfPlayers > new DeckImpl(numberOfSuits, numberOfRanks).getTotalCardCount();
    }
}
