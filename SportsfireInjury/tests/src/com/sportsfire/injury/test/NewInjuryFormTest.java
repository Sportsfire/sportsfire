package com.sportsfire.injury.test;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.IsolatedContext;
import android.test.UiThreadTest;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sportsfire.Player;
import com.sportsfire.db.DBHelper;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.db.PlayerTable;
import com.sportsfire.db.SquadTable;
import com.sportsfire.injury.InjuryForm;
import com.sportsfire.injury.sync.Provider;

public class NewInjuryFormTest extends ActivityInstrumentationTestCase2<InjuryForm> {
	private InjuryForm activity;
	private Context context;
	private String playerID = "";

	public NewInjuryFormTest() {
		super(InjuryForm.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// getInstrumentation().getTargetContext().deleteDatabase(DBHelper.getDbName());
		context = new IsolatedContext(getInstrumentation().getTargetContext().getContentResolver(),
				getInstrumentation().getTargetContext());
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_SQUADS, null,
				SquadTable.KEY_SQUAD_NAME + " = 'TestSquad'", null, null);
		if (cursor.moveToFirst()) {
			String id = cursor.getString(0);
			ContentValues values = new ContentValues();
			// make testSquad
			values.put(SquadTable.KEY_SQUAD_NAME, "TestSquad");
			context.getContentResolver().insert(Provider.CONTENT_URI_SQUADS, values);
			values.clear();
			// make test player
			values.put(PlayerTable.KEY_FIRST_NAME, "TestFirstName");
			values.put(PlayerTable.KEY_SURNAME, "testLastName");
			values.put(PlayerTable.KEY_DOB, 19991131);
			values.put(PlayerTable.KEY_SQUAD_ID, id);
			context.getContentResolver().insert(Provider.CONTENT_URI_PLAYERS, values);
		}
		// get id
		cursor = context.getContentResolver().query(Provider.CONTENT_URI_PLAYERS, null,
				PlayerTable.KEY_FIRST_NAME + " = 'TestFirstName'", null, null);
		if (cursor.moveToFirst()) playerID = cursor.getString(0);
		cursor.close();
		Player testPlayer = new Player("TestFirstName", "testLastName", "12", context);
		Intent intent = new Intent();
		intent.putExtra(InjuryForm.ARG_ITEM_PLAYER, testPlayer);
		setActivityIntent(intent);
		activity = getActivity();
	}

	protected void tearDown() {
		activity.finish();
	}

	@UiThreadTest
	public void testInjuryDateSaving() {
		String testDate = "19/01/2011";

		((TextView) activity.findViewById(com.sportsfire.injury.R.id.ir1a)).setText(testDate);
		activity.onSaveForm(null);
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_INJURIES, null,
				InjuryTable.KEY_DATE_OF_INJURY + " = '" + testDate + "'", null, null);
		if (cursor.moveToFirst()) {
			assertEquals(testDate, cursor.getString(1));
		}

	}

	public void testReturnDateSaving() throws Throwable {
		final String testDate = "20/01/2011";
		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.ir1b))
						.setText(testDate);
				activity.onSaveForm(null);
			}

		});
		String[] projection = { InjuryTable.KEY_DATE_OF_RETURN };
		Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI_INJURIES,
				projection, InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
		if (cursor.moveToFirst()) {
			assertEquals(testDate, cursor.getString(1));
		}

	}

	public void testBodyPartSideSaving() throws Throwable {
	}

	public void testOtherInjurySavingWhenNotChecked() throws Throwable {
		runTestOnUiThread(new Runnable() {
			String expected = "Brain Implosion";

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irOtherInjuryChk))
						.setChecked(false);
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.irOtherInjuryTxt))
						.setText(expected);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_DIAGNOSIS };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals("", cursor.getString(0));
				}
			}

		});
	}

	public void testOtherInjurySavingWhenChecked() throws Throwable {
		runTestOnUiThread(new Runnable() {
			String expected = "Brain Implosion";

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irOtherInjuryChk))
						.setChecked(true);
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.irOtherInjuryTxt))
						.setText(expected);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_DIAGNOSIS };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testOrchardCodeSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			String expected = "OCRCH";

			@Override
			public void run() {
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.irOtherInjuryTxt))
						.setText(expected);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_ORCHARD };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testNoReoccuranceSeletionSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irReOccurNoRd))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_PREVIOUS };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testPreviousInjuryReturnShouldNotSaveDateWhenNotSelected() throws Throwable {
		runTestOnUiThread(new Runnable() {
			String expected = "19/10/1993";

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irReOccurYesRd))
						.setChecked(false);
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.irPreDORtext))
						.setText(expected);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_PREVIOUS_DATE };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals("", cursor.getString(0));
				}
			}

		});
	}

	public void testPreviousInjuryReturnDateSavingWhenYesSelected() throws Throwable {
		runTestOnUiThread(new Runnable() {
			String expected = "19/10/1993";

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irReOccurYesRd))
						.setChecked(true);
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.irPreDORtext))
						.setText(expected);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_PREVIOUS_DATE, InjuryTable.KEY_PREVIOUS };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
					assertEquals("0", cursor.getString(1));
				}
			}

		});
	}

	public void testOveruseSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 0;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irOverRadBtn))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_OVERUSE_TRAUMA };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testTraumaSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irTrauRadBtn))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_TRAINING_MATCH };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testNoSelectionForTraumaOrOveruseSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 0;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irTrauRadBtn))
						.setChecked(false);
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irOverRadBtn))
						.setChecked(false);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_TRAINING_MATCH };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals("", cursor.getString(0));
				}
			}

		});
	}

	public void testTrainingSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 0;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irTrainInjRad))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_TRAINING_MATCH };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testMatchSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irMatchInjRad))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_TRAINING_MATCH };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testNoSelectionForTrainingOrMatchSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irMatchInjRad))
						.setChecked(false);
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irTrainInjRad))
						.setChecked(false);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_TRAINING_MATCH };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals("", cursor.getString(0));
				}
			}

		});

	}

	public void testNoContactSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irNoContactChk))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_CONTACT_NO };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}

			}
		});

	}

	public void testPlayerContactSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irContactPlyrChk))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_CONTACT_PLAYER };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});

	}

	public void testBallContactSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irContactBallChk))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_CONTACT_BALL };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testOtherContactSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irContactOtherChk))
						.setChecked(true);
				((TextView) activity.findViewById(com.sportsfire.injury.R.id.irContactOtherTxt))
						.setText("Ran into Wall");
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_CONTACT_OTHER };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals("Ran into Wall", cursor.getString(0));
				}
			}

		});
	}

	public void testNoViolationSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 0;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irNoViolRd))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_REFEREE };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testFreeKickSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irFreeKRd))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_REFEREE };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testYellowCardSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 2;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irYelCrdRd))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_REFEREE };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testRedCardSelectedSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 3;

			@Override
			public void run() {
				((RadioButton) activity.findViewById(com.sportsfire.injury.R.id.irRedCrdRd))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_REFEREE };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testSanctionAgainstPlayerSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irPlyrSanChk))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_SANCTION_PLAYER };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}

	public void testSanctionAgainstOpponentSaving() throws Throwable {
		runTestOnUiThread(new Runnable() {
			int expected = 1;

			@Override
			public void run() {
				((CheckBox) activity.findViewById(com.sportsfire.injury.R.id.irOpponSanChk))
						.setChecked(true);
				activity.onSaveForm(null);
				String[] projection = { InjuryTable.KEY_SANCTION_OPPONENT };
				Cursor cursor = context.getContentResolver()
						.query(Provider.CONTENT_URI_INJURIES, projection,
								InjuryTable.KEY_PLAYER_ID + " = '" + playerID + "'", null, null);
				if (cursor.moveToFirst()) {
					assertEquals(expected, cursor.getString(0));
				}
			}

		});
	}
}
