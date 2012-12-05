package com.sportsfire;
import java.util.ArrayList;
import java.util.ArrayListIterator;

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
    
    public ArrayList<String> getSquadNameList(){
        new ArrayList<String> nameList;
        
        ArrayListIterator<Squad> it = squadList.getIterator();
        
    }
}