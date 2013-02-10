package com.sportsfire;
import java.util.ArrayList;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.PlayerTable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class Season{
    //private ArrayList<Player> playerList = new ArrayList<Player>(); // should be made final?
	private SquadList squadList;
    private ArrayList<String> weekList = new ArrayList<String>();
    private String name;
    private String id;
    public Season(String _name, String _id,DBHelper dbHelp){
        
        name = _name;
        id = _id;
        squadList = new SquadList(dbHelp);
        for (int i = 0; i <= 53; i++) {
			weekList.add("Week " + Integer.toString(i));
		}
        //String selectSquadData = "SELECT  * FROM " + PlayerTable.TABLE_NAME + " WHERE "+PlayerTable.KEY_SQUAD_ID+" = "+_id+";";
        /*Cursor cursor = dbHelp.readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	Player pl = new Player(cursor.getString(1),cursor.getString(2),cursor.getString(0),dbHelp);
                playerList.add(pl);
                playerNameList.add(pl.getName());
            	
            } while (cursor.moveToNext());
        }*/
        
    }
    public SquadList getSquadList(){
        return squadList;
    }
    
    public ArrayList<String> getWeeklist(){
        return weekList;
    }
    
    public String getSeasonName(){
        return name;
    }

    public String getSeasonID(){
        return id;
    }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}