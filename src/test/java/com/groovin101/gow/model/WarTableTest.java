package com.groovin101.gow.model;

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
    private List<Card> expectedCardsFromGildernsternsHand;
    private List<Card> expectedCardsFromRosencrantzsHand;

    @Before
    public void setup() {
        super.setup();
        warTable = new WarTable();
        expectedCardsFromRosencrantzsHand = new ArrayList<Card>();
        expectedCardsFromGildernsternsHand = new ArrayList<Card>();
    }

    @Test
    public void testRetrieveCardsDealtFrom_returnsNoCardsWhenNobodyHasPlayedACardYet() throws Exception {
        assertTrue("No hands have been played yet, so warTable should have none", warTable.retrieveCardsDealtFrom(GILDENSTERN).isEmpty());
    }

    @Test
    public void testPlayAHand_singlePlayerPlayingASingleCard() throws Exception {
        dealTo(GILDENSTERN, ACE_OF_SPADES, expectedCardsFromGildernsternsHand);
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(1));
        assertEquals(expectedCardsFromGildernsternsHand, warTable.retrieveCardsDealtFrom(GILDENSTERN));
    }

    @Test
    public void testPlayAHand_singlePlayerPlayingMultipleCards() throws Exception {
        dealTo(GILDENSTERN, ACE_OF_SPADES, expectedCardsFromGildernsternsHand);
        dealTo(GILDENSTERN, KING_OF_SPADES, expectedCardsFromGildernsternsHand);
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(1));
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(1));
        assertTrue("Should have all cards played by Oscar", warTable.retrieveCardsDealtFrom(GILDENSTERN).containsAll(expectedCardsFromGildernsternsHand));
    }

    @Test
    public void testPlayAHand_contextCanHoldASingleCardPlayedByEachOfTwoPlayers() throws Exception {
        dealTo(GILDENSTERN, ACE_OF_SPADES, expectedCardsFromGildernsternsHand);
        dealTo(ROSENCRANTZ, KING_OF_SPADES, expectedCardsFromRosencrantzsHand);
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(1));
        warTable.playAHand(ROSENCRANTZ, ROSENCRANTZ.playCards(1));
        assertEquals("Should have R's card", expectedCardsFromRosencrantzsHand, warTable.retrieveCardsDealtFrom(ROSENCRANTZ));
        assertEquals("Should have G's card", expectedCardsFromGildernsternsHand, warTable.retrieveCardsDealtFrom(GILDENSTERN));
    }

    @Test
    public void testPlayAHand_canHoldMultipleCardsPlayedByEachOfTwoPlayers() throws Exception {
        dealTo(GILDENSTERN, ACE_OF_SPADES, expectedCardsFromGildernsternsHand);
        dealTo(GILDENSTERN, new Card(Rank.THREE, Suit.DIAMOND), expectedCardsFromGildernsternsHand);
        dealTo(ROSENCRANTZ, KING_OF_SPADES, expectedCardsFromRosencrantzsHand);
        dealTo(ROSENCRANTZ, new Card(Rank.NINE, Suit.SPADE), expectedCardsFromRosencrantzsHand);
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(2));
        warTable.playAHand(ROSENCRANTZ, ROSENCRANTZ.playCards(2));
        assertTrue("Should have all of G's cards", warTable.retrieveCardsDealtFrom(GILDENSTERN).containsAll(expectedCardsFromGildernsternsHand));
        assertTrue("Should have all of R's cards", warTable.retrieveCardsDealtFrom(ROSENCRANTZ).containsAll(expectedCardsFromRosencrantzsHand));
    }

    @Test
    public void testFetchSignificantCard_usesOnlyCardInASingleCardPile() {
        PlayerPile playersPile = new PlayerPile(CHEWY, ACE_OF_CLUBS);

        assertEquals("A pile with one card should flag that single card as the significant one",
                new PileCard(CHEWY, ACE_OF_CLUBS), warTable.fetchSignificantCard(playersPile));
    }

@Test
public void testFetchSignificantCard_usesLastPlayedCardInAPile() {

}

    @Test
    public void testPlayAHand_latestCardToBePutInPlayShouldBeAddedToTheListOfSignificantCards() throws Exception {

        dealTo(GILDENSTERN, ACE_OF_SPADES, expectedCardsFromGildernsternsHand);
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(1));
        assertTrue("Lists should be identical, containing G's single card", expectedCardsFromGildernsternsHand.equals(pileCardListAsCardList(warTable.fetchSignificantCards())));
    }
//    //todo: //test when players play a hand, that the significant card is updated
//    public void receiveCardsFrom(Player player, List<Card> cardsPassed) {
//        PlayerPile pile = allPilesOnTheTable.get(player);
//        if (pile != null) {
//            pile.getCards().addAll(cardsPassed);
//        }
//        else {
//            pile = new PlayerPile(player, cardsPassed);
//            allPilesOnTheTable.put(player, pile);
//        }
//    }

    @Test
    public void testGetPlayerPiles_incorporatesPlayedCardsFromSinglePlayer() throws Exception {
        dealTo(GILDENSTERN, ACE_OF_SPADES, expectedCardsFromGildernsternsHand);
        warTable.playAHand(GILDENSTERN, GILDENSTERN.playCards(1));
        assertEquals(ACE_OF_SPADES, warTable.retrieveCardsDealtFrom(GILDENSTERN).get(0));
    }

    @Test
    public void testClearAllPilesFromTheTable_leavesNoPiles() {
        DeckImpl deck = new DeckImpl();
        warTable.playAHand(CHEWY, deck.deal(11)); //pile 1
        warTable.playAHand(JABBA, deck.deal(12)); //pile 2
        assertEquals("Should be 2 piles on the warTable before clearing", 2, warTable.getAllPilesOnTheTable().size());
        warTable.clearAllPilesFromTheTable();
        assertEquals("Should be no more piles on the warTable", 0, warTable.getAllPilesOnTheTable().size());
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