package com.sportsfire;

public class InjuryReportControl {
    
    // creates a new injury report for Player p
    public InjuryReportControl(Player p){
        
    }
    
    // loads an injur report with ID == formID
    public InjuryReportControl(InjuryReportID formID){
        
    }
    
    public void saveForm(){
        
    }
    
    // maybe replace field name String with enum?
    public InjuryReportValue getValue(String field){
        return new InjuryReportValueBoolean(false);
    }
    
    public void setValue(String field,InjuryReportValue value){
        
    }
}