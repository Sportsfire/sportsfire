package com.sportsfire;

import com.sportsfire.injury.InjuryReportValue;

public class InjuryReportValueString extends InjuryReportValue {
	public String value;
	public InjuryReportValueString(){
		value = "";
	}
    public InjuryReportValueString(String s){
    	value = s;
    }    
}