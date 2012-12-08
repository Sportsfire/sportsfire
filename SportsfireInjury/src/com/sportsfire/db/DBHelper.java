package com.example.sportsfireinjury;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DB_VERSION = 1;
	
	// Database Name
    private static final String DB_NAME = "sportsfire";
    
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
    @Override
    public void onCreate(SQLiteDatabase db) {
		SquadTable.onCreate(db);
		PlayerTable.onCreate(db);
		InjuriesTable.onCreate(db);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SquadTable.onUpgrade(db, oldVersion, newVersion);
		PlayerTable.onUpgrade(db, oldVersion, newVersion);
		InjuryTable.onUpgrade(db, oldVersion, newVersion);
    }
}