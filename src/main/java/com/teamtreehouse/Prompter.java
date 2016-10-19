package com.teamtreehouse;

import com.teamtreehouse.model.League;
import com.teamtreehouse.model.ListOfPlayers;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Team;

import java.io.*;
import java.util.*;

/**
 * Created by nicolasjhampton on 10/16/16.
 */
public class Prompter {
    private final League league;
    private BufferedReader reader;
    private final Map<String, String> menu;
    private String flash = "";
    private String borderLine = String.format("===========================================================");


    public Prompter(Player[] players) {
        this.league = new League(Arrays.asList(players));

        this.reader = new BufferedReader(new InputStreamReader(System.in));

        this.menu = new LinkedHashMap<>();
        this.menu.put("players", "Show a list of players (players)");
        this.menu.put("join", "Sign up a player for the league (join)");
        this.menu.put("bump", "Bump a player from the league (add)");
        this.menu.put("create", "Create a team (create)");
        this.menu.put("add", "Add a player to a team (add)");
        this.menu.put("remove", "Remove a player to a team (add)");
        this.menu.put("teams", "Show a list of teams (teams)");
        this.menu.put("team", "Display a sorted team list (team)");
        this.menu.put("generate", "Auto generate entire league (generate)");
        this.menu.put("quit", "Quit the program (quit)");
    }

    ///////////////////////////////////////////////////
    // Main Menu
    ///////////////////////////////////////////////////

    public void menu() {
        String option = "";
        do {
            clearScreen();

            displayFlash();
            currentStatus();
            try {
                option = promptOptions();

                clearScreen();

                menuSwitch(option);

            } catch (IOException ioe) {
                System.out.println("Error reading input");
                ioe.printStackTrace();
            } catch (IndexOutOfBoundsException iobe) {
                System.out.println("Range out of bounds");
                iobe.printStackTrace();
            }
        } while (!option.equals("quit"));
    }

    ///////////////////////////////////////////////////
    // Menu options
    ///////////////////////////////////////////////////

    private void menuSwitch(String option) throws IOException,
                                           IndexOutOfBoundsException {
        switch(option) {
            case "create":
                create();
                break;
            case "add":
                add();
                break;
            case "remove":
                remove();
                break;
            case "join":
                joinLeague();
                break;
            case "bump":
                bumpFromLeague();
                break;
            case "teams":
                printTeams();
                break;
            case "team":
                printTeam();
                break;
            case "players":
                players();
                break;
            case "generate":
                generate();
                break;
            case "quit":
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println(String.format("Option not recognized: %s%n", option));
                break;
        }
    }

    private void create() throws IOException {
        Team newTeam = createTeam();
        league.addTeam(newTeam);
        flash = String.format("Team created: %s, coached by %s",
                newTeam.getName(),
                newTeam.getCoach());
    }

    private void add() throws IOException {
        int teamChoice = chooseTeam();
        Team addingTeam = league.getTeam(teamChoice);
        int playerChoice = choosePlayer(league);

        if(playerChoice != -1 && teamChoice != -1) {

            String first = league.getPlayer(playerChoice).getFirstName();
            String last =  league.getPlayer(playerChoice).getLastName();
            String name = addingTeam.getName();
            int count = addingTeam.getPlayerCount();

            league.addPlayerToTeam(playerChoice, teamChoice);
            flash = String.format("%s %s added to %s, total players: %d",
                    first, last, name, count);
        }
    }

    private void joinLeague() throws IOException {
        Player newPlayer = createPlayer();
        boolean joined = league.addPlayerToLeague(newPlayer);
        if(joined) {
            flash = String.format("%s %s, welcome to the league!%n",
                    newPlayer.getFirstName(),
                    newPlayer.getLastName());
        } else {
            flash = String.format("We're at capacity, %s, you're #%d on the waiting list",
                    newPlayer.getFirstName(),
                    league.getWaitingListLength());
        }
    }

    private void bumpFromLeague() throws IOException {
        int playerToBump = choosePlayer(league);
        Player[] playerChange = league.bumpPlayerFromLeague(playerToBump);

        flash = String.format("%s has been bumped. Good riddance, %s was a bad apple!%n",
                        playerChange[0].getFirstName(),
                        playerChange[0].getLastName());
        if(playerChange[1] != null) {
            flash += String.format("Welcome to the league %s %s!",
                    playerChange[1].getFirstName(),
                    playerChange[1].getLastName());
        }
    }

    private void remove() throws IOException {
        int teamChoice = chooseTeam();
        Team removingTeam = league.getTeam(teamChoice);
        int playerChoice = choosePlayer(removingTeam);

        if(playerChoice != -1 && teamChoice != -1) {

            String first = removingTeam.getPlayer(playerChoice).getFirstName();
            String last =  removingTeam.getPlayer(playerChoice).getLastName();
            String name = removingTeam.getName();
            int count = removingTeam.getPlayerCount() - 1;

            league.removePlayerFromTeam(playerChoice, teamChoice);
            flash = String.format("%s %s removed from %s, total players left: %d",
                    first, last, name, count);
        }
    }

    private void printTeams() {
        System.out.println(String.format(
                "There are currently %d teams created:%n",
                league.getTeams().size()));
        int index = 1;
        for(Team team: league.getTeams()) {
            System.out.println(borderLine);
            System.out.println(String.format("%d) %s", index, team));
            System.out.println(borderLine);
            index++;
        }
        System.out.println("");
    }

    private void printTeam() throws IOException {
        int teamChoice = chooseTeam();
        clearScreen();
        printTeam(teamChoice);
        System.out.print("Would you like to export to a text file? (y/n) ");
        String print = reader.readLine();
        if(print.charAt(0) == 'y') {
            exportTeam(teamChoice);
        }
    }

    private void players() throws IOException {
        choosePlayer(league);
    }

    private void generate() throws IOException {
        String teamName = "";
        String coach = "";
        Map<String, String> teamsToBeCreated = new HashMap<>();
        do {
            teamName = promptTeamName();
            coach = promptTeamCoach();
            if(!teamName.equals("") && !coach.equals("")) {
                teamsToBeCreated.put(teamName, coach);
            }
        } while(!teamName.equals("") || !coach.equals(""));

        league.generateLeague(teamsToBeCreated);

        printTeams();
    }

    ///////////////////////////////////////////////////
    // General IO Helpers
    ///////////////////////////////////////////////////

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void startScreen() {
        clearScreen();
        System.out.println("Welcome to the league's team creator!");
        System.out.println(borderLine);
        System.out.printf("%n");
    }

    private void printPlayerCount(ListOfPlayers playerList) {
        System.out.println(String.format(
                "%d total players.%n",
                playerList.getPlayers().size()));
    }

    private void currentStatus() {
        printPlayerCount(league);
        System.out.println(String.format(
                "There are currently %d teams created.",
                league.getTeams().size()));
        System.out.printf("%n");
    }

    private String promptOptions () throws IOException  {
        for(Map.Entry option: menu.entrySet()) {
            System.out.println(String.format(
                    "-> %s - %s",
                    option.getKey(),
                    option.getValue()
            ));
        }
        System.out.printf("%nSelect an option: ");
        String selectedOption = reader.readLine().toLowerCase().trim();
        return selectedOption;
    }

    private void displayFlash() {
        if(flash.length() != 0) {
            System.out.println(borderLine);
            System.out.println(flash);
            System.out.println(borderLine);
            System.out.println("");
            flash = "";
        }
    }

    ///////////////////////////////////////////////////
    // Team Specific methods
    ///////////////////////////////////////////////////

    private Team createTeam() throws IOException {
        printTeams();
        String teamName = promptTeamName();
        String coach = promptTeamCoach();
        return new Team(teamName, coach);
    }

    private String promptTeamName() throws IOException {
        System.out.print("Name of the new team: ");
        String teamName = reader.readLine();
        return teamName;
    }

    private String promptTeamCoach() throws IOException {
        System.out.print("Name of the team coach: ");
        String coach = reader.readLine();
        return coach;
    }

    private Player createPlayer() throws IOException {
        System.out.print("First name of player: ");
        String playerFirst = reader.readLine();
        System.out.print("Last name of player: ");
        String playerLast = reader.readLine();
        System.out.print("Are You Experienced (y/n): ");
        String exp = reader.readLine();
        boolean experience = (exp.charAt(0) == 'y');
        System.out.print("Enter height in inches: ");
        String heightString = reader.readLine().trim();
        int height = Integer.parseInt(heightString);
        return new Player(playerFirst, playerLast, height, experience);
    }

    private void printTeam(int teamToDisplay) throws IOException {

        System.out.println(league.getTeam(teamToDisplay).printTeam());

    }

    private void exportTeam(int team) throws IOException {
        clearScreen();
        System.out.print("Please enter a file name: ");
        String fileName = reader.readLine().trim().toLowerCase();
        try (
                FileOutputStream fos = new FileOutputStream(fileName + ".txt");
                PrintWriter writer = new PrintWriter(fos);
        ) {
            writer.printf(league.getTeam(team).printTeam());
        } catch (IOException ioe) {
            System.out.printf("Problem saving %s.txt %n", fileName);
            ioe.printStackTrace();
        }
        flash = String.format("%s.txt saved successfully!", fileName);
    }

    ///////////////////////////////////////////////////
    // Option menus
    ///////////////////////////////////////////////////

    private int chooseTeam() throws IOException {
        printTeams();
        System.out.print("Choose a team: ");
        String teamString = reader.readLine().trim().toLowerCase();
        if(teamString.equals("quit")) {
            return -1;
        }

        return Integer.parseInt(teamString) - 1;
    }

    private int choosePlayer(ListOfPlayers playerList)
            throws IOException,
            IndexOutOfBoundsException,
            UnsupportedOperationException {

        int index = 0;
        int page = 1;
        int playerIndex = -1;

        do {
            clearScreen();

            printPlayerCount(playerList);

            List<Player> playerSlice = playerList.getPlayerPage(page);

            for(Player player: playerSlice) {
                System.out.println(String.format("%d) %s", index + 1, player));
                index++;
            }

            // Prompt for player choice
            System.out.println("");
            System.out.println("Indicate player by number");
            System.out.print("(type enter to continue, quit to stop): ");
            String playerString = reader.readLine().trim();

            // continue paging or assign choice
            if (playerString.equals("quit")) {
                // break if user quits
                break;
            } else if(playerString.matches("^-?\\d+$")) { // Check if the input was a number

                // Get the player from the list passed in
                playerIndex = Integer.parseInt(playerString);
                // set flash and return player
                flash = String.format("%s", playerList.getPlayer(playerIndex - 1));
                return playerIndex - 1;

            } else if (!((page * 10) < playerList.getPlayers().size())) {

                // restart loop from beginning if list is at the end
                page = 0;
                index = 0;

            }

            // go to next page if there is one
            page++;


        } while (true);

        // return null if player quits
        flash = String.format(borderLine);
        return -1;
    }

}
