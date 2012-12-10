package com.groovin101.gow.test.utils;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class BaseTest {

    public static Player TESLA;
    public static Player THE_DUDE;
    public static Player ROSENCRANTZ;
    public static Player GILDENSTERN;
    public static Player CHEWY;
    public static Player JABBA;

    public static final Card ACE_OF_CLUBS = new Card(Rank.ACE, Suit.CLUB);
    public static final Card KING_OF_SPADES = new Card(Rank.KING, Suit.SPADE);
    public static final Card QUEEN_OF_HEARTS = new Card(Rank.QUEEN, Suit.HEART);
    public static final Card JACK_OF_DIAMONDS = new Card(Rank.JACK, Suit.DIAMOND);

    /**
     * Don't forget to call this in subclasses!
     */
    @Before
    public void setup() {
        GILDENSTERN = buildPlayer("Gildenstern");
        TESLA = buildPlayer("Nikola Tesla");
        THE_DUDE = buildPlayer("The Dude");
        ROSENCRANTZ = buildPlayer("Rosencrantz");
        CHEWY = buildPlayer("Chewbacca");
        JABBA = buildPlayer("Jabba The Hut");
    }

    public static List<Card> buildCardList(Card[] cardArray) {
        List<Card> cardList = new ArrayList<Card>();
        CollectionUtils.addAll(cardList, cardArray);
        return cardList;
    }

    protected PlayerPile buildPlayerPile(Player player, int numberOfNonUniqueCardsInPile) {
        DeckImpl deck = new DeckImpl();
        return new PlayerPile(player, deck.deal(numberOfNonUniqueCardsInPile));
    }

    private static Player buildPlayer(String name) {
        try {
            return new Player(name);
        }
        catch (InvalidUsernameException e) {
            try {
                return new Player("no name provided");
            }
            catch (InvalidUsernameException e2) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<Player> buildPlayerList(Player[] playerArray) {
        List<Player> playerList = new ArrayList<Player>();
        CollectionUtils.addAll(playerList, playerArray);
        return playerList;
    }

}
