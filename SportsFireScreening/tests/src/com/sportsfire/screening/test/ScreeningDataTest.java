package com.sportsfire.screening.test;

import java.util.ArrayList;

import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.screening.ScreeningData;
import com.sportsfire.sync.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.IsolatedContext;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

public class ScreeningDataTest extends ProviderTestCase2<Provider> {

	private String playerID;
	private String seasonID;

	public ScreeningDataTest() {
		super(Provider.class, "com.sportsfire.sync.Provider");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		final String filenamePrefix = "test.";
		MockContentResolver resolver = new MockContentResolver();
		resolver.addProvider("com.sportsfire.sync.Provider", getProvider());
		RenamingDelegatingContext targetContextWrapper = new RenamingDelegatingContext(
				new MockContext(), // The context that most methods are delegated to
				getContext(), // The context that file methods are delegated to
				filenamePrefix);
		Context context = new IsolatedContext(resolver, targetContextWrapper);
		setContext(context);
		ContentValues values = new ContentValues();
		// make testSquad
		values.put(SquadTable.KEY_SQUAD_NAME, "TestSquad");
		context.getContentResolver().insert(Provider.CONTENT_URI_SQUADS, values);
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_SQUADS, null,
				SquadTable.KEY_SQUAD_NAME + " = 'TestSquad'", null, null);
		cursor.moveToFirst();
		String id = cursor.getString(0);
		System.out.println(id);
		values.clear();
		// make test player
		values.put(PlayerTable.KEY_FIRST_NAME, "TestFirstName");
		values.put(PlayerTable.KEY_SURNAME, "testLastName");
		values.put(PlayerTable.KEY_DOB, 19991131);
		values.put(PlayerTable.KEY_SQUAD_ID, id);
		context.getContentResolver().insert(Provider.CONTENT_URI_PLAYERS, values);
		// get id
		cursor = context.getContentResolver().query(Provider.CONTENT_URI_PLAYERS, null,
				PlayerTable.KEY_FIRST_NAME + " = 'TestFirstName'", null, null);
		cursor.moveToFirst();
		playerID = cursor.getString(0);
		cursor.close();
		seasonID = "12";
	}

	public void testNewScreeningResultSaving() {
		String expected = "110294.90";
		ScreeningData data = new ScreeningData(getContext(), seasonID, "1");
		data.setValue(playerID, "TEST", expected);
		assertEquals(expected, data.getValue(playerID, "TEST"));
	}

	public void testChangeSeasonsResultSaving(){
		String expected = "110294.90";
		ScreeningData data = new ScreeningData(getContext(), seasonID, "1");
		data.setValue(playerID, "TEST", expected);
		assertEquals(expected, data.getValue(playerID, "TEST"));
		String expected2 = "57856.32";
		String differentSeason = "11";
		ScreeningData data2 = new ScreeningData(getContext(), differentSeason, "1");
		data2.setValue(playerID, "TEST", expected2);
		assertEquals(expected2, data2.getValue(playerID, "TEST"));
		assertEquals(expected, data.getValue(playerID, "TEST"));
		
	}
	
	public void testAvgResult() {
		String expected = "250.00";
		ScreeningData data = new ScreeningData(getContext(), seasonID, "1");
		data.setValue(playerID, "TEST", "200");
		data = new ScreeningData(getContext(), seasonID, "2");
		data.setValue(playerID, "TEST", "300");
		data = new ScreeningData(getContext(), seasonID, "3");
		data.setValue(playerID, "TEST", "350");
		data = new ScreeningData(getContext(), seasonID, "4");
		data.setValue(playerID, "TEST", "150");
		data = new ScreeningData(getContext(), seasonID, "5");
		assertEquals(expected, data.getAverageValue(playerID, "TEST"));
		String differentSeason = "11";
		ScreeningData data2 = new ScreeningData(getContext(), differentSeason , "6");
		data2.setValue(playerID, "TEST", "1150");
		assertEquals(expected, data.getAverageValue(playerID, "TEST"));
	}

	public void testPreviousResult() {
		String expected = "200";
		ScreeningData data = new ScreeningData(getContext(), seasonID, "1");
		data.setValue(playerID, "TEST", expected);
		data = new ScreeningData(getContext(), seasonID, "2");
		assertEquals(expected, data.getPreviousValue(playerID, "TEST"));
		expected = "300";
		data.setValue(playerID, "TEST", expected);
		data = new ScreeningData(getContext(), seasonID, "3");
		assertEquals(expected, data.getPreviousValue(playerID, "TEST"));
		expected = "350";
		data.setValue(playerID, "TEST", expected);
		data = new ScreeningData(getContext(), seasonID, "4");
		assertEquals(expected, data.getPreviousValue(playerID, "TEST"));
		expected = "150";
		data.setValue(playerID, "TEST", expected);
		data = new ScreeningData(getContext(), seasonID, "5");
		assertEquals(expected, data.getPreviousValue(playerID, "TEST"));
	}

	public void testGetPlayerSeasonData() {
		String differentSeason = "11";
		ArrayList<Double> expected = new ArrayList<Double>();
		ScreeningData data = new ScreeningData(getContext(), seasonID, "1");
		data.setValue(playerID, "TEST", "200");
		expected.add(200.00);
		data = new ScreeningData(getContext(), seasonID, "2");
		data.setValue(playerID, "TEST", "300");
		expected.add(300.00);
		data = new ScreeningData(getContext(), seasonID, "3");
		data.setValue(playerID, "TEST", "350");
		expected.add(350.00);
		data = new ScreeningData(getContext(), seasonID, "4");
		data.setValue(playerID, "TEST", "150");
		expected.add(150.00);
		data = new ScreeningData(getContext(), differentSeason, "5");
		data.setValue(playerID, "TEST", "1150");
		data = new ScreeningData(getContext(), seasonID);
		assertEquals(expected, data.getPlayerSeasonData(playerID, "TEST"));
	}
}
