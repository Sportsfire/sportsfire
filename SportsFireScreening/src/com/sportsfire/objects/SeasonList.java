package com.sportsfire.objects;
import java.util.ArrayList;

import com.sportsfire.db.SeasonTable;
import com.sportsfire.unique.Provider;

import android.content.Context;
import android.database.Cursor;

public class SeasonList {
    private ArrayList<Season> seasonList = new ArrayList<Season>();
    private ArrayList<String> seasonNameList = new ArrayList<String>();
    // loads the current SquadList from DB
    public SeasonList(Context context){
    	
    	String[] projection = { SeasonTable.KEY_SEASON_NAME, SeasonTable.KEY_SEASON_ID, SeasonTable.KEY_START_DATE};
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_SEASONS, projection,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				seasonList.add(new Season(cursor.getString(0), cursor.getString(1),cursor.getString(2)));
				seasonNameList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
    
    }
    
    public ArrayList<Season> getSeasonList(){
        return seasonList;
    }
    
    public ArrayList<String> getSeasonNameList(){
        return seasonNameList;
    }
}