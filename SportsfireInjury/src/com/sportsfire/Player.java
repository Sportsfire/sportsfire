package com.sportsfire;

import java.util.ArrayList;

public class Player {
    private String firstName;
    private String lastName;
    private ArrayList<InjuryReportID> injuryReportList;
    private ArrayList<String> injuryReportNameList;
    public Player(){
        firstName = "Test";
        lastName = "Testman";
        ArrayList<InjuryReportID> injuryReports = new ArrayList<InjuryReportID>();
        injuryReportNameList = new ArrayList<String>();
        
        InjuryReportID in = new InjuryReportID(0,"Severe Injury");
        
        injuryReports.add(in);
        injuryReportNameList.add(in.getName());
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public ArrayList<InjuryReportID> getInjuryReportList(){
        return injuryReportList;
    }
    
    public ArrayList<String> getInjuryReportNameList(){
        return injuryReportNameList;
    }
    public String getName(){
        return lastName + ", " + firstName;
    }
    
}