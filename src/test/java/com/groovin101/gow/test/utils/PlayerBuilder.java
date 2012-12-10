package com.groovin101.gow.test.utils;

import com.groovin101.gow.exception.InvalidUsernameException;
import com.groovin101.gow.model.Player;

/**
 */
public class PlayerBuilder {

    public static final Player TESLA = buildPlayer("Nikola Tesla");
    public static final Player THE_DUDE = buildPlayer("The Dude");
    public static final Player ROSENCRANTZ = buildPlayer("Rosencrantz");
    public static final Player GILDENSTERN = buildPlayer("Gildenstern");
    public static final Player CHEWY = buildPlayer("Chewbacca");
    public static final Player JABBA = buildPlayer("Jabba The Hut");

    public static Player buildPlayer(String name) {
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

}
