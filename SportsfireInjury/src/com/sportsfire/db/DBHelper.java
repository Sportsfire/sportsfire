package com.sportsfire.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DB_VERSION = 4;
	
	// Database Name
    private static final String DB_NAME = "sportsfire";
    
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
		SQLiteDatabase db;
 
        
        // Open for Read/Write
        db = this.getWritableDatabase();
        
        // Open for Read-Only
        //db = dBHelper.getReadableDatabase();
        
        // Add a squad
        ContentValues values = new ContentValues();
        // No need to include squad id, is automatically added
        values.put(SquadTable.KEY_SQUAD_NAME, "First Team");
        Log.e("### Adding a squad", "...");
        db.insert(SquadTable.TABLE_NAME, null, values);
        
        // Add a player
        values.clear();
        // No need to include player id, is automatically added
        values.put(PlayerTable.KEY_FIRST_NAME, "Baron");
        values.put(PlayerTable.KEY_SURNAME, "Baronson");
        values.put(PlayerTable.KEY_DOB, 19991231);
        values.put(PlayerTable.KEY_SQUAD_ID, 0);
        Log.e("### Adding first player", "...");
        db.insert(PlayerTable.TABLE_NAME, null, values);
        
        // close the connection
        this.close();
	}
	
    @Override
    public void onCreate(SQLiteDatabase db) {
		SquadTable.onCreate(db);
		PlayerTable.onCreate(db);
		InjuryTable.onCreate(db);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SquadTable.onUpgrade(db, oldVersion, newVersion);
		PlayerTable.onUpgrade(db, oldVersion, newVersion);
		InjuryTable.onUpgrade(db, oldVersion, newVersion);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
    	db.execSQL("PRAGMA foreign_keys = ON");
    }
}