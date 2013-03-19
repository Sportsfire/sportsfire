package com.sportsfire;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.sync.Provider;

public class Squad implements Parcelable{
    private ArrayList<Player> playerList = new ArrayList<Player>(); // should be made final?
    private ArrayList<String> playerNameList = new ArrayList<String>();
    private String name;
    private String id;
    private Context context;
    public Squad(String _name, String _id,Context context){
        this.context= context;
        name = _name;
        id = _id;
        String[] projection = { PlayerTable.KEY_FIRST_NAME, PlayerTable.KEY_SURNAME, PlayerTable.KEY_PLAYER_ID };
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_PLAYERS, projection, PlayerTable.KEY_SQUAD_ID + " = '" + _id + "'", null, null);
		if (cursor.moveToFirst()) {
            do {
            	Player pl = new Player(cursor.getString(0),cursor.getString(1),cursor.getString(2),context);
                playerList.add(pl);
                playerNameList.add(pl.getName());
            } while (cursor.moveToNext());
        }
		cursor.close();
        
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