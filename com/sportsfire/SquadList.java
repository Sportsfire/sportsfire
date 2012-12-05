package com.sportsfire;
import java.util.ArrayList;

public class SquadList {
    private ArrayList<Squad> squadList;
    // loads the current SquadList from DB
    public SquadList(){
        squadList = new ArrayList<Squad>();
        
        squadList.add(new Squad());
    }
    
    public ArrayList<Squad> getSquadList(){
        return squadList;
    }
}