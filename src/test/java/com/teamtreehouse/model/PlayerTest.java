package com.teamtreehouse.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nicolasjhampton on 10/18/16.
 */
public class PlayerTest {

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @Before
    public void setUp() throws Exception {
        // Arrange
        player1 = new Player("Nicolas", "Hampton", 45, true);
        player2 = new Player("Nicolas", "Hampton", 45, true);
        player3 = new Player("Nicolas", "Zeemo", 45, true);
        player4 = new Player("Zeemo", "Hampton", 45, true);
    }

    @Test
    public void testProperFormatForToStringMethod() throws Exception {

        // Act
        String playerString = player1.toString();

        // Assert
        assertEquals("Nicolas Hampton, 45 inches with expert experience.", playerString);
    }

    @Test
    public void compareToReturnsZeroForSamePlayer() throws Exception {

        // Act
        int comparison = player2.compareTo(player1);

        // Assert
        assertEquals(0, comparison);
    }

    @Test
    public void compareToReturnsZeroForSamePlayerObject() throws Exception {

        // Act
        int comparison = player1.compareTo(player1);

        // Assert
        assertEquals(0, comparison);
    }

    @Test
    public void compareToReturnsLessThanZeroIfLastNameComesAfterButFirstIsTheSame() throws Exception {

        // Act
        int comparison = player1.compareTo(player3);

        // Assert
        assertTrue(comparison < 0);
    }

    @Test
    public void compareToReturnsLessThanZeroIfFirstNameComesAfter() throws Exception {

        // Act
        int comparison = player1.compareTo(player4);

        // Assert
        assertTrue(comparison < 0);
    }

    @Test
    public void compareToReturnsGreaterThanZeroIfLastNameComesBeforeButFirstIsTheSame() throws Exception {

        // Act
        int comparison = player3.compareTo(player1);

        // Assert
        assertTrue(comparison > 0);
    }

    @Test
    public void compareToReturnsGreaterThanZeroIfFirstNameComesBefore() throws Exception {

        // Act
        int comparison = player4.compareTo(player1);

        // Assert
        assertTrue(comparison > 0);
    }

    @Test
    public void equalsReturnsTrueForSameObject() throws Exception {

        // Act
        boolean comparison = player1.equals(player1);

        // Assert
        assertTrue(comparison);
    }

    @Test
    public void equalsReturnsTrueForSamePlayer() throws Exception {

        // Act
        boolean comparison = player1.equals(player2);

        // Assert
        assertTrue(comparison);
    }

    @Test
    public void equalsReturnsFalseForDifferentPlayerLastName() throws Exception {

        // Act
        boolean comparison = player1.equals(player3);

        // Assert
        assertFalse(comparison);
    }

    @Test
    public void equalsReturnsFalseForDifferentPlayerFirstName() throws Exception {

        // Act
        boolean comparison = player1.equals(player4);

        // Assert
        assertFalse(comparison);
    }
}