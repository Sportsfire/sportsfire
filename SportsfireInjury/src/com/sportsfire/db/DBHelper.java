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
	public void initiateDatabaseWithStubValues(){
		openToWrite();
		String sql = "SELECT COUNT(*) FROM " + SquadTable.TABLE_NAME;
		SQLiteStatement statement = db.compileStatement(sql);
	    if (statement.simpleQueryForLong()>0){
	    	close();
	    	return;
	    }
       // Add squad 1
        ContentValues values = new ContentValues();
        // No need to include squad id, is automatically added
        values.put(SquadTable.KEY_SQUAD_NAME, "Empire");
        Log.e("### Adding a squad", "...");
        insert(SquadTable.TABLE_NAME, null, values);
        String selectSquadData = "SELECT  * FROM " + SquadTable.TABLE_NAME + " WHERE "+SquadTable.KEY_SQUAD_NAME+" = 'Empire';";
        Cursor cursor = readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	String id = cursor.getString(0);

                // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "Darth");
                values.put(PlayerTable.KEY_SURNAME, "Vader");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);
                
             // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "General");
                values.put(PlayerTable.KEY_SURNAME, "Tarkin");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);

                // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "The");
                values.put(PlayerTable.KEY_SURNAME, "Emperor");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);
                
             // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "Boba");
                values.put(PlayerTable.KEY_SURNAME, "Fett");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);
                
                
            	
            } while (cursor.moveToNext());
        }
        
        // Add squad 1
        // No need to include squad id, is automatically added
        values.clear();
        values.put(SquadTable.KEY_SQUAD_NAME, "Rebels");
        Log.e("### Adding a squad", "...");
        insert(SquadTable.TABLE_NAME, null, values);
        selectSquadData = "SELECT  * FROM " + SquadTable.TABLE_NAME + " WHERE "+SquadTable.KEY_SQUAD_NAME+" = 'Rebels';";
        cursor = readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	String id = cursor.getString(0);

                // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "Luke");
                values.put(PlayerTable.KEY_SURNAME, "Skywalker");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);
                

                // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "Han");
                values.put(PlayerTable.KEY_SURNAME, "Solo");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);
                
             // Add a player
                values.clear();
                // No need to include player id, is automatically added
                values.put(PlayerTable.KEY_FIRST_NAME, "Princess");
                values.put(PlayerTable.KEY_SURNAME, "Leia");
                values.put(PlayerTable.KEY_DOB, 19991231);
                values.put(PlayerTable.KEY_SQUAD_ID, id);
                Log.e("### Adding first player", "...");
                insert(PlayerTable.TABLE_NAME, null, values);
                
                
            	
            } while (cursor.moveToNext());
        }
        
        // close the connection
        close();
		
	}
	
    @Override
    public void onCreate(SQLiteDatabase db) {
		SquadTable.onCreate(db);
		PlayerTable.onCreate(db);
		InjuryTable.onCreate(db);
		InjuryUpdateTable.onCreate(db);
		ScreeningValuesTable.onCreate(db);
		ScreeningAverageValuesTable.onCreate(db);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SquadTable.onUpgrade(db, oldVersion, newVersion);
		PlayerTable.onUpgrade(db, oldVersion, newVersion);
		InjuryTable.onUpgrade(db, oldVersion, newVersion);
		InjuryUpdateTable.onUpgrade(db, oldVersion, newVersion);
		ScreeningValuesTable.onUpgrade(db, oldVersion, newVersion);
		ScreeningAverageValuesTable.onUpgrade(db, oldVersion, newVersion);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
    	db.execSQL("PRAGMA foreign_keys = ON");
    }

	public static String getDbName() {
		return DB_NAME;
	}

	
}