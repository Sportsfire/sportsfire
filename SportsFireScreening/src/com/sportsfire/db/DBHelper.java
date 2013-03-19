package com.sportsfire.db;


import com.sportsfire.Player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DB_VERSION = 4;
	
	// Database Name
    private static final String DB_NAME = "sportsfire";
    
    private SQLiteDatabase db = null;
    
	public DBHelper(Context context) {
		super(context, getDbName(), null, DB_VERSION);
	}
	
	public void openToRead(){
		close();		
		db = this.getReadableDatabase();
	}
	
	public void openToWrite(){
		close();
		db = this.getWritableDatabase();
	}
	
	public void close(){
		if(db != null){
			super.close();
			db = null;
		}
			
	}
	public void deleteDatabase(){
		db.delete(getDbName(), null, null); 
	}
	public long insert(String table,String nullColumnHack,ContentValues values){
		return db.insert(table, nullColumnHack, values);
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return db.update(table, values,whereClause,whereArgs);
	}
	
	public Cursor readQuery(String sql, String[] selectionArgs){
		return db.rawQuery(sql, selectionArgs);
	}
	
	
	/*
	 * This is just a temporary method to initialize the DB with stub values if needed
	 */
	
	
    @Override
    public void onCreate(SQLiteDatabase db) {
		SquadTable.onCreate(db);
		PlayerTable.onCreate(db);
		ScreeningValuesTable.onCreate(db);
		ScreeningUpdatesTable.onCreate(db);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SquadTable.onUpgrade(db, oldVersion, newVersion);
		PlayerTable.onUpgrade(db, oldVersion, newVersion);
		ScreeningValuesTable.onUpgrade(db, oldVersion, newVersion);
		ScreeningUpdatesTable.onUpgrade(db, oldVersion, newVersion);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
    	db.execSQL("PRAGMA foreign_keys = ON");
    }

	public static String getDbName() {
		return DB_NAME;
	}

	
}