package com.sportsfire;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{
    private String firstName;
    private String lastName;
    private ArrayList<InjuryReportID> injuryReportList = new ArrayList<InjuryReportID>() ;
    private ArrayList<String> injuryReportNameList = new ArrayList<String>() ;
    public Player(){
        firstName = "Test";
        lastName = "Testman";
        
        InjuryReportID in = new InjuryReportID(0,"Severe Injury");
        
        injuryReportList.add(in);
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

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
    
}