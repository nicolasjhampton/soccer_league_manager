package com.teamtreehouse.model;

import java.util.*;

/**
 * Created by nicolasjhampton on 10/16/16.
 */
public class League implements ListOfPlayers {
    private final Set<Team> teams;
    private final Set<Player> players;
    private final int MAX_PLAYERS;
    private final ArrayDeque<Player> waitingList;

    public League(List<Player> players) {
        this.teams = new TreeSet<>();
        this.players = new TreeSet<>(players);
        this.MAX_PLAYERS = players.size();
        this.waitingList = new ArrayDeque<>();
    }

    public int getMaxLeaguePlayers() {
        return MAX_PLAYERS;
    }

    public int getWaitingListLength() {
        return waitingList.size();
    }

    public List<Team> getTeams() {
        return new ArrayList<Team>(teams);
    }

    public Team getTeam(int index) {
        return getTeams().get(index);
    }


    public int getTotalPlayersInLeague() {
        return players.size() + getTotalPlayerInTeams();
    }

    // TODO: Needs Test
    public Player[] bumpPlayerFromLeague(int playerToBump) {
        Player bumpedPlayer = getPlayer(playerToBump);
        players.remove(bumpedPlayer);
        if(getWaitingListLength() != 0) {
           Player newLeaguePlayer = waitingList.poll();
           players.add(newLeaguePlayer);
           Player[] playerChange = {bumpedPlayer, newLeaguePlayer};
           return playerChange;
        } else {
           Player[] playerChange = {bumpedPlayer, null};
           return playerChange;
        }
    }

    // TODO: Needs test
    public boolean addPlayerToLeague(Player newPlayer) {
        boolean joinedLeague;
        if(getTotalPlayersInLeague() < MAX_PLAYERS) {
            players.add(newPlayer);
            joinedLeague = true;
        } else {
            waitingList.add(newPlayer);
            joinedLeague = false;
        }
        return joinedLeague;
    }

    // TODO: Needs test
    public int getTotalPlayerInTeams() {
        return getTeams().stream().mapToInt(Team::getPlayerCount).sum();
    }

    // TODO: Needs test
    public Team getTeamWithMostPlayers() {
        return Collections.max(getTeams(), Comparator.comparing(Team::getPlayerCount));
    }

    // TODO: Needs test
    public void addTeam(Team team) {

        // If only one player was on each team, we could have 33 teams
        int totalTeamsCurrentlyAllowed = 33;

        // If a team has been created
        if(getTeams().size() != 0) {

            float greatestPlayerCount;
            // if the number of players on the largest team is not equal to zero
            if((greatestPlayerCount = (float) getTeamWithMostPlayers().getPlayerCount()) != 0)  {

                // Get the sum of all the players in the league
                float totalLeaguePlayers = (float) getTotalPlayersInLeague();

                // divide the total players in league by number of players in largest team, rounded down
                float totalTeamsAllowedFloat = totalLeaguePlayers / greatestPlayerCount;

                // cast into double for Math.floor method
                double totalTeamsAllowedDouble = (double) totalTeamsAllowedFloat;

                // cast into integer for comparison
                totalTeamsCurrentlyAllowed = (int) Math.floor(totalTeamsAllowedDouble);
            }

        }

        // If we're requesting to make more teams than the current max allowed, throw exception
        if (getTeams().size() < totalTeamsCurrentlyAllowed) {
            teams.add(team);
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    // TODO: Needs test
    public void addPlayerToTeam(int player, int team) {
        Player playerToAdd = getPlayer(player);

        Team addToTeam = getTeam(team);

        addToTeam.addPlayer(playerToAdd);

        players.remove(playerToAdd);

    }

    // TODO: Needs test
    public void removePlayerFromTeam(int player, int team) {
        Team selectedTeam = getTeam(team);

        Player playerToRemove = selectedTeam.getPlayer(player);

        players.add(playerToRemove);

        selectedTeam.removePlayer(playerToRemove);

    }

    ///////////////////////////////////////////////////
    // ListOfPlayers interface overrides
    ///////////////////////////////////////////////////

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    @Override
    public Player getPlayer(int index) {
        return getPlayers().get(index);
    }

    @Override
    public List<Player> getPlayerPage(int page) throws IndexOutOfBoundsException {
        // Determine range of players to show
        page = page - 1;
        int bottom = page * 10;
        if(bottom < 0 || bottom
                > players.size()) {
            throw new IndexOutOfBoundsException();
        }
        int top;
        if(players.size() - bottom < 10) {
            top = players.size();
        } else {
            top = bottom + 10;
        }
        List<Player> playersList = new ArrayList<>(players);
        return playersList.subList(bottom, top);
    }

}
