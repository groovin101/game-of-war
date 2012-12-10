package com.groovin101.gow.test.utils;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.model.Player;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class PlayerBuilder {

    public static final Player TESLA = buildPlayer("Nikola Tesla");
    public static final Player THE_DUDE = buildPlayer("The Dude");
    public static final Player ROSENCRANTZ = buildPlayer("Rosencrantz");
    public static final Player GILDENSTERN = buildPlayer("Gildenstern");
    public static final Player CHEWY = buildPlayer("Chewbacca");
    public static final Player JABBA = buildPlayer("Jabba The Hut");

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
