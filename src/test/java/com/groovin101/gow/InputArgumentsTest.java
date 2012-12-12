package com.groovin101.gow;

import com.groovin101.gow.exception.IncorrectNumberOfArgumentsException;
import com.groovin101.gow.exception.InvalidNumberOfPlayersException;
import com.groovin101.gow.exception.InvalidNumberOfRanksException;
import com.groovin101.gow.exception.InvalidNumberOfSuitsException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.*;

/**
 */
public class InputArgumentsTest {


    @Test
    public void test_noArgumentsPopulatesArgumentsWithDefaultParams() throws Exception {
        InputArguments args = new InputArguments(new String[]{});
        assertEquals(2, args.getNumberOfPlayers());
        assertEquals(4, args.getNumberOfSuits());
        assertEquals(13, args.getNumberOfRanks());
    }

    @Test
    public void test_argumentsCorrespondWithParams() throws Exception {
        InputArguments args = new InputArguments(new String[]{"2", "1", "3"});
        assertEquals(2, args.getNumberOfPlayers());
        assertEquals(1, args.getNumberOfSuits());
        assertEquals(3, args.getNumberOfRanks());
    }

    @Test
    public void test_argumentsWithSpacesAreTolerated() throws Exception {
        InputArguments args = new InputArguments(new String[]{" 2 ", " 1 ", " 3 "});
        assertEquals(2, args.getNumberOfPlayers());
        assertEquals(1, args.getNumberOfSuits());
        assertEquals(3, args.getNumberOfRanks());
    }

    @Test
    public void test_argumentsWithQuotesAroundThemAreTolerated() throws Exception {
        InputArguments args = new InputArguments(new String[]{"\"2\"", "\"1\"", "\"3\""});
        assertEquals(2, args.getNumberOfPlayers());
        assertEquals(1, args.getNumberOfSuits());
        assertEquals(3, args.getNumberOfRanks());
    }

    //todo: this would be a nice to have
    @Ignore
    @Test
    public void test_argumentsSeperatedByCommasAreTolerated() throws Exception {
        InputArguments args = new InputArguments(new String[]{"2,1,3"});
        assertEquals(2, args.getNumberOfPlayers());
        assertEquals(1, args.getNumberOfSuits());
        assertEquals(3, args.getNumberOfRanks());
    }

    @Test
    public void test_notCorrectNumberOfParamsWhenOnlyTwoAreProvided() throws Exception {

        try {
            InputArguments args = new InputArguments(new String[]{"5", "1"});
            fail("Should have thrown exception since not enough arguments were provided");
        }
        catch (IncorrectNumberOfArgumentsException e) {
        }
    }

    @Test
    public void test_notCorrectNumberOfParamsWhenTooManyAreProvided() throws Exception {

        try {
            InputArguments args = new InputArguments(new String[]{"5", "1", "1", "1"});
            fail("Should have thrown exception since too many arguments were provided");
        }
        catch (IncorrectNumberOfArgumentsException e) {
        }
    }

    @Test
    public void testTurnExceptionReportingOn() throws Exception {
        InputArguments args = new InputArguments(new String[]{"-e", "2", "2", "2"});
        args.turnExceptionReportingOnIfNeeded(new String[]{"-e", "2", "2", "2"});
        assertTrue("We provied a -e so exception reporting should turn on", args.isExceptionReportingTurnedOn());
    }

    @Test
    public void testTurnExceptionReportingOn_doesNotTurnOnWhenNoDashEIsPresent() throws Exception {
        InputArguments args = new InputArguments(new String[]{"2", "2", "2"});
        args.turnExceptionReportingOnIfNeeded(new String[]{"2", "2", "2"});
        assertFalse("Should not have turned exception reporting on since we did not supply a -e", args.isExceptionReportingTurnedOn());
    }

    @Test
    public void testRemoveDashEArgument() throws Exception {

        String[] argsToModify = new String[]{"-e", "2", "2", "2"};
        InputArguments args = new InputArguments(argsToModify);

        String[] modifiedArguments = args.removeDashEArgument(argsToModify);

        List<String> modifiedArgumentsAsList = new ArrayList<String>();
        Collections.addAll(modifiedArgumentsAsList, modifiedArguments);
        assertFalse("Should have removed the -e", modifiedArgumentsAsList.contains("-e"));
    }

    @Test
    public void test_dashEArgDoesNotCountTowardsArgCount() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"-e", "2", "2", "2"});
        }
        catch (IncorrectNumberOfArgumentsException e) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    public void test_playerArgIsOfWrongType() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"x", "1", "1"});
            fail("Args should all be ints - need an exception here");
        }
        catch (InvalidNumberOfPlayersException e) {}
    }

    @Test
    public void test_suitsArgIsOfWrongType() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"1", "x", "1"});
            fail("Args should all be ints - need an exception here");
        }
        catch (InvalidNumberOfSuitsException e) {}
    }

    @Test
    public void test_ranksArgIsOfWrongType() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"1", "1", "x"});
            fail("Args should all be ints - need an exception here");
        }
        catch (InvalidNumberOfRanksException e) {}
    }

    @Test
    public void test_mustSpecifyAtLeastOneSuit() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"1", "0", "13"});
            fail("Not enough suits were specified - need an exception here");
        }
        catch (InvalidNumberOfSuitsException e) {}
    }

    @Test
    public void test_cannotSpecifyMoreThanFourSuits() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"1", "5", "13"});
            fail("Cannot specify more than 4 suits - need an exception here");
        }
        catch (InvalidNumberOfSuitsException e) {}
    }

    @Test
    public void test_mustSpecifyAtLeastOneRank() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"1", "1", "0"});
            fail("Not enough ranks were specified - need an exception here");
        }
        catch (InvalidNumberOfRanksException e) {}
    }

    @Test
    public void test_cannotSpecifyMoreThanThirteenRanks() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"2", "1", "15"});
            fail("Too many ranks were specified - need an exception here");
        }
        catch (InvalidNumberOfRanksException e) {}
    }

    @Test
    public void test_numberOfPlayersExceedsNumberOfCards() throws Exception {
        InputArguments args = new InputArguments(new String[]{});
        assertFalse("No players present so should not exceed", args.numberOfPlayersExceedsNumberOfCards(0, 4, 13));
    }

    @Test
    public void test_numberOfPlayersExceedsNumberOfCardsYes() throws Exception {
        InputArguments args = new InputArguments(new String[]{});
        assertTrue("Too many players so should have exceeded number of cards", args.numberOfPlayersExceedsNumberOfCards(53, 4, 13));
    }

    @Test
    public void test_mustHaveAtLeastTwoPlayers() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"0", "1", "13"});
            fail("Not enough players were specified - need an exception here");
        }
        catch (InvalidNumberOfPlayersException e) {}
    }

    @Test
    public void test_cantHaveMorePlayersThanNumberOfCards() throws Exception {
        try {
            InputArguments args = new InputArguments(new String[]{"14", "1", "13"});
            fail("Can't have more players than number of cards - need an exception here");
        }
        catch (InvalidNumberOfPlayersException e) {}
    }

@Ignore
    @Test
    public void test_dashEArgTurnsOnExceptions() throws Exception {
        InputArguments args = new InputArguments(new String[]{"-e", "2", "2", "2"});
        fail();
    }
    //todo: fix ignored tests or remove them entirely
    //todo: followup on all todos
}
