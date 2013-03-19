package com.sportsfire.db;


import android.database.sqlite.*;
import android.util.Log;

public class SquadTable {
	
	// Table name
	public static final String TABLE_NAME = "squads";
 
	// Squad Table Keys
	public static final String KEY_SQUAD_ID = "_id"; // Primary key
	public static final String KEY_SQUAD_NAME = "squadName"; 
	
    public static void onCreate(SQLiteDatabase db) {
		String createSquadTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_SQUAD_ID + " INTEGER PRIMARY KEY NOT NULL," 
				+ KEY_SQUAD_NAME + " TEXT UNIQUE NOT NULL" 
				+ ")";
        
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