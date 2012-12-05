package com.sportsfire;
import java.util.ArrayList;

public class Squad {
    private ArrayList<Player> playerList; // should be made final?
    private ArrayList<String> playerNameList;
    public Squad(){
        playerList = new ArrayList<Player>();
        playerNameList = new ArrayList<String>();
        
        Player pl = new Player();
        playerList.add(pl);
        playerNameList.add(pl.getName());
    }
    
    public ArrayList<Player> getPlayerList(){
        return playerList;
    }
    
    public ArrayList<String> getPlayerNameList(){
        return playerNameList;
    }
    
    public String getSquadName(){
        return "Super Squad";
    }
}