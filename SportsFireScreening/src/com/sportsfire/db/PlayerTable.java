package com.sportsfire.db;

import android.database.sqlite.*;
import android.util.Log;

public class PlayerTable {
	
	// Table name
	public static final String TABLE_NAME = "players";
 
	// Players Table Keys
    public static final String KEY_PLAYER_ID = "_id"; // Primary key
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_DOB = "dateOfBirth";
    public static final String KEY_SQUAD_ID = "squadID"; // Foreign key 
	
    public static void onCreate(SQLiteDatabase db) {
		String createPlayerTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_PLAYER_ID + " INTEGER PRIMARY KEY," 
				+ KEY_FIRST_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT," 
				+ KEY_DOB + " INTEGER NOT NULL," 
                + KEY_SQUAD_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY("+ KEY_SQUAD_ID +") REFERENCES " + SquadTable.TABLE_NAME + "(" + SquadTable.KEY_SQUAD_ID + ")"
				+ ")";
        
		db.execSQL(createPlayerTable);
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