package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.exception.NoCardsToPlayException;
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
    private Card cardForTestB;
    private GameTable gameTable;

    @Before
    public void setup() {
        super.setup();
        try {
            player = new Player("Chewbacca");
        }
        catch (InvalidUsernameException e) {
        }
        cardForTestA = new Card(Rank.ACE, Suit.CLUB);
        cardForTestB = new Card(Rank.EIGHT, Suit.SPADE);
        gameTable = new GameTable();
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
    public void testAddToTopOfPlayerDeck_orderIsCorrect() throws Exception {
        player.dealToTopOfPlayersDeck(cardForTestB);
        player.dealToTopOfPlayersDeck(cardForTestA);
        assertEquals(cardForTestA, player.playACard(gameTable));
    }

    @Test
    public void testAddToBottomOfPlayerDeck_orderIsCorrect() throws Exception {
        player.addToBottomOfPlayerDeck(cardForTestB);
        player.addToBottomOfPlayerDeck(cardForTestA);
        assertEquals(cardForTestB, player.playACard(gameTable));
    }

    @Test
    public void testPlayACard_singleCard() throws Exception {
        player.dealToTopOfPlayersDeck(cardForTestA);
        assertEquals(cardForTestA, player.playACard(gameTable));
    }

    @Test
    public void testPlayACard_multipleCards() throws Exception {
        player.dealToTopOfPlayersDeck(cardForTestA);
        player.dealToTopOfPlayersDeck(cardForTestB);
        List<Card> cardsThatWereExpectedToBePlayed = new ArrayList<Card>();
        cardsThatWereExpectedToBePlayed.add(cardForTestA);
        cardsThatWereExpectedToBePlayed.add(cardForTestB);
        assertTrue("Should contain all two of our cards since we played both", player.playCards(2, gameTable).containsAll(cardsThatWereExpectedToBePlayed));
    }

    @Test
    public void testPlayACard_requestingMoreCardsThanWeHaveReturnsWhatWeDoHave() throws Exception {
        player.dealToTopOfPlayersDeck(cardForTestA);
        List<Card> cardsThatWereExpectedToBePlayed = new ArrayList<Card>();
        cardsThatWereExpectedToBePlayed.add(cardForTestA);
        assertTrue("Should contain a single card even though we specified to deal 2", player.playCards(2, gameTable).containsAll(cardsThatWereExpectedToBePlayed));
    }

    @Test
    public void testPlayACard_emptyHandThrowsANoCardsException() {
        try {
            player.playCards(1, gameTable);
            fail("Should have thrown an exception to indicate that we can't play a single card");
        }
        catch (NoCardsToPlayException e) {
        }
    }

    @Test
    public void testPlayACard_returnsTheOnlyCardInOurDeck() {
        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
        assertEquals("Should return the only card that was in Jabba's deck", JACK_OF_DIAMONDS, JABBA.throwNextCard());
    }

    @Test
    public void testThrowNextCard_addsToTheThrowdownPile() {
        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
        JABBA.throwNextCard();
        assertEquals("Should now be one card in the throwdown pile", 1, JABBA.getThrowdownCards().size());
    }

    @Test
    public void testThrowNextCard_marksTheSingleCardThrownAsTheSignificantCard() {
        JABBA.dealToTopOfPlayersDeck(JACK_OF_DIAMONDS);
        JABBA.throwNextCard();
        assertEquals("Should now be one card in the throwdown pile", 1, JABBA.getThrowdownCards().size());
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
        cardsThatShouldHaveBeenPlayedThisRound.addAll(buildCardList(new Card[]{JACK_OF_DIAMONDS,QUEEN_OF_HEARTS,KING_OF_SPADES,ACE_OF_CLUBS}));

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

}