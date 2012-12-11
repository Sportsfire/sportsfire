package com.sportsfire;
import java.util.ArrayList;

import com.sportsfire.db.PlayerTable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class Squad implements Parcelable{
    private ArrayList<Player> playerList = new ArrayList<Player>(); // should be made final?
    private ArrayList<String> playerNameList = new ArrayList<String>();
    private String name;
    private int id;
    public Squad(String _name, String _id,SQLiteDatabase db){
        
        name = _name;
        
        String selectSquadData = "SELECT  * FROM " + PlayerTable.TABLE_NAME + " WHERE "+PlayerTable.KEY_SQUAD_ID+" = "+_id+";";
        Cursor cursor = db.rawQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	Player pl = new Player(cursor.getString(1),cursor.getString(2),cursor.getString(0),db);
                playerList.add(pl);
                playerNameList.add(pl.getName());
            	
            } while (cursor.moveToNext());
        }
        
    }
    
    public ArrayList<Player> getPlayerList(){
        return playerList;
    }
    
    public ArrayList<String> getPlayerNameList(){
        return playerNameList;
    }
    
    public String getSquadName(){
        return name;
    }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}