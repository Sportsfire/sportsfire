package com.sportsfire;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{
    private String firstName;
    private String lastName;
    private String id;
    private ArrayList<InjuryReportID> injuryReportList = new ArrayList<InjuryReportID>() ;
    private ArrayList<String> injuryReportNameList = new ArrayList<String>() ;
    public Player(String _firstName,String _lastName, String _id, SQLiteDatabase db){
        firstName = _firstName;
        lastName = _lastName;
        id = _id;
        
        InjuryReportID in = new InjuryReportID(0,"Severe Injury");
        
        injuryReportList.add(in);
        injuryReportNameList.add(in.getName());
    }
    
    public Player(Parcel in){
    	readFromParcel(in);
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
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(id);
	}
	
	private void readFromParcel(Parcel in){
		firstName = in.readString();
		lastName = in.readString();
		id = in.readString();
	}
	
   public static final Parcelable.Creator CREATOR =
   	new Parcelable.Creator() {
           public Player createFromParcel(Parcel in) {
               return new Player(in);
           }

           public Player[] newArray(int size) {
               return new Player[size];
           }
       };
}