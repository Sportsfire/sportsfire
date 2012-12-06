package com.sportsfire;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Squad implements Parcelable{
    private ArrayList<Player> playerList = new ArrayList<Player>(); // should be made final?
    private ArrayList<String> playerNameList = new ArrayList<String>();
    public Squad(){
        Player pl = new Player();
        playerList.add(pl);
        playerNameList.add(pl.getName());
    }
    
    public ArrayList<Player> getPlayerList(){
        return playerList;
    }
    
    public ArrayList<String> getPlayerNameList(){
        return playerNameList;
    }
    
    public String getSquadName(){
        return "Super Squad";
    }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}