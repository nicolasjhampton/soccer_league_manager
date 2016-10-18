package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by nicolasjhampton on 10/16/16.
 */
public class Team implements Comparable<Team>, ListOfPlayers {
    private String teamName;
    private String coach;
    private Set<Player> players;

    public Team(String teamName, String coach) {
        this.teamName = teamName;
        this.coach = coach;
        this.players = new TreeSet<>();
    }

    public String getCoach() {
        return coach;
    }

    public String getName() {
        return teamName;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) { players.remove(player); }

    public String printTeam() {
        String borderLine = String.format("===========================================================%n");
        String blankLine = String.format("%n");
        String teamHeader = String.format("| TEAM: %s | COACH: %s | TIER: %d |%n",
                getName(), getCoach(), getTeamTier());
        // String heightCountString = "";

        // Print team header
        String printOut = borderLine;
        printOut += teamHeader;
        printOut += borderLine;
        printOut += blankLine;
        printOut += borderLine;

        // Loop through players by height
        Map<Integer, List<Player>> roster = mapPlayersByHeight();
        for(Map.Entry<Integer, List<Player>> heightGroup: roster.entrySet()) {

            // Get current height
            int height = heightGroup.getKey();
            // Header for height group
            printOut += String.format("%d inches:%n", height);

            // Print each player in this height group
            List<Player> thisHeight = heightGroup.getValue();
            for(Player player: thisHeight) {
                printOut += String.format("%s%n", player);
            }

            printOut += borderLine;

            // Add current height to height summary at the end of report
            // heightCountString += String.format("|%din: %d", height, thisHeight.size());
        }

        // Print height summary
        printOut += String.format("%s%n", getTeamHeightSummary());
        printOut += borderLine;

        return printOut;
    }

    // TODO: needs test
    public String getTeamHeightSummary() {
        String heightCountString = "";
        // Loop through players by height
        Map<Integer, List<Player>> roster = mapPlayersByHeight();
        for(Map.Entry<Integer, List<Player>> heightGroup: roster.entrySet()) {

            // Add current height to height summary
            heightCountString += String.format("|%din: %d",
                    heightGroup.getKey(), heightGroup.getValue().size());

        }

        // close section
        heightCountString += "|";

        return heightCountString;
    }

    public int getExpertCount(boolean expert) {
        int index = 0;
        for(Player player: players) {
            if(player.isPreviousExperience() == expert) {
                index++;
            }
        }
        return index;
    }

    public int getTeamTier() {
        float expertsFloat = getExpertCount(true);
        float noobsFloat = getExpertCount(false);
        float tierFloat;
        if(getExpertCount(true) > 0) {
            tierFloat = ( ( ( ( (expertsFloat - noobsFloat) * 10f) / 11f ) + (expertsFloat - 1f) ) / 20f ) * 10f;
            // (( (total exp. * 10 / max team size ) + (no. of experts on the team - 1) ) / points possible ) * 10
            //              max: 10                               max: 10                       max:20
        } else {
            tierFloat = 0;
        }
        return Math.round(tierFloat);
    }

    public Map<Integer, List<Player>> mapPlayersByHeight() {
        Map<Integer, List<Player>> playerByHeight = new TreeMap<>();

        for(Player player: players) {

            List<Player> thisHeightList = playerByHeight.get(player.getHeightInInches());

            if(thisHeightList == null) {
                thisHeightList = new ArrayList<>();
                playerByHeight.put(player.getHeightInInches(), thisHeightList);
            }

            thisHeightList.add(player);
        }

        return playerByHeight;
    }

    ///////////////////////////////////////////////////
    // ListOfPlayers interface overrides
    ///////////////////////////////////////////////////

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public Player getPlayer(int index) {
        return getPlayers().get(index);
    }

    @Override
    public List<Player> getPlayerPage(int page) throws IndexOutOfBoundsException {
        return new ArrayList<>(players);
    }

    ///////////////////////////////////////////////////
    // Comparable interface override
    ///////////////////////////////////////////////////

    @Override
    public int compareTo(Team other) {
        if(equals(other)) {
            return 0;
        } else if (teamName.equals(other.teamName)) {
            return coach.compareTo(other.coach);
        }
        return teamName.compareTo(other.teamName);
    }

    ///////////////////////////////////////////////////
    // toString override
    ///////////////////////////////////////////////////

    @Override
    public String toString() {

        // Summary header
        String title = String.format("%s, coached by %s, has %s players.",
                getName(),
                getCoach(),
                getPlayerCount());

        // Experience stats
        String stats = String.format("%n   | Experts: %d | Noobs: %d | Team Tier: Tier %d |",
                getExpertCount(true), getExpertCount(false), getTeamTier());

        // Height stats
        String heights = String.format("%n   %s", getTeamHeightSummary());

        return title + stats + heights;
    }

}
