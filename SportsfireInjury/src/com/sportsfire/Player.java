package com.sportsfire;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.injury.InjuryReportID;
import com.sportsfire.injury.sync.Provider;

public class Player implements Parcelable{
    private String firstName;
    private String lastName;
    private String id;
    private ArrayList<InjuryReportID> injuryReportList = new ArrayList<InjuryReportID>() ;
    private ArrayList<String> injuryReportNameList = new ArrayList<String>() ;
    private Context context;
    
    public Player(String _firstName,String _lastName, String _id, Context context){
        firstName = _firstName;
        lastName = _lastName;
        id = _id;
        this.context = context;
        String[] projection = { InjuryTable.KEY_INJURY_ID, InjuryTable.KEY_DATE_OF_INJURY };
        Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_INJURIES, projection, InjuryTable.KEY_PLAYER_ID+"= '"+id+"'", null, null);
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
        cursor.close();
       
    }
    protected void refresh() throws Exception{
    	if(context == null){
    		throw new Exception("Context in Player Object is null. Maybe parcelable was used, in which case use setContext().");
    	}
    	//when new injury comes in call this method in SquadList to show changes to db
    	injuryReportList.clear();
    	injuryReportNameList.clear();
    	 String[] projection = { InjuryTable.KEY_INJURY_ID, InjuryTable.KEY_DATE_OF_INJURY };
         Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_INJURIES, projection, InjuryTable.KEY_PLAYER_ID+"= '"+id+"'", null, null);
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
         cursor.close();
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
	
	public void setContext(Context context){
		this.context = context;
	}
	
   public static final Parcelable.Creator<Player> CREATOR =
   	new Parcelable.Creator<Player>() {
           public Player createFromParcel(Parcel in) {
               return new Player(in);
           }

           public Player[] newArray(int size) {
               return new Player[size];
           }
       };
}