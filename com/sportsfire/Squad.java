package com.sportsfire;
import java.util.ArrayList;

public class Squad {
    private ArrayList<Player> playerList; // should be made final?
    public Squad(){
        playerList = new ArrayList<Player>();
        
        playerList.add(new Player());
    }
    
    public ArrayList<Player> getPlayerList(){
        return playerList;
    }
    
    public String getSquadName(){
        return "Super Squad";
    }
}