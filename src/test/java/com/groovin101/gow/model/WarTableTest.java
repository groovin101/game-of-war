package com.groovin101.gow.model;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.test.utils.BaseTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 */
public class WarTableTest extends BaseTest {

    private WarTable warTable;
    private Player felix;
    private Player oscar;
    private Card ace;
    private Card king;
    private List<Card> expectedCardsFromOscarsHand;
    private List<Card> expectedCardsFromFelixsHand;

    @Before
    public void setup() {
        super.setup();
        warTable = new WarTable();
        try {
            oscar = new Player("oscar");
            felix = new Player("felix");
        }
        catch (InvalidUsernameException e) {}
        ace = new Card(Rank.ACE, Suit.CLUB);
        king = new Card(Rank.KING, Suit.HEART);
        expectedCardsFromOscarsHand = new ArrayList<Card>();
        expectedCardsFromFelixsHand = new ArrayList<Card>();
    }

    @Test
    public void testRetrieveCardsDealtFrom_returnsNoCardsWhenNobodyHasPlayedACardYet() throws Exception {
        assertTrue("No hands have been played yet, so warTable should have none", warTable.retrieveCardsDealtFrom(oscar).isEmpty());
    }

    @Test
    public void testReceiveCardFrom_canHoldASingleCardPlayedByASinglePlayer() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        warTable.receiveCardsFrom(oscar, oscar.playCards(1));
        assertEquals("Should have the only card that was played by Oscar", expectedCardsFromOscarsHand, warTable.retrieveCardsDealtFrom(oscar));
    }

    @Test
    public void testReceiveCardFrom_canHoldMultipleCardsPlayedByASinglePlayer() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(oscar, king, expectedCardsFromOscarsHand);
        warTable.receiveCardsFrom(oscar, oscar.playCards(1));
        warTable.receiveCardsFrom(oscar, oscar.playCards(1));
        assertTrue("Should have all cards played by Oscar", warTable.retrieveCardsDealtFrom(oscar).containsAll(expectedCardsFromOscarsHand));
    }

    @Test
    public void testReceiveCardFrom_canHoldASingleCardPlayedByEachOfTwoPlayers() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(felix, king, expectedCardsFromFelixsHand);
        warTable.receiveCardsFrom(oscar, oscar.playCards(1));
        warTable.receiveCardsFrom(felix, felix.playCards(1));
        assertEquals("Should have Oscar's card", expectedCardsFromOscarsHand, warTable.retrieveCardsDealtFrom(oscar));
        assertEquals("Should have Felix's card", expectedCardsFromFelixsHand, warTable.retrieveCardsDealtFrom(felix));
    }

    @Test
    public void testReceiveCardFrom_canHoldMultipleCardsPlayedByEachOfTwoPlayers() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        dealTo(oscar, new Card(Rank.THREE, Suit.DIAMOND), expectedCardsFromOscarsHand);
        dealTo(felix, king, expectedCardsFromFelixsHand);
        dealTo(felix, new Card(Rank.NINE, Suit.SPADE), expectedCardsFromFelixsHand);
        warTable.receiveCardsFrom(oscar, oscar.playCards(2));
        warTable.receiveCardsFrom(felix, felix.playCards(2));
        assertTrue("Should have all of Oscar's cards", warTable.retrieveCardsDealtFrom(oscar).containsAll(expectedCardsFromOscarsHand));
        assertTrue("Should have all of Felix's cards", warTable.retrieveCardsDealtFrom(felix).containsAll(expectedCardsFromFelixsHand));
    }

    @Test
    public void testGetPlayerPiles_incorporatesPlayedCardsFromSinglePlayer() throws Exception {
        dealTo(oscar, ace, expectedCardsFromOscarsHand);
        warTable.receiveCardsFrom(oscar, oscar.playCards(1));
        assertEquals(ace, warTable.retrieveCardsDealtFrom(oscar).get(0));
    }

    @Test
    public void testClearAllPilesFromTheTable_leavesNoPiles() {
        DeckImpl deck = new DeckImpl();
        warTable.receiveCardsFrom(CHEWY, deck.deal(11)); //pile 1
        warTable.receiveCardsFrom(JABBA, deck.deal(12)); //pile 2
        assertEquals("Should be 2 piles on the warTable before clearing", 2, warTable.getAllPilesOnTheTable().size());
        warTable.clearAllPilesFromTheTable();
        assertEquals("Should be no more piles on the warTable", 0, warTable.getAllPilesOnTheTable().size());
    }

    @Test
    public void testAreThereTiesPresent_noBecauseAllCardsAreDifferent() {
        List<PlayerPile> pilesThatDoNotTieEachOther = new ArrayList<PlayerPile>();
        pilesThatDoNotTieEachOther.add(new PlayerPile(TESLA, ACE_OF_CLUBS));
        pilesThatDoNotTieEachOther.add(new PlayerPile(THE_DUDE, KING_OF_SPADES));
        assertFalse("All cards are different; there should not be a tie", warTable.areThereTiesPresent(pilesThatDoNotTieEachOther));
    }

    @Test
    public void testAreThereTiesPresent_yesBecauseWeHaveTwoCardsWithTheSameRank() {
        List<PlayerPile> pilesThatTieEachOther = new ArrayList<PlayerPile>();
        pilesThatTieEachOther.add(new PlayerPile(ROSENCRANTZ, ACE_OF_CLUBS));
        pilesThatTieEachOther.add(new PlayerPile(GILDENSTERN, ACE_OF_SPADES));
        assertTrue("Ranks match; there should be a tie", warTable.areThereTiesPresent(pilesThatTieEachOther));
    }

//    @Test
//    public void testAreThereTiesPresent_yesBecauseWeHaveTwoCardsWithTheSameRank() {
//        warTable.putAPlayerPileOnTheTable(new PlayerPile(ROSENCRANTZ, ACE_OF_CLUBS));
//        warTable.putAPlayerPileOnTheTable(new PlayerPile(GILDENSTERN, ACE_OF_SPADES));
//        assertTrue("Ranks match; there should be a tie", warTable.areThereTiesPresent());
//    }


    @Ignore
    @Test
    public void testx() {
        fail();
        // test that receiveCardsFrom not only tracks all cards on warTable, but also current plays for each player
//        getPilesFromMostRecentPlay
                // this will be needed when we try to determine whether a tie has occured in the current handl of play, which is then used to determine if we hsould go to war or not
    }

    private void dealTo(Player player, Card card, List<Card> expectedCardsToUseInTest) {
        player.dealToTopOfPlayersDeck(card);
        expectedCardsToUseInTest.add(card);
    }
}