package com.sportsfire;
import java.util.ArrayList;
import com.sportsfire.db.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SquadList {
    private ArrayList<Squad> squadList = new ArrayList<Squad>();
    private ArrayList<String> squadNameList = new ArrayList<String>();
    private DBHelper dbHelp;
    // loads the current SquadList from DB
    
    public SquadList(DBHelper dbHelp){
    	this.dbHelp = dbHelp;
    	dbHelp.openToRead();
        String selectSquadData = "SELECT  * FROM " + SquadTable.TABLE_NAME + ";";
        Cursor cursor = dbHelp.readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	Squad sq = new Squad(cursor.getString(1),cursor.getString(0),dbHelp);
                squadList.add(sq);
                squadNameList.add(sq.getSquadName());
            } while (cursor.moveToNext());
        }
        dbHelp.close();
    }
    public SquadList(Context context){
    	this(new DBHelper(context));
    }
    
    public void refresh(){
    	//refresh the squads
    	dbHelp.openToRead();
    	for (Squad squad: squadList){
    		squad.refresh(dbHelp);
    	}
        dbHelp.close();
    }
    public ArrayList<Squad> getSquadList(){
        return squadList;
    }
    
    public ArrayList<String> getSquadNameList(){
        return squadNameList;
        
    }
}