package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 */
public class PlayerTest extends BaseTest {

    private Player player;
    private Card cardForTestA;

    @Before
    public void setup() {
        super.setup();
        try {
            player = new Player("Chewbacca");
        }
        catch (InvalidUsernameException e) {
        }
        cardForTestA = new Card(Rank.ACE, Suit.CLUB);
    }

    @Test
    public void testInstantiation_playerMustNotHaveANullName() {
        try {
            new Player(null);
            fail("Should have told us that this player needs a name");
        }
        catch (InvalidUsernameException e) {
        }
    }

    @Test
    public void testInstantiation_playerMustNotHaveAnEmptyStringForAName() {
        try {
            new Player("       ");
            fail("Should have told us that this player needs a name");
        }
        catch (InvalidUsernameException e) {
        }
    }

    @Test
    public void testGetPlayerDeckSize_newPlayerDoesNotHaveADeck() {
        assertEquals(0, player.getPlayerDeckSize());
    }

    @Test
    public void testAddToBottomOfPlayerDeck_incrementsDeckSize() {
        player.addToBottomOfPlayerDeck(cardForTestA);
        assertEquals(1, player.getPlayerDeckSize());
    }

    @Test
    public void testAddToTopOfPlayerDeck_incrementsDeckSize() {
        player.dealToTopOfPlayersDeck(cardForTestA);
        assertEquals(1, player.getPlayerDeckSize());
    }

    @Test
    public void testBattle_playerWithSingleCardAddsOneCardToCurrentHand() {

        List<Card> expectedCurrentHand = new ArrayList<Card>();
        expectedCurrentHand.add(JACK_OF_DIAMONDS);

        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);

        JABBA.battle();
        assertEquals("Should have added the one card to the current hand", expectedCurrentHand, JABBA.getCurrentHand());
    }

    @Test
    public void testBattle_subsequentBattleResetsCurrentHand() {

        List<Card> expectedCurrentHand = new ArrayList<Card>();
        expectedCurrentHand.add(JACK_OF_DIAMONDS);

        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS); //Will be on bottom of our 2 card deck
        JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);

        JABBA.battle();
        JABBA.battle();
        assertEquals("Should have added the next top card to the current hand, making sure previously played card was removed",
                expectedCurrentHand, JABBA.getCurrentHand());
    }

    @Test
    public void testBattle_singleBattleAddsTheCardThatWasPlayedToTheListOfCardsPlayedThisRound() {

        List<Card> cardsThatShouldHaveBeenPlayedThisRound = new ArrayList<Card>();
        cardsThatShouldHaveBeenPlayedThisRound.add(JACK_OF_DIAMONDS);

        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);

        JABBA.battle();
        assertEquals("Should have added the one card to the list of cards played this round", cardsThatShouldHaveBeenPlayedThisRound, JABBA.getCardsPlayedThisRound());
    }

    @Test
    public void testWar_playerWithOneCardsAddsOneCardToCurrentHand() {

        List<Card> expectedCurrentHand = new ArrayList<Card>();
        expectedCurrentHand.add(ACE_OF_CLUBS);

        JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);

        JABBA.war();
        assertEquals("Should have added the single card played to the current hand", expectedCurrentHand, JABBA.getCurrentHand());
    }

    @Test
    public void testWar_playerWithFourCardsAddsThreeCardsToCurrentHand() {

        List<Card> expectedCurrentHand = new ArrayList<Card>();
        expectedCurrentHand.addAll(buildCardList(new Card[]{ACE_OF_CLUBS, KING_OF_SPADES, QUEEN_OF_HEARTS}));

        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
        JABBA.dealToTopOfPlayersDeck(QUEEN_OF_HEARTS);
        JABBA.dealToTopOfPlayersDeck(KING_OF_SPADES);
        JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);

        JABBA.war();

        assertEquals("Should have added the 3 top cards to the current hand", expectedCurrentHand, JABBA.getCurrentHand());
    }

    @Test
    public void testWar_subsequentWarResetsCurrentHand() {

        List<Card> expectedCurrentHand = new ArrayList<Card>();
        expectedCurrentHand.add(JACK_OF_DIAMONDS);

        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
        JABBA.dealToTopOfPlayersDeck(QUEEN_OF_HEARTS);
        JABBA.dealToTopOfPlayersDeck(KING_OF_SPADES);
        JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);

        JABBA.war();
        JABBA.war();
        assertEquals("Should have added the next top card to the current hand, making sure previously played cards were removed",
                expectedCurrentHand, JABBA.getCurrentHand());
    }

    @Test
    public void testWar_singleWarAddsAllCardsToTheCardsPlayedThisRound() {
        List<Card> cardsThatShouldHaveBeenPlayedThisRound = new ArrayList<Card>();
        cardsThatShouldHaveBeenPlayedThisRound.add(ACE_OF_CLUBS);

        JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);

        JABBA.war();
        assertEquals("Should have added the single card played to list of cards played",
                cardsThatShouldHaveBeenPlayedThisRound, JABBA.getCardsPlayedThisRound());
    }

   @Test
    public void testWar_multipleWarsAddsAllCardsToTheCardsPlayedThisRound() {

       List<Card> cardsThatShouldHaveBeenPlayedThisRound = new ArrayList<Card>();
       cardsThatShouldHaveBeenPlayedThisRound.addAll(buildCardList(new Card[]{JACK_OF_DIAMONDS,QUEEN_OF_HEARTS,KING_OF_SPADES,ACE_OF_CLUBS,ACE_OF_SPADES}));

       JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
       JABBA.dealToTopOfPlayersDeck(QUEEN_OF_HEARTS);
       JABBA.dealToTopOfPlayersDeck(KING_OF_SPADES);
       JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);
       JABBA.dealToTopOfPlayersDeck(ACE_OF_SPADES);

       JABBA.war();
       JABBA.war();
       assertTrue("Should have added all of the cards that we played from both wars",
               JABBA.getCardsPlayedThisRound().containsAll(cardsThatShouldHaveBeenPlayedThisRound));
    }

    @Test
    public void testBattleAndWar_cardsFromBothBattleAndWarAreAddedToTheListOfCardsPlayedThisRound() {

        List<Card> cardsThatShouldHaveBeenPlayedThisRound = new ArrayList<Card>();
        cardsThatShouldHaveBeenPlayedThisRound.addAll(buildCardList(new Card[]{JACK_OF_DIAMONDS, QUEEN_OF_HEARTS, KING_OF_SPADES, ACE_OF_CLUBS}));

        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
        JABBA.dealToTopOfPlayersDeck(QUEEN_OF_HEARTS);
        JABBA.dealToTopOfPlayersDeck(KING_OF_SPADES);
        JABBA.dealToTopOfPlayersDeck(ACE_OF_CLUBS);

        JABBA.battle();
        JABBA.war();
        assertTrue("Should have added all cards to the list of cards played this round but got " + JABBA.getCardsPlayedThisRound(),
                JABBA.getCardsPlayedThisRound().containsAll(cardsThatShouldHaveBeenPlayedThisRound));
    }

    @Test
    public void testWar_addsNoCardsToTheCurrentHand() {
        JABBA.war();
        assertEquals(0, JABBA.getCurrentHand().size());
    }

    @Test
    public void testClearCardsPlayedDuringThisRound() {
        JABBA.setCardsPlayedThisRound(buildCardList(new Card[]{ACE_OF_SPADES}));
        assertEquals(1, JABBA.getCardsPlayedThisRound().size());
        JABBA.clearCardsFromPreviousRound();
        assertEquals(0, JABBA.getCardsPlayedThisRound().size());
    }

    @Test
    public void testGetSignificantCard_returnsTheLastCardPlayed() {
        JABBA.setCurrentHand(buildCardList(new Card[]{ACE_OF_CLUBS,JACK_OF_DIAMONDS,QUEEN_OF_HEARTS}));
        assertEquals(QUEEN_OF_HEARTS, JABBA.getSignificantCard());
    }

    //todo: add tests to verify getSignificantCard in a two card war
    //todo: getSignificantCard with zero cards played
}