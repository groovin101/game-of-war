package com.groovin101.gow.test.utils;

import com.groovin101.gow.model.Card;
import com.groovin101.gow.model.Rank;
import com.groovin101.gow.model.Suit;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CardBuilder {

    public static final Card ACE_OF_CLUBS = new Card(Rank.ACE, Suit.CLUB);
    public static final Card KING_OF_SPADES = new Card(Rank.KING, Suit.SPADE);
    public static final Card QUEEN_OF_HEARTS = new Card(Rank.QUEEN, Suit.HEART);
    public static final Card JACK_OF_DIAMONDS = new Card(Rank.JACK, Suit.DIAMOND);

    public static List<Card> buildCardList(Card[] cardArray) {
        List<Card> cardList = new ArrayList<Card>();
        CollectionUtils.addAll(cardList, cardArray);
        return cardList;
    }
}
