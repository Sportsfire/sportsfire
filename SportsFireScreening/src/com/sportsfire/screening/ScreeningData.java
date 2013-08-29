package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.Locale;

import org.achartengine.util.MathHelper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sportsfire.db.PlayerTable;
import com.sportsfire.screening.db.ScreeningUpdatesTable;
import com.sportsfire.screening.db.ScreeningValuesTable;
import com.sportsfire.unique.Provider;

public class ScreeningData {

	private String seasonID;
	private String week;
	private ContentResolver content;
	private Cursor cursor;
	
	public ScreeningData(Context context, String seasonID, String week) {
		content = context.getContentResolver();
		this.seasonID = seasonID;
		this.week = week;
	}

	public ScreeningData(Context context, String seasonID) {
		content = context.getContentResolver();
		this.seasonID = seasonID;
	}
	
	public void closeCursor(){
		try {
			cursor.close();
		} catch (Exception e) {
		}
	}
	
	public ArrayList<Double> getPlayerSeasonData(String playerID, String measurementType) {
		ArrayList<Double> data = new ArrayList<Double>();
		String where = ScreeningValuesTable.KEY_PLAYER_ID + " = '" + playerID + "' and "
				+ ScreeningValuesTable.KEY_SEASON_ID + " = '" + seasonID + "' and "
				+ ScreeningValuesTable.KEY_MEASUREMENT_TYPE + " = '" + measurementType + "'";
		String[] projection = { ScreeningValuesTable.KEY_VALUE };
		cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where,
				null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return new ArrayList<Double>();
		} else {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				try {
					data.add(cursor.getDouble(0));
				} catch (Exception e) {
					data.add(MathHelper.NULL_VALUE);
				}

			}
		}
		cursor.close();
		return data;
	}

	public double getPlayerSquadAverageSeasonData(String playerID, String measurementType,
			String week) {
		double data = MathHelper.NULL_VALUE;
		String where = ScreeningValuesTable.KEY_SEASON_ID + " = '" + seasonID + "' and "
				+ ScreeningValuesTable.KEY_PLAYER_ID + " IN (SELECT " + PlayerTable.KEY_PLAYER_ID
				+ " FROM " + PlayerTable.TABLE_NAME + " WHERE " + PlayerTable.KEY_SQUAD_ID
				+ " = (SELECT " + PlayerTable.KEY_SQUAD_ID + " FROM " + PlayerTable.TABLE_NAME
				+ " WHERE " + PlayerTable.KEY_PLAYER_ID + " = '" + playerID + "'))" + " and "
				+ ScreeningValuesTable.KEY_WEEK + " = '" + week + "' and "
				+ ScreeningValuesTable.KEY_MEASUREMENT_TYPE + " = '" + measurementType + "'";
		String[] projection = { "AVG(" + ScreeningValuesTable.KEY_VALUE + ")" };
		cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where,
				null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return data;
		} else {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				try {
					data = cursor.getDouble(0);
				} catch (Exception e) {
				}

			}
		}
		cursor.close();
		return data;
	}

	private String getWhere(String playerID, String measurementType, String week) {
		final String where = ScreeningValuesTable.KEY_PLAYER_ID + " = '" + playerID + "' and "
				+ ScreeningValuesTable.KEY_SEASON_ID + " = '" + seasonID + "' and "
				+ ScreeningValuesTable.KEY_WEEK + " = '" + week + "' and "
				+ ScreeningValuesTable.KEY_MEASUREMENT_TYPE + " = '" + measurementType + "'";

		return where;
	}

	private String getAvgWhere(String playerID, String measurementType) {
		final String where = ScreeningValuesTable.KEY_PLAYER_ID + " = '" + playerID + "' and "
				+ ScreeningValuesTable.KEY_SEASON_ID + " = '" + seasonID + "' and "
				+ ScreeningValuesTable.KEY_MEASUREMENT_TYPE + " = '" + measurementType + "'";

		return where;
	}

	public boolean setValue(String playerID, String measurementType, String value) {
		if (value == "") return false;
		try {
			Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}

		String where = getWhere(playerID, measurementType, week);

		String[] projection = { ScreeningValuesTable.KEY_VALUE, ScreeningValuesTable.KEY_ID };
		cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where,
				null, null);

		ContentValues values = new ContentValues();

		if (cursor.getCount() == 0) {
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

	public String getValue(String playerID, String measurementType) {
		String where = getWhere(playerID, measurementType, week);
		String[] projection = { ScreeningValuesTable.KEY_VALUE };
		cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where,
				null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return "";
		} else {
			cursor.moveToFirst();
			String result = cursor.getString(0);
			double number = Double.parseDouble(result);

			cursor.close();
			return String.format(Locale.UK,"%1$.2f", number);
		}
	}

	public String getAverageValue(String playerID, String measurementType) {
		String where = getAvgWhere(playerID, measurementType);

		String[] projection = { ScreeningValuesTable.KEY_VALUE };
		cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where,
				null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return "";
		} else {
			Double values = 0d;
			if (cursor.moveToFirst()) {
				do {
					try {
						values = values + Double.parseDouble(cursor.getString(0));
					} catch (NumberFormatException e) {
					}
				} while (cursor.moveToNext());
			}

			values /= cursor.getCount();

			cursor.close();
			return String.format(Locale.UK, "%1$.2f", values);
		}
	}

	public String getPreviousValue(String playerID, String measurementType) {
		int lastweek = Integer.parseInt(week);
		if (lastweek > 0) {
			lastweek--; // this needs to be changed for monthly

			String where = getWhere(playerID, measurementType, Integer.toString(lastweek));

			String[] projection = { ScreeningValuesTable.KEY_VALUE };
			cursor = content.query(Provider.CONTENT_URI_SCREENING_VALUES, projection, where,
					null, null);

			if (cursor.getCount() == 0) {
				cursor.close();
				return "";
			} else {
				cursor.moveToFirst();
				String result = cursor.getString(0);
				cursor.close();
				return result;
			}
		}

		return "";
	}
}
