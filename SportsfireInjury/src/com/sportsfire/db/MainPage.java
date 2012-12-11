package com.sportsfire.db;


import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;

public class MainPage extends Activity {
	
	private DBHelper dbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main_page);
        
        // ### To test SQLiteDatabase functionality ###
      	   
        SQLiteDatabase db;
        dbHelper = new DBHelper(this);
        
        // Open for Read/Write
        db = dbHelper.getWritableDatabase();
        
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
        dbHelper.close();
        
        // re-open connection (to test)
        db = dbHelper.getWritableDatabase();
        
        // Add another player
        values.clear();
        values.put(PlayerTable.KEY_FIRST_NAME, "Neil");
        values.put(PlayerTable.KEY_SURNAME, "Neilson");
        values.put(PlayerTable.KEY_DOB, 20031025);
        values.put(PlayerTable.KEY_SQUAD_ID, 1);
        Log.e("### Adding second player", "...");
        db.insert(PlayerTable.TABLE_NAME, null, values);
        
        // Print Squad Table
        String selectSquadData = "SELECT  * FROM " + SquadTable.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	String id = cursor.getString(0);
            	String squadName = cursor.getString(1);
                               
                Log.e("### Squad Data: ", id + "|" + squadName);
            } while (cursor.moveToNext());
        }
        
        // Print Player Table
        String selectPlayerData = "SELECT  * FROM " + PlayerTable.TABLE_NAME + ";";
        cursor = db.rawQuery(selectPlayerData, null);
        if (cursor.moveToFirst()) {
            do {
            	String id = cursor.getString(0);
            	String firstName = cursor.getString(1);
                String surname = cursor.getString(2);
                String dateOfBirth = cursor.getString(3);
                String squad = cursor.getString(4);
                
                Log.e("### Player Data: ", id + "|" + firstName + "|" + surname+ "|" + dateOfBirth+ "|" + squad);
            } while (cursor.moveToNext());
        }
        
//        db.delete(SquadTable.TABLE_NAME, null, null);
//        db.delete(PlayerTable.TABLE_NAME, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
   //     getMenuInflater().inflate(R.menu.activity_main_page, menu);
        return true;
    }
}