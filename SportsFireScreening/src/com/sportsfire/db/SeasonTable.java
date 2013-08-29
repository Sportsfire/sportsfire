package com.sportsfire.db;


import android.database.sqlite.*;
import android.util.Log;

public class SeasonTable {
	
	// Table name
	public static final String TABLE_NAME = "seasons";

	// Season Table Keys
	public static final String KEY_SEASON_ID = "_id"; // Primary key
	public static final String KEY_SEASON_NAME = "seasonname";
	public static final String KEY_START_DATE = "startdate";
	
	public static void onCreate(SQLiteDatabase db) {
		String createSquadTable = "CREATE TABLE " + TABLE_NAME + 
				"(" + KEY_SEASON_ID + " INTEGER PRIMARY KEY NOT NULL,"
				+ KEY_SEASON_NAME + " TEXT NOT NULL," 
				+ KEY_START_DATE + " TEXT" + ")";

		db.execSQL(createSquadTable);
	}
 
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SquadTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
		
		// Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);        
	}
}