package com.sportsfire.screening;

import java.util.LinkedList;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sportsfire.Player;
import com.sportsfire.db.DBHelper;
import com.sportsfire.db.ScreeningUpdatesTable;
import com.sportsfire.db.ScreeningValuesTable;
import com.sportsfire.sync.Provider;

public class ScreeningData {

	private String seasonID;
	private String week;
	private ContentResolver content;
	public ScreeningData(Context context,String seasonID,String week){
		content = context.getContentResolver();
		this.seasonID = seasonID;
		this.week = week;
	}
	
	private String getWhere(String playerID,String measurementType,String week){
		final String where = ScreeningValuesTable.KEY_PLAYER_ID + " = '" + playerID + "' and " +
				ScreeningValuesTable.KEY_SEASON_ID + " = '" + seasonID + "' and " + 
				ScreeningValuesTable.KEY_WEEK + " = '" + week + "' and " +
				ScreeningValuesTable.KEY_MEASUREMENT_TYPE + " = '" + measurementType + "'";
		
		return where;
	}
	
	private String getAvgWhere(String playerID,String measurementType){
		final String where = ScreeningValuesTable.KEY_PLAYER_ID + " = '" + playerID + "' and " +
				ScreeningValuesTable.KEY_SEASON_ID + " = '" + seasonID + "' and " + 
				ScreeningValuesTable.KEY_MEASUREMENT_TYPE + " = '" + measurementType + "'";
		
		return where;
	}
	
	
	public boolean setValue(String playerID,String measurementType,String value){
		if(value == "") 
			return false;
		try{
			Double.parseDouble(value);
		}
		catch(NumberFormatException e){
			return false;
		}
		
		String where = getWhere(playerID,measurementType, week);
		
		String[] projection = { ScreeningValuesTable.KEY_VALUE, ScreeningValuesTable.KEY_ID };
		Cursor cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where, null, null);
		
		ContentValues values = new ContentValues();
		
		if(cursor.getCount() == 0){
			values.put(ScreeningValuesTable.KEY_MEASUREMENT_TYPE, measurementType);
			values.put(ScreeningValuesTable.KEY_PLAYER_ID, playerID);
			values.put(ScreeningValuesTable.KEY_SEASON_ID, seasonID);
			values.put(ScreeningValuesTable.KEY_VALUE, value);
			values.put(ScreeningValuesTable.KEY_WEEK, week);
			Uri uri = content.insert(Provider.CONTENT_URI_SCREENING_VALUES, values);
			String insertID = uri.getLastPathSegment();
			
			values.clear();
			values.put(ScreeningUpdatesTable.KEY_VALUE_ID, Integer.parseInt(insertID));
			
			content.insert(Provider.CONTENT_URI_SCREENING_UPDATES, values);
			
		} else {
			values.put(ScreeningValuesTable.KEY_VALUE, value);
			
			cursor.moveToFirst();
			
			content.update(Provider.CONTENT_URI_SCREENING_VALUES, values, where, null);
			
			values.clear();
			values.put(ScreeningUpdatesTable.KEY_VALUE_ID, cursor.getInt(1));
			content.insert(Provider.CONTENT_URI_SCREENING_UPDATES, values);
			
		}
		
		cursor.close();
		
		return true;
	}
	
	
	

	public String getValue(String playerID,String measurementType){
		String where = getWhere(playerID,measurementType, week);
		
		String[] projection = { ScreeningValuesTable.KEY_VALUE };
		Cursor cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where, null, null);
		
		if(cursor.getCount() == 0){
			return "";
		}
		else{
			cursor.moveToFirst();
			String result = cursor.getString(0);
			double number = Double.parseDouble(result);
			
			cursor.close();
			return String.format("%1$.2f", number);
		}
	}
	
	public String getAverageValue(String playerID,String measurementType){
		String where = getAvgWhere(playerID,measurementType);
		
		String[] projection = { ScreeningValuesTable.KEY_VALUE };
		Cursor cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where, null, null);
		
		if(cursor.getCount() == 0){
			return "";
		}
		else{
			Double values = 0d;
			if(cursor.moveToFirst()){
				do{
					try{
						values = values + Double.parseDouble(cursor.getString(0));
					}
					catch(NumberFormatException e){
					}
				} while(cursor.moveToNext());
			}
			
			values /= cursor.getCount();
			
			cursor.close();
			return String.format("%1$.2f", values);
		}
	}
	
	

	public String getPreviousValue(String playerID,String measurementType){
		int lastweek = Integer.parseInt(week);
		if(lastweek > 0){
			lastweek--; // this needs to be changed for monthly
			
			String where = getWhere(playerID,measurementType,Integer.toString(lastweek));
			
			
			String[] projection = { ScreeningValuesTable.KEY_VALUE };
			Cursor cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where, null, null);
			
			if(cursor.getCount() == 0){
				return "";
			}
			else{
				cursor.moveToFirst();
				String result = cursor.getString(0);
				cursor.close();
				return result;
			}
		}
		
		return "";		
	}
}
