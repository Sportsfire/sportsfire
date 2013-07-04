package com.sportsfire.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

public class ScreeningUpdatesTable {
	
	// Table name
	public static final String TABLE_NAME = "screeningupdates";
 
	// Players Table Keys
    public static final String KEY_ID = "_id"; // Primary key
    public static final String KEY_VALUE_ID = "valueid"; 

    public static void onCreate(SQLiteDatabase db) {
		String createValuesTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ KEY_VALUE_ID + " INTEGER"
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