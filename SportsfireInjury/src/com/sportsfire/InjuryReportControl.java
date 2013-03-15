package com.sportsfire;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.InjuryUpdateTable;
import com.sportsfire.sync.Provider;

public class InjuryReportControl {
	private HashMap<String, String> changedData = new HashMap<String, String>();
	private HashMap<String, String> oldData = new HashMap<String, String>();
	private String playerID;
	private String injuryID;
	boolean newReport;
	private Context context;

	// creates a new injury report for Player p
	public InjuryReportControl(Player p, Context c) {
		Log.e("### Creating new Injury Report", "...");
		newReport = true;
		playerID = p.getID();
		context = c;
	}

	// loads an injury report with ID == formID
	public InjuryReportControl(InjuryReportID formID, Context context) {
		Log.e("###  Loading existing injury report", "...");
		newReport = false;
		playerID = ""; // here we need to set this from DB later

		injuryID = formID.getID();
		this.context = context;
		
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_INJURIES, null, InjuryTable.KEY_INJURY_ID + " = '" + injuryID + "'", null, null);

		if (cursor.moveToFirst()) {
			do {
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					oldData.put(cursor.getColumnName(i), cursor.getString(i));
				}

			} while (cursor.moveToNext());
		}
	}

	private void createNewReport() {

		// Open for Read-Only
		// db = dBHelper.getReadableDatabase();

		ContentValues values = new ContentValues();
		// No need to include squad id, is automatically added
		values.put(InjuryTable.KEY_PLAYER_ID, playerID);
		Log.e("### Adding a new injury", "...");
		injuryID = UUID.randomUUID().toString();
		values.put(InjuryTable.KEY_INJURY_ID, injuryID);
		context.getContentResolver().insert(Provider.CONTENT_URI_INJURIES, values);
		
		values.clear();
		
		values.put(InjuryUpdateTable.KEY_INJURY_ID, injuryID);
		values.put(InjuryUpdateTable.KEY_UPDATE_TYPE,InjuryUpdateTable.TYPE_NEW);
		JSONObject jsonData = new JSONObject();
		try {
			jsonData.put("playerID",playerID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		values.put("data", jsonData.toString());
		context.getContentResolver().insert(Provider.CONTENT_URI_INJURIES_UPDATES, values);
		
		newReport = false;
	}

	public void saveForm() {
		// open db for read / write
		//SQLiteDatabase db = dbHelp.getWritableDatabase();

		if (newReport == true) {
			createNewReport();
		}

		// process changes
		ContentValues values = new ContentValues();
		JSONObject jsonChanges = new JSONObject();
		Iterator<Entry<String,String>> it = changedData.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();

			values.put(pairs.getKey(), pairs.getValue());
			try {
				jsonChanges.put(pairs.getKey(), pairs.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		context.getContentResolver().update(Uri.withAppendedPath(Provider.CONTENT_URI_INJURIES, injuryID), values, null, null);
		
		values.clear();
		values.put(InjuryUpdateTable.KEY_INJURY_ID, injuryID);
		values.put(InjuryUpdateTable.KEY_UPDATE_TYPE, InjuryUpdateTable.TYPE_UPDATE);
		
		String jsonString = jsonChanges.toString();
		values.put(InjuryUpdateTable.KEY_DATA, jsonString);		
		context.getContentResolver().insert(Provider.CONTENT_URI_INJURIES_UPDATES, values);
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