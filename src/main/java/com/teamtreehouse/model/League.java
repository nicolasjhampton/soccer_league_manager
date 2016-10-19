package com.teamtreehouse.model;

import java.util.*;

/**
 * Created by nicolasjhampton on 10/16/16.
 */
public class League implements ListOfPlayers {
    private final TreeSet<Team> teams;
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
        List<Team> teamsToSort = new ArrayList<>(teams);
        Collections.sort(teamsToSort);
        return new ArrayList<Team>(teamsToSort);
    }

    public Team getTeam(int index) {
        return getTeams().get(index);
    }


    public int getTotalPlayersInLeague() {
        return players.size() + getTotalPlayerInTeams();
    }

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

    public int getTotalPlayerInTeams() {
        return getTeams().stream().mapToInt(Team::getPlayerCount).sum();
    }

    public Team getTeamWithMostPlayers() {
        return Collections.max(getTeams(), Comparator.comparing(Team::getPlayerCount));
    }

    public void addTeam(Team team) {

        // If only one player was on each team, we could have 33 teams
        int maxTotalTeamsCurrentlyAllowed = getTotalPlayersInLeague();

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
                maxTotalTeamsCurrentlyAllowed = (int) Math.floor(totalTeamsAllowedDouble);
            }

        }

        // If we're requesting to make more teams than the current max allowed, throw exception
        if (getTeams().size() < maxTotalTeamsCurrentlyAllowed) {
            teams.add(team);
        } else {
            throw new IndexOutOfBoundsException("Not enough players for a new team");
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

    // TODO: Needs test
    public TreeMap<Integer, List<Player>> mapPlayersByHeight() {
        TreeMap<Integer, List<Player>> playerByHeight = new TreeMap<>();

        for(Player player: getPlayers()) {

            List<Player> thisHeightList = playerByHeight.get(player.getHeightInInches());

            if(thisHeightList == null) {
                thisHeightList = new ArrayList<>();
                playerByHeight.put(player.getHeightInInches(), thisHeightList);
            }

            thisHeightList.add(player);
        }

        return playerByHeight;
    }

    // TODO: Needs test
    public List<Team> generateLeague(Map<String, String> teams) {

        for(Map.Entry<String, String> team: teams.entrySet()) {
            addTeam(new Team(team.getKey(), team.getValue()));
        }

        Map<Integer, List<Player>> playerHeightMap = mapPlayersByHeight();

        // Arrange pt.2 (add eleven players to each team)
        for(Map.Entry<Integer, List<Player>> nextHighestHeightEntry: playerHeightMap.entrySet()) {
            List<Player> playersOfThisHeight = nextHighestHeightEntry.getValue();
            for(Player player: playersOfThisHeight) {
                int NoOfNextHighestPlayer = getPlayers().indexOf(player);
                addPlayerToTeam(NoOfNextHighestPlayer, 0);
            }
        }

        return getTeams();

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
