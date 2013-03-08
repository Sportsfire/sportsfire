package com.sportsfire.db;

import android.database.sqlite.*;
import android.util.Log;

public class InjuryUpdateTable {
	
    // Table names
	public static final String TABLE_NAME = "injuryUpdates";
 
	// Injury Table Keys
    public static final String KEY_ID = "_id"; // Primary key
    public static final String KEY_INJURY_ID = "injuryID";
	public static final String KEY_UPDATE_TYPE = "updateType"; // new or update 
    public static final String KEY_DATA = "data"; //serialized field array
    
    public static final String TYPE_NEW = "new";
    public static final String TYPE_UPDATE = "update";
    
    public static void onCreate(SQLiteDatabase db) {
		String createInjuryUpdateTable = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_INJURY_ID + " TEXT DEFAULT '',"
                + KEY_UPDATE_TYPE + " TEXT DEFAULT ''," 
                + KEY_DATA + " TEXT DEFAULT '')";
        db.execSQL(createInjuryUpdateTable);
   }
 
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(InjuryTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
		
		// Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);        
	}
}