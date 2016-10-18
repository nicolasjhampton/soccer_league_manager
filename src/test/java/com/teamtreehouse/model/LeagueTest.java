package com.teamtreehouse.model;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by nicolasjhampton on 10/16/16.
 */
public class LeagueTest {
    private League league;
    private Set<Player> leaguePlayers;
    private ArrayList<Player> testPlayers;

    @Before
    public void setUp() throws Exception {
        Set<Player> players = new TreeSet<Player>(Arrays.asList(Players.load()));
        testPlayers = new ArrayList<>(players);

        List<Player> leaguePlayers = new ArrayList<Player>(Arrays.asList(Players.load()));
        league = new League(leaguePlayers);
    }

    @Test
    public void getPlayerPageReturnsSliceOfTenPlayers() throws Exception {
        // Act
        List<Player> subsetPlayers = league.getPlayerPage(2);

        // Assert
        assertEquals(testPlayers.subList(10, 20), subsetPlayers);
    }

    @Test
    public void getPlayerPageReturnsSliceOfLastPlayers() throws Exception {
        // Act
        List<Player> subsetPlayers = league.getPlayerPage(4);

        // Assert
        assertEquals(testPlayers.subList(30, 33), subsetPlayers);
    }

    // Assert
    @Test(expected = IndexOutOfBoundsException.class)
    public void getPlayerPageUnderRangeThrowsRangeException() throws Exception {
        // Act
        List<Player> subsetPlayers = league.getPlayerPage(0);

    }

    // Assert
    @Test(expected = IndexOutOfBoundsException.class)
    public void getPlayerPageOverRangeThrowsRangeException() throws Exception {
        // Act
        List<Player> subsetPlayers = league.getPlayerPage(5);

    }


}