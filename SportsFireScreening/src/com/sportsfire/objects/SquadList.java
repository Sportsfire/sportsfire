package com.sportsfire.objects;

import java.util.ArrayList;

import com.sportsfire.db.SquadTable;
import com.sportsfire.unique.Provider;

import android.content.Context;
import android.database.Cursor;

public class SquadList {
	private ArrayList<Squad> squadList = new ArrayList<Squad>();
	private ArrayList<String> squadNameList = new ArrayList<String>();

	public SquadList(Context context) {
		String[] projection = { SquadTable.KEY_SQUAD_NAME, SquadTable.KEY_SQUAD_ID };
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_SQUADS, projection,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				squadList.add(new Squad(cursor.getString(0), cursor.getString(1), context));
				squadNameList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	public ArrayList<Squad> getSquadList() {
		return squadList;
	}

	public ArrayList<String> getSquadNameList() {
		if (squadNameList.size() == 0){
        	squadNameList.add("Error: Didn't load");
        }
		return squadNameList;
	}
}