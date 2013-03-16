package com.sportsfire.db;

import android.database.sqlite.*;
import android.util.Log;

public class ScreeningAverageValuesTable {
	
	// Table name
	public static final String TABLE_NAME = "screeningaveragevalues";
 
	// Players Table Keys
    public static final String KEY_ID = "_id"; // Primary key
    public static final String KEY_MEASUREMENT_TYPE = "type"; // weight, etc...
    public static final String KEY_AVERAGE = "average";
    public static final String KEY_NUM_MEASUREMENTS = "numberofmeasurements";
    public static final String KEY_SEASON_ID = "seasonid"; 
    public static final String KEY_PLAYER_ID = "playerid";
	
    public static void onCreate(SQLiteDatabase db) {
		String createValuesTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ KEY_MEASUREMENT_TYPE + " TEXT,"
                + KEY_AVERAGE + " TEXT,"
                + KEY_NUM_MEASUREMENTS + " INTEGER," 
                + KEY_SEASON_ID + " INTEGER NOT NULL,"
                + KEY_PLAYER_ID + " INTEGER NOT NULL"
				+ ")";
        
		db.execSQL(createValuesTable);
    }
 
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PlayerTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
		
		// Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);        
	}
}