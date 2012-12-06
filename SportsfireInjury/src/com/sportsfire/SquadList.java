package com.sportsfire;
import java.util.ArrayList;

public class SquadList {
    private ArrayList<Squad> squadList = new ArrayList<Squad>();
    private ArrayList<String> squadNameList = new ArrayList<String>();
    // loads the current SquadList from DB
    public SquadList(){
        Squad sq = new Squad();
        squadList.add(sq);
        squadNameList.add(sq.getSquadName());
    }
    
    public ArrayList<Squad> getSquadList(){
        return squadList;
    }
    
    public ArrayList<String> getSquadNameList(){
        return squadNameList;
        
    }
}