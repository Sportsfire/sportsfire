package com.sportsfire;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.sportsfireinjury.R;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class InjuryReportControl {
	private HashMap<String, String> changedData = new HashMap<String, String>();
	private HashMap<String, String> oldData = new HashMap<String, String>();
	private Context context;
	private String playerID;
	private String injuryID;
	boolean newReport;

	// creates a new injury report for Player p
	public InjuryReportControl(Player p, Context c) {
		Log.e("### Creating new Injury Report", "...");
		newReport = true;
		playerID = p.getID();
		context = c;
	}

	// loads an injury report with ID == formID
	public InjuryReportControl(InjuryReportID formID, Context c) {
		Log.e("###  Loading existing injury report", "...");
		newReport = false;
		context = c;
		playerID = ""; // here we need to set this from DB later

		injuryID = formID.getID();

		// open db for read / write
		DBHelper dbHelp = new DBHelper(context);
		SQLiteDatabase db = dbHelp.getReadableDatabase();

		String selectSquadData = "SELECT  * FROM " + InjuryTable.TABLE_NAME + " WHERE "
				+ InjuryTable.KEY_INJURY_ID + " = " + injuryID + ";";
		Cursor cursor = db.rawQuery(selectSquadData, null);
		if (cursor.moveToFirst()) {
			do {
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					oldData.put(cursor.getColumnName(i), cursor.getString(i));
				}

			} while (cursor.moveToNext());
		}
		dbHelp.close();
	}

	private void createNewReport(SQLiteDatabase db) {

		// Open for Read-Only
		// db = dBHelper.getReadableDatabase();

		ContentValues values = new ContentValues();
		// No need to include squad id, is automatically added
		values.put(InjuryTable.KEY_PLAYER_ID, playerID);
		Log.e("### Adding a new injury", "...");
		long rowID = db.insert(InjuryTable.TABLE_NAME, null, values);

		/*
		 * String getInjuryID = "SELECT * FROM " + InjuryTable.TABLE_NAME +
		 * " WHERE "+InjuryTable.KEY_PLAYER_ID+" = '"+playerID+"';"; Cursor
		 * cursor = db.rawQuery(getInjuryID, null); if (cursor.moveToFirst()) {
		 * injuryID = cursor.getString(0); }
		 */
		injuryID = String.valueOf(rowID);
		newReport = false;
	}

	public void saveForm() {
		// open db for read / write
		DBHelper dbHelp = new DBHelper(context);
		SQLiteDatabase db = dbHelp.getWritableDatabase();

		if (newReport == true) {
			createNewReport(db);
		}

		// process changes
		ContentValues values = new ContentValues();
		Iterator it = changedData.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();

			values.put(pairs.getKey(), pairs.getValue());

		}
		db.update(InjuryTable.TABLE_NAME, values, InjuryTable.KEY_INJURY_ID + " = '" + injuryID
				+ "'", null);
	}

	private String getStandardValue(String field) {
		if (field.equals(InjuryTable.KEY_DATE_OF_INJURY)
				|| field.equals(InjuryTable.KEY_DATE_OF_RETURN)
				|| field.equals(InjuryTable.KEY_PREVIOUS_DATE)
				|| field.equals(InjuryTable.KEY_DIAGNOSIS)
				|| field.equals(InjuryTable.KEY_INJURED_BODY_PART)) {
			return "";
		}
		return "0";
	}
	private String idToField(int id) {
		switch (id) {
			case R.id.ir1a :
				return InjuryTable.KEY_DATE_OF_INJURY;
			case R.id.ir1b :
				return InjuryTable.KEY_DATE_OF_RETURN;
			default :
				return "";
		}
	}
	
	public String getOrchardCode(){
		return getValue(InjuryTable.KEY_ORCHARD);
	}
	
	public void setOrchardCode(String orchardCode){
		setValue(InjuryTable.KEY_ORCHARD,orchardCode);
	}
	
	// maybe replace field name String with enum?
	public String getValue(String field) {
		String result = changedData.get(field);

		if (result != null)
			return result;

		if (newReport) {
			return getStandardValue(field);
		}

		result = oldData.get(field);

		if (result == null) {
			result = "";
			Log.e("getValueError", field);
		}

		return result;
	}

	public void setValue(String field, String value) {
		changedData.put(field, value);
	}

}