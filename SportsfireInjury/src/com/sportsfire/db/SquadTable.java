package com.example.sportsfireinjury;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class SquadTable {
	
	// Table name
	public static final String TABLE_NAME = "squads";
 
	// Squad Table Keys
	public static final String KEY_SQUAD_ID = "_id"; // Primary key
	public static final String KEY_SQUAD_NAME = "squadName"; 
    
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
    public static void onCreate(SQLiteDatabase db) {
		String createSquadTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_SQUAD_ID + " INTEGER PRIMARY KEY," 
				+ KEY_SQUAD_NAME + " TEXT" 
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