package com.sportsfire;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;

public class Player implements Parcelable{
    private String firstName;
    private String lastName;
    private String id;
    private ArrayList<InjuryReportID> injuryReportList = new ArrayList<InjuryReportID>() ;
    private ArrayList<String> injuryReportNameList = new ArrayList<String>() ;
    public Player(String _firstName,String _lastName, String _id, DBHelper dbHelp){
        firstName = _firstName;
        lastName = _lastName;
        id = _id;
        
        //InjuryReportID in = new InjuryReportID(0,"Severe Injury");
        String selectSquadData = "SELECT  * FROM " + InjuryTable.TABLE_NAME + " WHERE "+InjuryTable.KEY_PLAYER_ID+" = "+id+";";
        Cursor cursor = dbHelp.readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	InjuryReportID in = new InjuryReportID(cursor.getString(0),cursor.getString(1)	);
            	 injuryReportList.add(in);
                 injuryReportNameList.add(in.getName());
            } while (cursor.moveToNext());
        }
        else{
        	injuryReportList.add(new InjuryReportID("0",""));
        	injuryReportNameList.add("No Injuries!");
        }
       
    }
    protected void refresh(DBHelper dbHelp){
    	//when new injury comes in call this method in SquadList to show changes to db
    	injuryReportList.clear();
    	injuryReportNameList.clear();
    	String selectSquadData = "SELECT  * FROM " + InjuryTable.TABLE_NAME + " WHERE "+InjuryTable.KEY_PLAYER_ID+" = "+id+";";
        Cursor cursor = dbHelp.readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	InjuryReportID in = new InjuryReportID(cursor.getString(0),cursor.getString(1) + " - " + cursor.getString(3));
            	 injuryReportList.add(in);
                 injuryReportNameList.add(in.getName());
            } while (cursor.moveToNext());
        }
        else{
        	injuryReportList.add(new InjuryReportID("0",""));
        	injuryReportNameList.add("No Injuries!");
        }
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
    public String getID(){
    	return id;
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