package com.sportsfire.injury.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.IsolatedContext;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import com.sportsfire.Player;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.injury.InjuryReportControl;
import com.sportsfire.injury.sync.Provider;

public class InjuryReportControlTest extends ProviderTestCase2<Provider> {
	private IsolatedContext context;
	private String playerID = "";
	private InjuryReportControl report;

	public InjuryReportControlTest() {
		super(Provider.class, "com.sportsfire.injury.sync.Provider");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		final String filenamePrefix = "test.";
		MockContentResolver resolver = new MockContentResolver();
		resolver.addProvider("com.sportsfire.injury.sync.Provider", getProvider());
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
		Player testPlayer = new Player("TestFirstName", "testLastName", playerID, context);
		report = new InjuryReportControl(testPlayer, context);
		report.setValue(InjuryTable.KEY_ORCHARD, "");
		report.saveForm();
	}

	public void testCreateNewReport() {
		assertEquals("", report.getOrchardCode());
		assertEquals("", report.getValue(InjuryTable.KEY_CONTACT_BALL));
		assertEquals("", report.getValue(InjuryTable.KEY_CONTACT_NO));
		assertEquals("", report.getValue(InjuryTable.KEY_CONTACT_OTHER));
		assertEquals("", report.getValue(InjuryTable.KEY_CONTACT_PLAYER));
		assertEquals("", report.getValue(InjuryTable.KEY_DATE_OF_INJURY));
		assertEquals("", report.getValue(InjuryTable.KEY_DATE_OF_RETURN));
		assertEquals("", report.getValue(InjuryTable.KEY_DATE_OF_RETURN));
		assertEquals("", report.getValue(InjuryTable.KEY_DIAGNOSIS));
		assertEquals("", report.getValue(InjuryTable.KEY_INJURED_BODY_PART));
		assertEquals("", report.getValue(InjuryTable.KEY_INJURY_ID));
		assertEquals("", report.getValue(InjuryTable.KEY_ORCHARD));
		assertEquals("", report.getValue(InjuryTable.KEY_OVERUSE_TRAUMA));
		assertEquals("", report.getValue(InjuryTable.KEY_OVERUSE_TRAUMA));
		assertEquals(playerID, report.getValue(InjuryTable.KEY_PLAYER_ID));
		assertEquals("", report.getValue(InjuryTable.KEY_PREVIOUS));
		assertEquals("", report.getValue(InjuryTable.KEY_PREVIOUS_DATE));
		assertEquals("", report.getValue(InjuryTable.KEY_REFEREE));
		assertEquals("", report.getValue(InjuryTable.KEY_SANCTION_OPPONENT));
		assertEquals("", report.getValue(InjuryTable.KEY_SANCTION_PLAYER));
		assertEquals("", report.getValue(InjuryTable.KEY_TRAINING_MATCH));
	}
	
	public void testUpdateReport(){
	}
}
