package com.groovin101.gow.rules;

import com.groovin101.gow.model.WarTable;

/**
 */
public interface Rule {

    public void fireRule(WarTable gameWarTable, RuleChainImplGameOfWar ruleChain);

    /*

    results of a rule enforcement

        player wins hand
            player x forfeits hand
            player y foreits hand
            player z claims hand

        players tie hand
            player x plays another hand
            player y plays another hand
            player z plays another hand

        players hand option:
            win
            lose
            tie
            run out of cards
                 If a player runs out of cards while dealing the face-down cards of a war, he may play the last card in his deck as his face-up card and still have a chance to stay in the game
                 If one of the players has no more cards in a battle that player wins that battle


        ie war must be lost, not forfeited


build a rule engine and enforce sequentially; rules should know if they fire a next one or if they continue to the next round

        dealing:
            The deck is divided evenly among the two players, giving each a down stack

        first play of round:
            Each player reveals the top card on his deck (a "battle")
                LastCardInABattleRule - [confusing for multi-player war; only enforce this for 2 player war]
                    If one of the players has no more cards in a battle
                        that player wins that battle
                HighestCardRule
                    Player with the highest card
                        HighestCardTieRule [If the two highest cards played are of equal value]
                            NO
                                player with highest card
                                    takes all the cards played
                                    moves them to the bottom of his stack
                                next round of play
                            YES - [war!]
                                    *each* player
                                        lays down three face-down cards
                                            LastCardInAPileRule [If a player runs out of cards while dealing the face-down cards of a war}
                                                YES
                                                    he may play the last card in his deck as his face-up card
                                                NO
                                                    picks one of the cards out of the three as his face up card
                                                        HighestCardTieRule [If the two highest cards played are of equal value]
                                                            NO
                                                                takes all the cards played
                                                                moves them to the bottom of his stack
                                                                next round of play
                                                            YES - [war!]
                                                                ...
        winning:
            A player wins by collecting all the cards

        conditions:
            if the order the winning cards are placed is fixed it is possible to create an infinite loop wherein neither side will win and the game will not end.



        EXTERNAL RULE SCENARIO FOR CONSIDERATION:
            If you run out of cards during a war,
                your last card is turned face up and is used for all battles in that war.
                If this happens to both players in a war and their last cards are equal,
                    the game is a draw.
            Example: Players A and B both play sevens, so there is a war.
                Player A plays a card face down, but player B has only one card, so it must be played face up. It is a queen.
                Player A plays a card face up and it is also a queen, so the war must continue.
                Player B's queen stays (B's last card) while player A plays a card face down and one face up, which is a nine.
                Player B wins the war and takes all these seven cards (the five cards that A played and the two cards that B played) and the game continues normally.
     */
}
