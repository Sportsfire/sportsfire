package com.sportsfire.test;

import com.sportsfire.Player;
import com.sportsfire.R;
import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.injury.InjuryForm;
import com.sportsfire.sync.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.test.ActivityInstrumentationTestCase2;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NewInjuryFormTest extends ActivityInstrumentationTestCase2<InjuryForm> {
	private InjuryForm activity;
	private IsolatedContext context;
	private String playerID = "-5";

	public NewInjuryFormTest() {
		super(InjuryForm.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getInstrumentation().getContext().deleteDatabase(DBHelper.getDbName());
		Cursor cursor;
		MockContentResolver resolver = new MockContentResolver();
		Provider provider = new Provider();
		resolver.addProvider("com.sportsfire.sync.Provider", provider);
		RenamingDelegatingContext targetContextWrapper = new RenamingDelegatingContext(
				new MockContext(), // The context that most methods are delegated to
				getInstrumentation().getTargetContext(), // The context that file methods are delegated to
				"test.");
		context = new IsolatedContext(resolver, targetContextWrapper);
		ContentValues values = new ContentValues();
		// make testSquad
		values.put(SquadTable.KEY_SQUAD_NAME, "TestSquad");
		context.getContentResolver().insert(Provider.CONTENT_URI_SQUADS, values);
		cursor = context.getContentResolver().query(Provider.CONTENT_URI_SQUADS, null,
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
		if (cursor.moveToFirst())
			playerID = cursor.getString(0);
		Player testPlayer = new Player("TestFirstName", "testLastName", "12", context);
		Intent intent = new Intent();
		intent.putExtra(InjuryForm.ARG_ITEM_PLAYER, testPlayer);
		setActivityIntent(intent);
		activity = getActivity();
	}
	protected void tearDown(){
		//context.deleteDatabase(DBHelper.getDbName());
		//System.out.println(getInstrumentation().getTargetContext().databaseList());
		//getInstrumentation().getContext().deleteDatabase(DBHelper.getDbName());
		//getInstrumentation().getTargetContext().deleteDatabase(DBHelper.getDbName());
	}
	
	public void testPreConditions() {
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_PLAYERS, null,
				PlayerTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		assertEquals("TestFirstName", cursor.getString(1));
		assertEquals("testLastName", cursor.getString(2));
	}

	public void testInjuryDateSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			String testDate = "19/01/2011";

			@Override
			public void run() {
				((TextView) activity.findViewById(com.sportsfire.R.id.ir1a)).setText(testDate);
				activity.onSaveForm(null);
				Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_INJURIES,
						null, InjuryTable.KEY_DATE_OF_INJURY + " = '" + testDate + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(testDate, cursor.getString(1));
				}
			}

		});

	}
	public void testReturnDateSaving(){}
	public void testBodyPartSideSaving(){}
	public void testOtherInjurySavingWhenNotChecked(){}
	public void testOtherInjurySavingWhenChecked(){}
	public void testOrchardCodeSaving(){}
	public void testReoccuranceSeletionSaving(){}
	public void testPreviousInjuryReturnDateSavingWhenNoSelected(){}
	public void testPreviousInjuryReturnDateSavingWhenYesSelected(){}
	public void testPreviousInjuryReturnDateSavingWhenNothingSelected(){}
	public void testOveruseSelectedSaving(){}
	public void testTraumaSelectedSaving(){}
	public void testNoSelectionForTraumaOrOveruseSaving(){}
	public void testTrainingSelectedSaving(){}
	public void testMatchSelectedSaving(){}
	public void testNoSelectionForTrainingOrMatchSaving(){}
	public void testNoContactSelectedSaving(){}
	public void testPlayerContactSelectedSaving(){}
	public void testBallContactSelectedSaving(){}
	public void testOtherContactSelectedSaving(){}
	public void testNoViolationSelectedSaving(){}
	public void testFreeKickSelectedSaving(){}
	public void testYellowCardSelectedSaving(){}
	public void testRedCardSelectedSaving(){}
	public void testSanctionAgainstPlayerSaving(){}
	public void testSanctionAgainstOpponentSaving(){}
}
