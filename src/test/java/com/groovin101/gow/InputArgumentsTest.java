package com.groovin101.gow;

import com.groovin101.gow.exception.IncorrectNumberOfArgumentsException;
import com.groovin101.gow.exception.InvalidNumberOfPlayersException;
import com.groovin101.gow.exception.InvalidNumberOfRanksException;
import com.groovin101.gow.exception.InvalidNumberOfSuitsException;
import org.junit.Test;

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

    //todo: fix ignored tests or remove them entirely
    //todo: followup on all todos


    //todo: invalid args reply with error message via exception
    //todo: parse args out of String array; handle for spaces, handle for quotes, handle for valid input

}
