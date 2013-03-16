package com.sportsfire.screening;

import android.content.Context;

import com.sportsfire.db.DBHelper;

public class ScreeningData {

	private DBHelper dbHelp;
	private String seasonID;
	private String week;
	
	public ScreeningData(DBHelper dbHelp,String seasonID,String week){
		this.dbHelp = dbHelp;
		this.seasonID = seasonID;
		this.week = week;
	}
	
	public ScreeningData(Context c,String seasonID, String week){
		this(new DBHelper(c),seasonID,week);
	}
	
	public boolean setValue(String playerID,String valueName,String value){
		return true;
	}
	
	public String getValue(String playerID,String valueName){
		return "1";
	}
	
	public String getAverageValue(String playerID,String valueName){
		return "1";
	}
	
	public String getPreviousValue(String playerID,String valueName){
		return "1";
	}
}
