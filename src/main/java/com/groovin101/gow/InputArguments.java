package com.groovin101.gow;

import com.groovin101.gow.exception.IncorrectNumberOfArgumentsException;
import com.groovin101.gow.exception.InvalidNumberOfPlayersException;
import com.groovin101.gow.exception.InvalidNumberOfRanksException;
import com.groovin101.gow.exception.InvalidNumberOfSuitsException;
import com.groovin101.gow.exception.WarInitializationException;
import com.groovin101.gow.model.DeckImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 */
public class InputArguments {

    private static final int DEFAULT_NUMBER_OF_PLAYERS = 2;
    private static final int DEFAULT_NUMBER_OF_SUITS = 4;
    private static final int DEFAULT_NUMBER_OF_RANKS = 13;

    private int numberOfPlayers;
    private int numberOfSuits;
    private int numberOfRanks;
    private boolean helpRequested;
    private boolean exceptionReportingEnabled;

    private boolean playersExplicitlySet;
    private boolean suitsExplicitlySet;
    private boolean ranksExplicitlySet;

    public InputArguments(String[] args) throws WarInitializationException {

        assignDefaultValues();
        parseArguments(args == null ? new String[0] : args);
        if (!helpRequested) {
            validateConfiguration();
        }
    }

    private void parseArguments(String[] args) throws WarInitializationException {

        List<String> positionalArgs = new ArrayList<String>();

        for (int i = 0; i < args.length; i++) {
            String token = sanitize(args[i]);
            if (token == null) {
                continue;
            }

            if (isExceptionFlag(token)) {
                exceptionReportingEnabled = true;
                continue;
            }
            if (isHelpFlag(token)) {
                helpRequested = true;
                continue;
            }

            if (token.startsWith("--")) {
                i = handleLongOption(token, args, i);
                continue;
            }

            if (token.startsWith("-") && token.length() > 1) {
                i = handleShortOption(token, args, i);
                continue;
            }

            positionalArgs.add(token);
        }

        applyPositionalArguments(positionalArgs);
    }

    private int handleLongOption(String token, String[] args, int currentIndex) throws WarInitializationException {

        String[] parts = token.split("=", 2);
        String optionName = parts[0].toLowerCase(Locale.ENGLISH);
        String value = parts.length > 1 ? sanitize(parts[1]) : null;
        boolean consumedNextValue = false;

        if ("--players".equals(optionName)) {
            String playersValue = resolveOptionValue(optionName, value, args, currentIndex);
            consumedNextValue = value == null;
            setPlayers(playersValue);
        }
        else if ("--suits".equals(optionName)) {
            String suitsValue = resolveOptionValue(optionName, value, args, currentIndex);
            consumedNextValue = value == null;
            setSuits(suitsValue);
        }
        else if ("--ranks".equals(optionName)) {
            String ranksValue = resolveOptionValue(optionName, value, args, currentIndex);
            consumedNextValue = value == null;
            setRanks(ranksValue);
        }
        else if ("--exception-reporting".equals(optionName)) {
            exceptionReportingEnabled = true;
        }
        else if ("--help".equals(optionName) || "--usage".equals(optionName)) {
            helpRequested = true;
        }
        else {
            throw new IncorrectNumberOfArgumentsException("Unknown argument: " + token);
        }

        return consumedNextValue ? currentIndex + 1 : currentIndex;
    }

    private int handleShortOption(String token, String[] args, int currentIndex) throws WarInitializationException {

        String normalized = token.toLowerCase(Locale.ENGLISH);

        if ("-p".equals(normalized)) {
            setPlayers(requireNextValue("-p", args, currentIndex));
            return currentIndex + 1;
        }
        if (normalized.startsWith("-p") && normalized.length() > 2) {
            setPlayers(normalized.substring(2));
            return currentIndex;
        }

        if ("-s".equals(normalized)) {
            setSuits(requireNextValue("-s", args, currentIndex));
            return currentIndex + 1;
        }
        if (normalized.startsWith("-s") && normalized.length() > 2) {
            setSuits(normalized.substring(2));
            return currentIndex;
        }

        if ("-r".equals(normalized)) {
            setRanks(requireNextValue("-r", args, currentIndex));
            return currentIndex + 1;
        }
        if (normalized.startsWith("-r") && normalized.length() > 2) {
            setRanks(normalized.substring(2));
            return currentIndex;
        }

        if ("-usage".equals(normalized)) {
            helpRequested = true;
            return currentIndex;
        }

        throw new IncorrectNumberOfArgumentsException("Unknown argument: " + token);
    }

    private String resolveOptionValue(String optionName, String providedValue, String[] args, int currentIndex)
            throws IncorrectNumberOfArgumentsException {

        if (providedValue != null && providedValue.length() > 0) {
            return providedValue;
        }
        return requireNextValue(optionName, args, currentIndex);
    }

    private String requireNextValue(String optionName, String[] args, int currentIndex)
            throws IncorrectNumberOfArgumentsException {

        if (currentIndex + 1 >= args.length) {
            throw new IncorrectNumberOfArgumentsException("Missing value for option: " + optionName);
        }
        return sanitize(args[currentIndex + 1]);
    }

    private void applyPositionalArguments(List<String> positionalArgs) throws WarInitializationException {

        if (positionalArgs.isEmpty()) {
            return;
        }

        int index = 0;
        if (!playersExplicitlySet && index < positionalArgs.size()) {
            setPlayers(positionalArgs.get(index++));
        }
        if (!suitsExplicitlySet && index < positionalArgs.size()) {
            setSuits(positionalArgs.get(index++));
        }
        if (!ranksExplicitlySet && index < positionalArgs.size()) {
            setRanks(positionalArgs.get(index++));
        }

        if (index < positionalArgs.size()) {
            throw new IncorrectNumberOfArgumentsException("Too many positional arguments provided: " + positionalArgs.size());
        }
    }

    private void setPlayers(String value) throws InvalidNumberOfPlayersException {
        numberOfPlayers = parsePlayer(value);
        playersExplicitlySet = true;
    }

    private void setSuits(String value) throws InvalidNumberOfSuitsException {
        numberOfSuits = parseSuit(value);
        suitsExplicitlySet = true;
    }

    private void setRanks(String value) throws InvalidNumberOfRanksException {
        numberOfRanks = parseRank(value);
        ranksExplicitlySet = true;
    }

    private void validateConfiguration() throws WarInitializationException {

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

    private void assignDefaultValues() {
        numberOfPlayers = DEFAULT_NUMBER_OF_PLAYERS;
        numberOfSuits = DEFAULT_NUMBER_OF_SUITS;
        numberOfRanks = DEFAULT_NUMBER_OF_RANKS;
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

    private String sanitize(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public String buildGameIsStartingMessage() {
        StringBuilder startupMessage = new StringBuilder("\n\n");
        startupMessage.append(indentingSpaces()).append("*********************************************************************").append("\n");
        startupMessage.append(indentingSpaces()).append("Starting a game for ").append(getNumberOfPlayers()).append(" players").append("\n");
        startupMessage.append(indentingSpaces()).append(" Using a deck with:").append("\n");
        startupMessage.append(indentingSpaces()).append("  ").append(getNumberOfSuits()).append(" suits").append("\n");
        startupMessage.append(indentingSpaces()).append("  ").append(getNumberOfRanks()).append(" ranks").append("\n");
        startupMessage.append(indentingSpaces()).append("*********************************************************************").append("\n\n");
        return startupMessage.toString();
    }

    public static String buildErrorMessage(boolean includeMoreInformationMessage) {
        StringBuilder errorMessage = new StringBuilder("\n\n");
        errorMessage.append(indentingSpaces()).append("*********************************************************************").append("\n");
        errorMessage.append(indentingSpaces()).append("Problem with arguments.").append("\n");
        errorMessage.append(buildUsageMessage());
        errorMessage.append("\n");
        if (includeMoreInformationMessage) {
            errorMessage.append(indentingSpaces()).append("For more information, call with a -e or --exception-reporting flag").append("\n");
        }
        errorMessage.append(indentingSpaces()).append("*********************************************************************").append("\n");
        errorMessage.append("\n");
        return errorMessage.toString();
    }

    public static String buildUsageMessage() {
        StringBuilder usageMessage = new StringBuilder("");
        usageMessage.append(indentingSpaces()).append("usage: war [options]").append("\n");
        usageMessage.append(indentingSpaces()).append("  --players, -p <int>        Number of players (default 2)").append("\n");
        usageMessage.append(indentingSpaces()).append("  --suits, -s <int>          Number of suits (1-4, default 4)").append("\n");
        usageMessage.append(indentingSpaces()).append("  --ranks, -r <int>          Number of ranks (1-13, default 13)").append("\n");
        usageMessage.append(indentingSpaces()).append("  -e, --exception-reporting  Show detailed exceptions").append("\n");
        usageMessage.append(indentingSpaces()).append("  --help | --usage | -usage  Display this help message").append("\n");
        usageMessage.append(indentingSpaces()).append("Positional arguments (legacy): players suits ranks").append("\n");
        return usageMessage.toString();
    }

    private static String indentingSpaces() {
        return "   * ";
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

    boolean shouldShowUsage() {
        return helpRequested;
    }

    boolean isExceptionReportingEnabled() {
        return exceptionReportingEnabled;
    }

    boolean numberOfPlayersExceedsNumberOfCards(int numberOfPlayers, int numberOfSuits, int numberOfRanks) {
        return numberOfPlayers > new DeckImpl(numberOfSuits, numberOfRanks).getTotalCardCount();
    }

    public static boolean isTheExceptionReportingFlagPresent(String[] args) {
        if (args == null) {
            return false;
        }
        for (String arg : args) {
            if (isExceptionFlag(arg)) {
                return true;
            }
        }
        return false;
    }

    String[] removeDashEArgument(String[] argsToModify) {

        List<String> argsToModifyAsList = new ArrayList<String>();
        Collections.addAll(argsToModifyAsList, argsToModify);

        Iterator<String> it = argsToModifyAsList.iterator();
        while (it.hasNext()) {
            String arg = sanitize(it.next());
            if (isExceptionFlag(arg)) {
                it.remove();
            }
        }
        return argsToModifyAsList.toArray(new String[argsToModifyAsList.size()]);
    }

    private static boolean isExceptionFlag(String token) {
        if (token == null) {
            return false;
        }
        String normalized = token.toLowerCase(Locale.ENGLISH);
        return "-e".equals(normalized) || "--exception-reporting".equals(normalized);
    }

    private static boolean isHelpFlag(String token) {
        if (token == null) {
            return false;
        }
        String normalized = token.toLowerCase(Locale.ENGLISH);
        return "--help".equals(normalized) || "-help".equals(normalized) || "--usage".equals(normalized) || "-usage".equals(normalized) || "-h".equals(normalized);
    }
}
