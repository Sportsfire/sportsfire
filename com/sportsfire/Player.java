package com.sportsfire;

public class Player {
    private String firstName;
    private String lastName;
    private ArrayList<InjuryReportID> injuryReportList;
    private ArrayList<String> injuryReportNameList;
    public Player(){
        firstName = "Test"
        lastName = "Testman";
        injuryReports = new ArrayList<InjuryReportsID>();
        injuryReportNameList = new ArrayList<String>();
        
        InjuryReportsID in = new InjuryReportID(0,"Severe Injury");
        
        injuryReports.add(in);
        injuryReportNameList(in.getName());
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
}