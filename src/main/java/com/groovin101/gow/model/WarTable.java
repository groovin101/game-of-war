package com.groovin101.gow.model;

import java.util.*;

/**
 * Serves as the context for gameplay, where all current rounds are played out
 */
public class WarTable implements GameContext {

    private Map<Player, PlayerPile> allPilesOnTheTable = new HashMap<Player, PlayerPile>();

    public void receiveCardsFrom(Player player, List<Card> cardsPassed) {
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

    public List<PlayerPile> getAllPilesOnTheTable() {
        List<PlayerPile> playerPiles = new ArrayList<PlayerPile>();
        playerPiles.addAll(allPilesOnTheTable.values());
        return playerPiles;
    }

    public void clearAllPilesFromTheTable() {
        allPilesOnTheTable.clear();
    }

    public boolean areThereTiesPresent(List<PlayerPile> pilesToInspectForTies) {
        Set<Rank> ranksOfTheSignificantCardsFromEachPile = new HashSet<Rank>();
        for (PlayerPile pile : pilesToInspectForTies) {
            ranksOfTheSignificantCardsFromEachPile.add(pile.fetchLastCardDealt().getRank());
        }
        return (ranksOfTheSignificantCardsFromEachPile.size() < pilesToInspectForTies.size());
    }
}