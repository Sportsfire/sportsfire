package com.sportsfire;

public class Player {
    private String firstName;
    private String lastName;
    private ArrayList<InjuryReportID> injuryReports;
    public Player(){
        firstName = "Test"
        lastName = "Testman";
        injuryReports = new ArrayList<InjuryReportsID>();
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
}