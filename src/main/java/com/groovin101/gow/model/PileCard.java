package com.groovin101.gow.model;

/**
 */
public class PileCard implements Comparable<PileCard> {

    private Player cardOwner;
    private Card card;

    public PileCard(Player cardOwner, Card card) {
        this.cardOwner = cardOwner;
        this.card = card;
    }

    public Player getCardOwner() {
        return cardOwner;
    }
    public void setCardOwner(Player cardOwner) {
        this.cardOwner = cardOwner;
    }
    public Card getCard() {
        return card;
    }
    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public int compareTo(PileCard o) {
        Card oCard = o == null ? null : o.getCard();
        return card.compareTo(oCard);
    }
}
