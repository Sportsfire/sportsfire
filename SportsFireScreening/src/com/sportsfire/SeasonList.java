package com.sportsfire;
import java.util.ArrayList;

import android.content.Context;

import com.sportsfire.db.DBHelper;

public class SeasonList {
    private ArrayList<Season> seasonList = new ArrayList<Season>();
    private ArrayList<String> seasonNameList = new ArrayList<String>();
    // loads the current SquadList from DB
    public SeasonList(Context context){
    	
    	seasonList.add(new Season("First Season","1",context));
    	seasonList.add(new Season("Second Season","2",context));
    	seasonNameList.add("First Season");
    	seasonNameList.add("Second Season");
    	/*DBHelper dbHelp = new DBHelper(context);
    	dbHelp.openToRead();
        String selectSquadData = "SELECT  * FROM " + SquadTable.TABLE_NAME + ";";
        Cursor cursor = dbHelp.readQuery(selectSquadData, null);
        if (cursor.moveToFirst()) {
            do {
            	Squad sq = new Squad(cursor.getString(1),cursor.getString(0),dbHelp);
                squadList.add(sq);
                squadNameList.add(sq.getSquadName());
            } while (cursor.moveToNext());
        }*/
        //dbHelp.close();
    }
    
    public ArrayList<Season> getSeasonList(){
        return seasonList;
    }
    
    public ArrayList<String> getSeasonNameList(){
        return seasonNameList;
        
    }
}