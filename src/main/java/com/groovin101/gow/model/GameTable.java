package com.groovin101.gow.model;

import java.util.*;

/**
 * Serves as the context for gameplay, where all current rounds are played out
 */
public class GameTable implements GameContext {

    private Map<Player, PlayerPile> allPilesOnTheTable;

    public GameTable() {
        allPilesOnTheTable = new HashMap<Player, PlayerPile>();
    }


    public void playAHand(Player player, List<Card> cardsPassed) {

        PlayerPile pile = allPilesOnTheTable.get(player);

        if (pile != null) {
            pile.getCards().addAll(cardsPassed);
        }
        else {
            pile = new PlayerPile(player, cardsPassed);
            allPilesOnTheTable.put(player, pile);
        }
    }

    public List<Card> retrieveCardsDealtFrom(Player player) {

        return allPilesOnTheTable.get(player) == null ? new ArrayList<Card>() : allPilesOnTheTable.get(player).getCards();
    }

    public void clearAllPilesFromTheTable() {

        allPilesOnTheTable.clear();
    }

    PileCard fetchSignificantCard(PlayerPile pile) {

        return new PileCard(pile.getPlayer(), pile.fetchLastCardDealt());
    }

    public List<PileCard> fetchSignificantCards() {

        List<PileCard> significantCards = new ArrayList<PileCard>();
        for (PlayerPile pile : allPilesOnTheTable.values()) {
            significantCards.add(fetchSignificantCard(pile));
        }
        return significantCards;
    }

    public List<PlayerPile> getAllPilesOnTheTable() {

        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        playerPiles.addAll(allPilesOnTheTable.values());
        return playerPiles;
    }
}