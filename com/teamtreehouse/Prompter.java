package com.teamtreehouse;


import java.io.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Team;


public class Prompter {
  private final Map<String, String> menu;
  private BufferedReader reader;
  private Set<Player> players;
  private Set<Team> teams;
  private String flash = "";
  private String borderLine = String.format("===========================================================");
  
  
  public Prompter(Player[] players) {
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    this.menu = new LinkedHashMap<>();
    this.teams = new TreeSet<Team>();
    Arrays.sort(players);
    this.players = new TreeSet<>(Arrays.asList(players));
    
    this.menu.put("players", "Show a list of players (players)");
    this.menu.put("create", "Create a team (create)");
    this.menu.put("add", "Add a player to a team (add)");
    this.menu.put("remove", "Remove a player to a team (add)");
    this.menu.put("teams", "Show a list of teams (teams)");
    this.menu.put("team", "Display a sorted team list (team)");
    this.menu.put("quit", "Quit the program (quit)");
  }
  
  public void startScreen() {
   clearScreen();
   System.out.println("Welcome to the league's team creator!"); 
   System.out.println(borderLine);
   System.out.printf("%n");
  }
  
  public void menu() {
   String option = "";
   do {
     Team teamChoice = null;
     Player playerChoice = null;
     displayFlash();
     currentStatus();
     try {
       option = promptOptions();
       clearScreen();
       switch(option) {
         case "create":
          // TODO: create a team
          teamChoice = createTeam();
          teams.add(teamChoice);
          flash = String.format("Team created: %s, coached by %s", 
                                teamChoice.getName(), 
                                teamChoice.getCoach());
          break;
         case "add":
          teamChoice = chooseTeam();
          playerChoice = printPlayers(true, players);
          if(playerChoice != null && teamChoice != null) {
            teamChoice.addPlayer(playerChoice);
            flash = String.format("%s %s added to %s, total players: %s",
                                  playerChoice.getFirstName(),
                                  playerChoice.getLastName(),
                                  teamChoice.getName(), 
                                  teamChoice.getPlayerCount());
          }
          break;
         case "remove":
          teamChoice = chooseTeam();
          playerChoice = printPlayers(teamChoice.getPlayers());
          if(playerChoice != null && teamChoice != null) {
            teamChoice.removePlayer(playerChoice);
            flash = String.format("%s %s added to %s, total players: %s",
                                  playerChoice.getFirstName(),
                                  playerChoice.getLastName(),
                                  teamChoice.getName(), 
                                  teamChoice.getPlayerCount());
          }
          break;
         case "teams":
          printTeams();
          break;
         case "team":
          teamChoice = chooseTeam();
          clearScreen();
          printTeam(teamChoice);
          System.out.print("Would you like to export to a text file? (y/n) ");
          String print = reader.readLine();
          if(print.charAt(0) == 'y') {
           exportTeam(teamChoice); 
          }
          break;
         case "players":
          printPlayers(true, players);
          break;
         case "quit":
          System.out.println("Goodbye!");
          break;
         default:
          System.out.println(String.format("Option not recognized: %s%n", option));
          break;
       }
     } catch (IOException ioe) {
       System.out.println("Error reading input");
       ioe.printStackTrace();
     } catch (IndexOutOfBoundsException iobe) {
       System.out.println("Range out of bounds");
       iobe.printStackTrace();
     }
   } while (!option.equals("quit"));
  }
  
  private void clearScreen() {
   System.out.print("\033[H\033[2J");  
   System.out.flush();
  }
  
  private void currentStatus() {
    System.out.println(String.format(
       "There are currently %d registered players.", 
       players.size()));
    System.out.println(String.format(
     "There are currently %d teams created.", 
     teams.size()));
    System.out.printf("%n");
  }
  
  private void displayFlash() {
   if(flash.length() != 0) {
    clearScreen();
    System.out.println(borderLine);
    System.out.println(flash);
    System.out.println(borderLine);
    System.out.println("");
    flash = "";
   }
  }
  
  private void printTeams() {
    int index = 1;
    System.out.println(String.format(
     "There are currently %d teams created:%n", 
     teams.size()));
   
    for(Team team: teams) {
      
     int experts = team.getExpertCount(true);
     int noobs = team.getExpertCount(false);
     int tier = team.getTeamTier();
     
     System.out.println(borderLine);
     System.out.println(
       String.format("%d) %s, coached by %s, has %s players.", 
                     index, 
                     team.getName(),
                     team.getCoach(),
                     team.getPlayerCount()));
     System.out.println(String.format("   | Experts: %d | Noobs: %d | Team Tier: Tier %d |", 
                                      experts, noobs, tier));
     System.out.println(borderLine);
       index++;
      
    }
    
    System.out.println("");
  }
  
  //  Done!
  //  As a coach of a team, I should be able to 
  //  print out a roster of all the players on 
  //  my team, so that I can plan appropriately.
  private void exportTeam(Team team) throws IOException {
    clearScreen();
    System.out.print("Please enter a file name: ");
    String fileName = reader.readLine().trim().toLowerCase();
    try (
      FileOutputStream fos = new FileOutputStream(fileName + ".txt");
      PrintWriter writer = new PrintWriter(fos);
    ) {
      writer.printf(team.printTeam());
    } catch (IOException ioe) {
      System.out.printf("Problem saving %s.txt %n", fileName);
      ioe.printStackTrace();
    }
    flash = String.format("%s.txt saved successfully!", fileName);
  }
  
  private void printTeam(Team teamToDisplay) throws IOException {
    
    System.out.println(teamToDisplay.printTeam());
    
  }
  
  private Team chooseTeam() throws IOException {
    printTeams();
    System.out.print("Choose a team: ");
    String teamString = reader.readLine().trim().toLowerCase();
    if(teamString.equals("quit")) {
      return null;
    } else {
      int teamIndex = Integer.parseInt(teamString);
      List<Team> teamArray = new ArrayList<>(teams);
      return teamArray.get(teamIndex - 1);
    }
  }
  
  private Player printPlayers(boolean action, 
                              Set<Player> playerSet) 
                              throws IOException, 
                              IndexOutOfBoundsException,
                              UnsupportedOperationException {
    
    int index = 1;
    int bottom = 0;
    int playerIndex = -1;
    List<Player> playerList = new ArrayList(playerSet);
    
    do {
      clearScreen();
      System.out.println(String.format(
       "There are currently %d players available:%n", 
       players.size()));
      
      // Determine range of players to show
      int top;
      if(playerList.size() - bottom < 10) {
       top = playerList.size();
      } else {
       top = bottom + 10; 
      }
      
      // Show page of players (throws IndexOutOfBoundsException)
      List<Player> playerSlice = playerList.subList(bottom, top);
      for(Player player: playerSlice) {
        System.out.println(String.format("%d) %s", index, player));
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
        Player playerSelected = playerList.get(playerIndex - 1);
        
        // Modify the available players list member variable
        // throws UnsupportedOperationException
        if(action) {
          if(players.contains(playerSelected)) {
            players.remove(playerSelected);
          }
        } else {
          if(!players.contains(playerSelected)) {
           players.add(playerSelected); 
          }
        }
        
        // set flash and return player
        flash = String.format("%s", playerSelected);
        return playerSelected;
        
      } else if (top < playerList.size()) {
        
        // go to next page if there is one
        bottom += 10;
        
      } else {
        
        // restart loop from beginning if list is at the end
        bottom = 0;
        index = 1;
        
      }
      
    } while (true);
    
    // return null if player quits
    flash = String.format(borderLine);
    return null;
  }
  
  private Player printPlayers(Set<Player> playerSet) 
                              throws IOException, 
                              IndexOutOfBoundsException,
                              UnsupportedOperationException {
    
    return printPlayers(false, playerSet);
  }
  
  private Team createTeam() throws IOException {
    printTeams();
    System.out.print("Name of the new team: ");
    String teamName = reader.readLine();
    System.out.print("Name of the team coach: ");
    String coach = reader.readLine();
    return new Team(teamName, coach);
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
  
}