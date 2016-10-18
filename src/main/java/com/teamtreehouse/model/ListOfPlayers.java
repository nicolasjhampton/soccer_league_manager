package com.teamtreehouse.model;

import java.util.List;

/**
 * Created by nicolasjhampton on 10/17/16.
 */
public interface ListOfPlayers {

    List<Player> getPlayerPage(int page) throws IndexOutOfBoundsException;

    List<Player> getPlayers();

    Player getPlayer(int index);

}
