package com.teamtreehouse.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by nicolasjhampton on 10/18/16.
 */
public class TeamTest {
    private Team team1;
    private Team team2;

    @Before
    public void setUp() throws Exception {

        // Arrange
        team1 = new Team("Sonics", "Russo");
        team2 = new Team("Nilists", "Bauhous");

        team1.addPlayer(new Player("Joe", "Smith", 42, true));
        team1.addPlayer(new Player("Jill", "Tanner", 36, true));
        team1.addPlayer(new Player("Bill", "Bon", 43, true));
        team1.addPlayer(new Player("Eva", "Gordon", 45, false));
        team1.addPlayer(new Player("Matt", "Gill", 40, false));
        team1.addPlayer(new Player("Kimmy", "Stein", 41, false));
        team1.addPlayer(new Player("Jeremy", "Smith", 42, true));

    }


    @Test
    public void getTeamHeightSummaryProducesCorrectSummaryString() throws Exception {

        // Act
        String summary = team1.getTeamHeightSummary();

        // Assert
        String expectedString = "|36in: 1|40in: 1|41in: 1|42in: 2|43in: 1|45in: 1|";
        assertEquals(expectedString, summary);
    }

    @Test
    public void getExpertCountGivesAccurateExperencedCountForTrue() throws Exception {

        // Act
        int expertCount = team1.getExpertCount(true);

        // Assert
        assertEquals(4, expertCount);
    }

    @Test
    public void getExpertCountGivesAccurateNoobCountForFalse() throws Exception {

        // Act
        int expertCount = team1.getExpertCount(false);

        // Assert
        assertEquals(3, expertCount);
    }

    @Test
    public void getExpertCountGivesZeroIfNoOneIsExperienced() throws Exception {

        // Act
        int expertCount = team2.getExpertCount(true);

        // Assert
        assertEquals(0, expertCount);
    }

    @Test
    public void getExpertCountGivesZeroIfNoOneIsNoob() throws Exception {

        // Act
        int expertCount = team2.getExpertCount(false);

        // Assert
        assertEquals(0, expertCount);
    }

    @Test
    public void getTeamTierGivesMaxTierOfTen() throws Exception {

        // Arrange
        Team boldAsLove = new Team("The Hendrix", "Jimi");
        boldAsLove.addPlayer(new Player("Joe", "Smith", 42, true));
        boldAsLove.addPlayer(new Player("Jill", "Tanner", 36, true));
        boldAsLove.addPlayer(new Player("Bill", "Bon", 43, true));
        boldAsLove.addPlayer(new Player("Jeremy", "Smith", 42, true));
        boldAsLove.addPlayer(new Player("Ben", "Droid", 44, true));
        boldAsLove.addPlayer(new Player("Jason", "Seaver", 41, true));
        boldAsLove.addPlayer(new Player("Diego", "Soto", 41, true));
        boldAsLove.addPlayer(new Player("Phillip", "Helm", 44, true));
        boldAsLove.addPlayer(new Player("Les", "Clay", 42, true));
        boldAsLove.addPlayer(new Player("Herschel", "Krustofski", 45, true));
        boldAsLove.addPlayer(new Player("Andrew", "Chalklerz", 42, true));

        // Act
        int tierLevel = boldAsLove.getTeamTier();

        // Assert
        assertEquals(10, tierLevel);
    }

    @Test
    public void getTeamTierGivesMinTierOfZero() throws Exception {

        // Arrange
        Team weakSauce = new Team("Hanson", "MmmBop");
        weakSauce.addPlayer(new Player("Joe", "Smith", 42, false));
        weakSauce.addPlayer(new Player("Jill", "Tanner", 36, false));
        weakSauce.addPlayer(new Player("Bill", "Bon", 43, false));
        weakSauce.addPlayer(new Player("Jeremy", "Smith", 42, false));
        weakSauce.addPlayer(new Player("Ben", "Droid", 44, false));
        weakSauce.addPlayer(new Player("Jason", "Seaver", 41, false));
        weakSauce.addPlayer(new Player("Diego", "Soto", 41, false));
        weakSauce.addPlayer(new Player("Phillip", "Helm", 44, false));
        weakSauce.addPlayer(new Player("Les", "Clay", 42, false));
        weakSauce.addPlayer(new Player("Herschel", "Krustofski", 45, false));
        weakSauce.addPlayer(new Player("Andrew", "Chalklerz", 42, false));

        // Act
        int tierLevel = weakSauce.getTeamTier();

        // Assert
        assertEquals(0, tierLevel);
    }

    @Test
    public void getTeamTierMeasuresExperienceSameForDifferentSizeTeams() throws Exception {

        // Arrange
        Team weakSauce = new Team("Hanson", "MmmBop");
        weakSauce.addPlayer(new Player("Joe", "Smith", 42, true));
        weakSauce.addPlayer(new Player("Jill", "Tanner", 36, false));

        Team boldAsLove = new Team("The Hendrix", "Jimi");
        boldAsLove.addPlayer(new Player("Bill", "Bon", 43, true));
        boldAsLove.addPlayer(new Player("Jeremy", "Smith", 42, true));
        boldAsLove.addPlayer(new Player("Ben", "Droid", 44, false));
        boldAsLove.addPlayer(new Player("Jason", "Seaver", 41, false));

        // Act
        boolean sameTier = weakSauce.getTeamTier() == boldAsLove.getTeamTier();

        // Assert
        assertTrue(sameTier);
    }



    @Test
    public void mapPlayersByHeightReturnsProperGroupingsOfPlayers() throws Exception {

        // Act
        Map<Integer, List<Player>> heightMap = team1.mapPlayersByHeight();
        List<Player> heightList1 = heightMap.get(42);

        // Assert
        assertEquals(6, heightMap.size());
        assertEquals(2, heightList1.size());
    }

    @Test
    public void mapPlayersByHeightReturnsEmptyListForEmptyTeam() throws Exception {

        // Act
        Team nobodies = new Team("Only the Lonelies", "Roy Orbison");
        Map<Integer, List<Player>> heightMap = nobodies.mapPlayersByHeight();

        // Assert
        assertEquals(0, heightMap.size());
    }

    @Test
    public void compareToReturnsZeroForSameTeam() throws Exception {

        // Act
        int comparison = team1.compareTo(team1);

        // Assert
        assertTrue(comparison == 0);
    }

    @Test
    public void compareToReturnsZeroForSameTeamSizeAndTier() throws Exception {

        // Arrange
        Team teamSame = new Team("Sonics", "Some other dude");
        teamSame.addPlayer(new Player("Joe", "Smith", 42, true));
        teamSame.addPlayer(new Player("Jill", "Tanner", 36, true));
        teamSame.addPlayer(new Player("Bill", "Bon", 43, true));
        teamSame.addPlayer(new Player("Eva", "Gordon", 45, false));
        teamSame.addPlayer(new Player("Matt", "Gill", 40, false));
        teamSame.addPlayer(new Player("Kimmy", "Stein", 41, false));
        teamSame.addPlayer(new Player("Jeremy", "Smith", 42, true));

        // Act
        int comparison = team1.compareTo(teamSame);

        // Assert
        assertTrue(comparison == 0);
    }

    @Test
    public void compareToReturnsGreaterThanForSameTeamSizeButLowerTier() throws Exception {

        // Arrange (This team is tier 4, team1 is tier 6, same player count)
        Team teamLowerExp = new Team("Sonics", "Some other dude");
        teamLowerExp.addPlayer(new Player("Joe", "Smith", 42, true));
        teamLowerExp.addPlayer(new Player("Jill", "Tanner", 36, true));
        teamLowerExp.addPlayer(new Player("Bill", "Bon", 43, true));
        teamLowerExp.addPlayer(new Player("Eva", "Gordon", 45, false));
        teamLowerExp.addPlayer(new Player("Matt", "Gill", 40, false));
        teamLowerExp.addPlayer(new Player("Kimmy", "Stein", 41, false));
        teamLowerExp.addPlayer(new Player("Jeremy", "Smith", 42, false));

        // Act
        int comparison = team1.compareTo(teamLowerExp);

        // Assert
        assertTrue(comparison > 0);
    }

    @Test
    public void compareToReturnsGreaterThanForLowerTeamSizeButSameTier() throws Exception {

        // Arrange (This team and team1 both have a tier of 6)
        Team teamLowerTeamSize = new Team("Sonics", "Some other dude");
        teamLowerTeamSize.addPlayer(new Player("Joe", "Smith", 42, true));
        teamLowerTeamSize.addPlayer(new Player("Jill", "Tanner", 36, true));
        teamLowerTeamSize.addPlayer(new Player("Eva", "Gordon", 45, false));
        teamLowerTeamSize.addPlayer(new Player("Kimmy", "Stein", 41, false));
        teamLowerTeamSize.addPlayer(new Player("Jeremy", "Smith", 42, true));

        // Act
        int comparison = team1.compareTo(teamLowerTeamSize);

        // Assert
        assertTrue(comparison > 0);
    }

    @Test
    public void compareToReturnsLessThanForHigherTeamSizeButSameTier() throws Exception {

        // Arrange (This team and team1 both have a tier of 6)
        Team teamLowerTeamSize = new Team("Sonics", "Some other dude");
        teamLowerTeamSize.addPlayer(new Player("Joe", "Smith", 42, true));
        teamLowerTeamSize.addPlayer(new Player("Jill", "Tanner", 36, true));
        teamLowerTeamSize.addPlayer(new Player("Eva", "Gordon", 45, false));
        teamLowerTeamSize.addPlayer(new Player("Kimmy", "Stein", 41, false));
        teamLowerTeamSize.addPlayer(new Player("Jeremy", "Smith", 42, true));

        // Act
        int comparison = teamLowerTeamSize.compareTo(team1);

        // Assert
        assertTrue(comparison < 0);
    }

    @Test
    public void compareToReturnsLessThanForSameTeamSizeButHigherTier() throws Exception {

        // Arrange (This team is tier 4, team1 is tier 6, same player count)
        Team teamLowerExp = new Team("Sonics", "Some other dude");
        teamLowerExp.addPlayer(new Player("Joe", "Smith", 42, true));
        teamLowerExp.addPlayer(new Player("Jill", "Tanner", 36, true));
        teamLowerExp.addPlayer(new Player("Bill", "Bon", 43, true));
        teamLowerExp.addPlayer(new Player("Eva", "Gordon", 45, false));
        teamLowerExp.addPlayer(new Player("Matt", "Gill", 40, false));
        teamLowerExp.addPlayer(new Player("Kimmy", "Stein", 41, false));
        teamLowerExp.addPlayer(new Player("Jeremy", "Smith", 42, false));

        // Act
        int comparison = teamLowerExp.compareTo(team1);

        // Assert
        assertTrue(comparison < 0);
    }

}