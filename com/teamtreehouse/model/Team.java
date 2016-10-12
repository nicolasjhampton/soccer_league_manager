package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Team implements Comparable<Team> {
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
  
  public Set<Player> getPlayers() {
   return this.players; 
  }
  
  public void addPlayer(Player player) {
   players.add(player); 
  }
  
  public void removePlayer(Player player) {
   players.remove(player); 
  }
  
  public String printTeam() {
    String borderLine = String.format("===========================================================%n");
    String blankLine = String.format("%n");
    String teamHeader = String.format("| TEAM: %s | COACH: %s | TIER: %d |%n", 
                                     getName(), getCoach(), getTeamTier());
    
    String printOut = borderLine;
    printOut += teamHeader;
    printOut += borderLine;
    printOut += blankLine;
    printOut += borderLine;
    
    Map<Integer, List<Player>> roster = mapPlayersByHeight();
    
    for(Map.Entry<Integer, List<Player>> heightGroup: roster.entrySet()) {
      
     printOut += String.format("%d inches:%n", heightGroup.getKey());
     List<Player> thisHeight = heightGroup.getValue();
      
     for(Player player: thisHeight) {
      printOut += String.format("%s%n", player);
     }
      
     printOut += borderLine;
      
    }
    return printOut;
  }
  
  //  Done
  //  As an organizer who is planning teams, 
  //  I should be able to see a League Balance 
  //  Report for all teams in the league showing 
  //  a total count of experienced players vs. 
  //  inexperienced players, so I can determine 
  //  from a high level if the teams are fairly 
  //  balanced regarding previous experience.
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
   return (int) Math.round(tierFloat);
  }
  
  //  Done
  //  As an organizer planning teams, I should 
  //  be able to view a report of a chosen team 
  //  grouped by height, so that I can determine 
  //  if teams are fair.
  
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
  
  @Override
  public int compareTo(Team other) {
   if(equals(other)) {
    return 0; 
   } else if (teamName.equals(other.teamName)) {
    return coach.compareTo(other.coach); 
   } 
   return teamName.compareTo(other.teamName);
  }
  
}