package com.sportsfire;
import java.util.ArrayList;

public class SquadList {
    private ArrayList<Squad> squadList;
    private ArrayList<String> squadNameList;
    // loads the current SquadList from DB
    public SquadList(){
        squadList = new ArrayList<Squad>();
        squadNameList = new ArrayList<String>();
        
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