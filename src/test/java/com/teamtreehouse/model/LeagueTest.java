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
    //private Set<Player> leaguePlayers;
    private List<Player> testPlayers;

    @Before
    public void setUp() throws Exception {

        // Sorted list of players to test against
        Set<Player> players = new TreeSet<Player>(Arrays.asList(Players.load()));
        testPlayers = new ArrayList<>(players);

        List<Player> leaguePlayers = new ArrayList<Player>(Arrays.asList(Players.load()));
        league = new League(leaguePlayers);
    }

    @Test
    public void bumpPlayerFromLeagueLowersLeagueCountIfNoWaitList() throws Exception {

        // Act
        league.bumpPlayerFromLeague(5);

        // Assert
        assertEquals(32, league.getTotalPlayersInLeague());

    }

    @Test
    public void bumpPlayerFromLeagueRemovesCorrectPlayerFromLeague() throws Exception {

        // Act
        Player[] bumpedArray = league.bumpPlayerFromLeague(5);

        // Assert
        assertEquals(testPlayers.get(5), bumpedArray[0]);

    }

    @Test
    public void bumpPlayerFromLeagueLeagueNoLongerContainsBumpedPlayer() throws Exception {

        // Act
        Player[] bumpedArray = league.bumpPlayerFromLeague(5);

        // Assert
        assertFalse(league.getPlayers().contains(bumpedArray[0]));

    }

    @Test
    public void bumpPlayerFromLeagueAddsWaitingListPlayerToLeagueIfPresent() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);
        boolean added = league.addPlayerToLeague(newToLeague);

        // Act
        Player[] bumpedArray = league.bumpPlayerFromLeague(6);
        int numberWaiting = league.getWaitingListLength();

        // Assert
        assertFalse(added); // Indicates player added to league was originally on waiting list
        assertTrue(league.getPlayers().contains(newToLeague));

    }

    @Test
    public void bumpPlayerFromLeagueAddsWaitingListPlayerReferenceToBumpedArrayIfAddedToLeague() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);
        boolean added = league.addPlayerToLeague(newToLeague);

        // Act
        Player[] bumpedArray = league.bumpPlayerFromLeague(6);

        // Assert
        assertFalse(added); // Indicates player added to league was originally on waiting list
        assertEquals(newToLeague, bumpedArray[1]);

    }

    @Test
    public void bumpPlayerFromLeagueReturnsNullValueForSecondArrayItemIfNoWaitingListPlayerPresent() throws Exception {

        // Act
        Player[] bumpedArray = league.bumpPlayerFromLeague(6);

        // Assert
        assertEquals(null, bumpedArray[1]);

    }

    @Test
    public void addPlayerToLeagueReturnsFalseIfAddedToWaitingList() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);

        // Act
        boolean added = league.addPlayerToLeague(newToLeague);

        // Assert
        assertFalse(added);
    }

    @Test
    public void addPlayerToLeagueReturnsTrueIfAddedToLeague() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);
        league.bumpPlayerFromLeague(1);

        // Act
        boolean added = league.addPlayerToLeague(newToLeague);

        // Assert
        assertTrue(added);
    }

    @Test
    public void addPlayerToLeagueAddsPlayerToWaitingListIfNoRoomAvailable() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);

        // Act
        boolean added = league.addPlayerToLeague(newToLeague);

        // Assert
        assertEquals(1, league.getWaitingListLength());
    }

    @Test
    public void addPlayerToLeagueAddsPlayerToLeagueIfSpaceIsAvailable() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);
        league.bumpPlayerFromLeague(1);

        // Act
        boolean added = league.addPlayerToLeague(newToLeague);

        // Assert
        assertTrue(league.getPlayers().contains(newToLeague));
    }

    // Assert
    @Test
    public void addTeamAddsATeamToTheLeague() throws Exception {

        // Act
        league.addTeam(new Team("The Sonics", "Robert Murphy"));


        // Assert
        assertEquals("The Sonics", league.getTeam(0).getName());

    }

    // Assert
    @Test(expected = IndexOutOfBoundsException.class)
    public void addTeamThrowsExceptionIfFourTeamsExistThatHaveSevenPlayers() throws Exception {

        // The theory behind this:
        // Four teams of seven players takes up 28 players
        // So by this point, we won't have enough players
        // to make another full team, assuming we start
        // with 33 players. The algorithm implemented adjusts,
        // however, to any number of players in the league

        // Arrange pt.1 (create four teams)
        String number = "";
        for(int i = 0; i < 4; ++i) {
            number = String.format("%d%n", i);
            league.addTeam(new Team(number, number));
        }

        // Arrange pt.1 (fill Teams)
        for(int i = 0; i < 28; i++) {
            league.addPlayerToTeam(0, 0);
        }

        // Act
        league.addTeam(new Team("5", "5"));

    }

    // Assert
    @Test(expected = IndexOutOfBoundsException.class)
    public void addTeamThrowsExceptionIfThreeTeamsExistThatHaveElevenPlayers() throws Exception {

        // The theory behind this:
        // Three teams of eleven players takes up 33 players
        // So by this point, we won't have enough players
        // to make another full team, assuming we start
        // with 33 players. The algorithm implemented adjusts,
        // however, to any number of players in the league

        // Arrange pt.1 (create three teams)
        String number = "";
        for(int i = 0; i < 3; ++i) {
            number = String.format("%d%n", i);
            league.addTeam(new Team(number, number));
        }

        // Arrange pt.1 (fill Teams)
        for(int i = 0; i < 33; i++) {
            league.addPlayerToTeam(0, 0);
        }

        // Act (IndexOutOfBoundsException("Not enough players for a new team"))
        league.addTeam(new Team("4", "4"));

    }

    // Assert
    @Test(expected = IndexOutOfBoundsException.class)
    public void addTeamThrowsExceptionIfThirtyThreeTeamsExistThatAllHaveOnePlayers() throws Exception {

        // The theory behind this:
        // Thirty-Three teams of one player each takes up 33 players
        // So by this point, we won't have enough players
        // to make another full team, assuming we start
        // with 33 players. The algorithm implemented adjusts,
        // however, to any number of players in the league

        // Arrange pt.1 (create thirty-three teams)
        String number = "";
        for(int i = 0; i < 33; ++i) {
            number = String.format("%d%n", i);
            league.addTeam(new Team(number, number));
        }

        // Arrange pt.1 (fill Teams)
        for(int i = 0; i < 33; i++) {
            league.addPlayerToTeam(0, 0);
        }

        // Act (IndexOutOfBoundsException("Not enough players for a new team"))
        league.addTeam(new Team("34", "34"));

    }

    @Test
    public void getTotalPlayerInLeagueGivesTotalPlayersInLeagueNotWaitingList() throws Exception {

        // Arrange
        Player newToLeague = new Player("Nicolas", "Hampton", 45, true);
        boolean added = league.addPlayerToLeague(newToLeague);

        // Act
        int leaguePlayersCount = league.getTotalPlayersInLeague();

        // Assert (League started with 33 players)
        assertFalse(added); // confirms the player was added to the waiting list
        assertEquals(33, leaguePlayersCount);
    }

//    @Test
//    public void getTotalPlayerInLeagueGivesTotalPlayersInAndOutOfTeams() throws Exception {
//
//        // Arrange
//
//        boolean added = league.addPlayerToTeam();
//
//        // Act
//        int leaguePlayersCount = league.getTotalPlayersInLeague();
//
//        // Assert (League started with 33 players)
//        assertFalse(added); // confirms the player was added to the waiting list
//        assertEquals(33, leaguePlayersCount);
//    }

    @Test
    public void getTotalPlayerInTeams() throws Exception {

        // Arrange pt.1 (create four teams)
        String number = "";
        for(int i = 0; i < 4; ++i) {
            number = String.format("%d%n", i);
            league.addTeam(new Team(number, number));
        }

        // Arrange pt.1 (add 20 players to teams)
        for(int i = 0; i < 20; i++) {
            league.addPlayerToTeam(0, 0);
        }

        // Act
        int teamPlayers = league.getTotalPlayerInTeams();

        // Assert
        assertEquals(20, teamPlayers);
    }

    @Test
    public void getTeamWithMostPlayers() throws Exception {

        // Arrange pt.1 (create four teams)
        String number = "";
        for(int i = 0; i < 4; ++i) {
            number = String.format("%d", i);
            league.addTeam(new Team(number, number));
        }

        // Arrange pt.2 (add varying amounts of players, team "3" having the most)
        // Teams sort themselves primarily by size, lowest at top, so the order
        // of the teams should stay constant here
        league.addPlayerToTeam(0, 3);
        league.addPlayerToTeam(0, 3);
        league.addPlayerToTeam(0, 3);
        league.addPlayerToTeam(0, 2);
        league.addPlayerToTeam(0, 2);
        league.addPlayerToTeam(0, 1);

        // Act
        Team greatestTeam = league.getTeamWithMostPlayers();

        // Assert
        assertEquals("3", greatestTeam.getName());

    }

    @Test
    public void addPlayerToTeam() throws Exception {

    }

    @Test
    public void removePlayerFromTeam() throws Exception {

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