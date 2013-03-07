package com.sportsfire;
import java.util.ArrayList;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.PlayerTable;

public class Squad implements Parcelable{
    private ArrayList<Player> playerList = new ArrayList<Player>(); // should be made final?
    private ArrayList<String> playerNameList = new ArrayList<String>();
    private String name;
    private String id;
    public Squad(String _name, String _id,DBHelper dbHelp){
        
        name = _name;
        id = _id;
        String selectSquadData = "SELECT  * FROM " + PlayerTable.TABLE_NAME + " WHERE "+PlayerTable.KEY_SQUAD_ID+" = "+_id+";";
        Cursor cursor = dbHelp.readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	Player pl = new Player(cursor.getString(1),cursor.getString(2),cursor.getString(0),dbHelp);
                playerList.add(pl);
                playerNameList.add(pl.getName());
            	
            } while (cursor.moveToNext());
        }
        
    }
    protected void refresh(DBHelper dbHelp){
    	//refresh the players
    	 for (Player player : playerList){
    		 player.refresh(dbHelp);
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