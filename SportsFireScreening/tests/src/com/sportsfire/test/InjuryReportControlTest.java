package com.sportsfire.test;

import com.sportsfire.Player;
import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.injury.InjuryReportControl;
import com.sportsfire.sync.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.IsolatedContext;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

public class InjuryReportControlTest extends ProviderTestCase2<Provider> {
	private RenamingDelegatingContext context;
	private String playerID = "";
	private InjuryReportControl report;

	public InjuryReportControlTest() {
		super(Provider.class, "com.sportsfire.sync.Provider");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// context = new RenamingDelegatingContext(getMockContext(),getContext(), "test.");
		// setContext(context);
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
		values.put(SquadTable.KEY_SQUAD_NAME, "TestSquad2");
		context.getContentResolver().insert(Provider.CONTENT_URI_SQUADS, values);
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_SQUADS, null,
				SquadTable.KEY_SQUAD_NAME + " = 'TestSquad2'", null, null);
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
		Player testPlayer = new Player("TestFirstName", "testLastName", playerID, context);
		report = new InjuryReportControl(testPlayer, context);
	}

	public void testCreateNewReport() {
		assertEquals("0", report.getOrchardCode());
		assertEquals("", InjuryTable.KEY_CONTACT_BALL);
		assertEquals("", InjuryTable.KEY_CONTACT_NO);
		assertEquals("", InjuryTable.KEY_CONTACT_OTHER);
		assertEquals("", InjuryTable.KEY_CONTACT_PLAYER);
		assertEquals("", InjuryTable.KEY_DATE_OF_INJURY);
		assertEquals("", InjuryTable.KEY_DATE_OF_RETURN);
		assertEquals("", InjuryTable.KEY_DATE_OF_RETURN);
		assertEquals("", InjuryTable.KEY_DIAGNOSIS);
		assertEquals("", InjuryTable.KEY_INJURED_BODY_PART);
		assertEquals("11", InjuryTable.KEY_INJURY_ID);
		assertEquals("", InjuryTable.KEY_ORCHARD);
		assertEquals("", InjuryTable.KEY_OVERUSE_TRAUMA);
		assertEquals("", InjuryTable.KEY_OVERUSE_TRAUMA);
		assertEquals("", InjuryTable.KEY_PLAYER_ID);
		assertEquals("", InjuryTable.KEY_PREVIOUS);
		assertEquals("", InjuryTable.KEY_PREVIOUS_DATE);
		assertEquals("", InjuryTable.KEY_REFEREE);
		assertEquals("", InjuryTable.KEY_SANCTION_OPPONENT);
		assertEquals("", InjuryTable.KEY_SANCTION_PLAYER);
		assertEquals("", InjuryTable.KEY_TRAINING_MATCH);
	}
}
