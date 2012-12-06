package com.sportsfire;

public class InjuryReportID extends InjuryReportValue{
    int id;
    String name;
    
    public InjuryReportID(int _id, String _name){
        id = _id;
        name = _name;
    }
    
    public int getID(){
        return id;
    }
    
    public String getName(){
        return name;
    }

}