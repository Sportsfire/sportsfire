package com.example.sportsfireinjury;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class InjuryTable {
	
    // Table names
	public static final String TABLE_NAME = "injuries";
 
	// Injury Table Keys
    public static final String KEY_INJURY_ID = "_id"; // Primary key
	public static final String KEY_1A = "1a"; 
    public static final String KEY_1B = "1b";
    public static final String KEY_2A = "2a";
    public static final String KEY_2B = "2b";
    public static final String KEY_3 = "3";
    public static final String KEY_3_OTHER = "3other";
    public static final String KEY_4 = "4";
    public static final String KEY_5 = "5";
    public static final String KEY_5_DATE = "5date";
    public static final String KEY_6 = "6";
    public static final String KEY_7 = "7";
    public static final String KEY_8 = "8";
    public static final String KEY_8_OTHER = "8other";
    public static final String KEY_9 = "9";
	public static final String KEY_PLAYER_ID = "playerID"; // Foreign key
    
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
    public static void onCreate(SQLiteDatabase db) {
		String createInjuryTable = "CREATE TABLE " + DB_NAME + "." + TABLE_INJURIES + "("
				+ KEY_INJURY_ID + " INTEGER PRIMARY KEY,"
                + KEY_1A + " INTEGER,"
                + KEY_1B + " INTEGER," 
				+ KEY_2A + " INTEGER,"
				+ KEY_2B + " INTEGER," 
				+ KEY_3 + " TEXT,"
				+ KEY_4 + " TEXT NOT NULL,"
				+ KEY_5 + " INTEGER,"
				+ KEY_5_DATE + " INTEGER,"
				+ KEY_6 + " INTEGER,"
				+ KEY_7 + " INTEGER,"
				+ KEY_8 + " INTEGER,"
				+ KEY_8_OTHER + " TEXT,"
				+ KEY_9 + " INTEGER,"
				+ KEY_PLAYER_ID_FK + " INTEGER," 
				+ "FOREIGN KEY("+ KEY_PLAYER_ID +") REFERENCES " + PlayerTable.TABLE_NAME + "(" + PlayerTable.KEY_PLAYER_ID + ")"
				+ ")";
        
        db.execSQL(createInjuryTable);
   }
 
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(InjuryTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
		
		// Drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INJURIES);
 
        // Create tables again
        onCreate(db);        
	}
}