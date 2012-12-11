package com.sportsfire.db;

import android.database.sqlite.*;
import android.util.Log;

public class InjuryTable {
	
    // Table names
	public static final String TABLE_NAME = "injuries";
 
	// Injury Table Keys
    public static final String KEY_INJURY_ID = "_id"; // Primary key
	public static final String KEY_1A = "input1a"; 
    public static final String KEY_1B = "input1b";
    public static final String KEY_2A = "input2a";
    public static final String KEY_2B = "input2b";
    public static final String KEY_3 = "input3";
    public static final String KEY_3_OTHER = "input3other";
    public static final String KEY_4 = "input4";
    public static final String KEY_5 = "input5";
    public static final String KEY_5_DATE = "input5date";
    public static final String KEY_6 = "input6";
    public static final String KEY_7 = "input7";
    public static final String KEY_8 = "input8";
    public static final String KEY_8_OTHER = "input8other";
    public static final String KEY_9 = "input9";
	public static final String KEY_PLAYER_ID = "playerID"; // Foreign key
	
    public static void onCreate(SQLiteDatabase db) {
		String createInjuryTable = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_INJURY_ID + " INTEGER PRIMARY KEY,"
                + KEY_1A + " INTEGER NOT NULL,"
                + KEY_1B + " INTEGER," 
				+ KEY_2A + " INTEGER NOT NULL,"
				+ KEY_2B + " INTEGER NOT NULL," 
				+ KEY_3 + " INTEGER NOT NULL,"
				+ KEY_3_OTHER + " TEXT"
				+ KEY_4 + " TEXT NOT NULL,"
				+ KEY_5 + " INTEGER NOT NULL,"
				+ KEY_5_DATE + " INTEGER,"
				+ KEY_6 + " INTEGER NOT NULL,"
				+ KEY_7 + " INTEGER NOT NULL,"
				+ KEY_8 + " INTEGER NOT NULL,"
				+ KEY_8_OTHER + " TEXT,"
				+ KEY_9 + " INTEGER,"
				+ KEY_PLAYER_ID + " INTEGER NOT NULL,"
				+ "FOREIGN KEY("+ KEY_PLAYER_ID +") REFERENCES " + PlayerTable.TABLE_NAME + "(" + PlayerTable.KEY_PLAYER_ID + ")"
				+ ")";
        
        db.execSQL(createInjuryTable);
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