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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PileCard pileCard = (PileCard) o;

        if (card != null ? !card.equals(pileCard.card) : pileCard.card != null) {
            return false;
        }
        if (cardOwner != null ? !cardOwner.equals(pileCard.cardOwner) : pileCard.cardOwner != null) {
            return false;
        }

        return true;
    }
    @Override
    public int hashCode() {
        int result = cardOwner != null ? cardOwner.hashCode() : 0;
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }
}
