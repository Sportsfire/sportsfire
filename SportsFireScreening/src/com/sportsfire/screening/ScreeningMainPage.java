package com.sportsfire.screening;

import java.io.File;

import net.sqlcipher.database.SQLiteDatabase;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sportsfire.objects.Season;
import com.sportsfire.objects.SeasonList;
import com.sportsfire.screening.analysis.AnalysisPageActivity;
import com.sportsfire.screening.input.InputPageActivity;
import com.sportsfire.sync.AuthenticatorActivity;
import com.sportsfire.sync.Constants;
import com.sportsfire.unique.Provider;

import static com.sportsfire.sync.Constants.*;

public class ScreeningMainPage extends Activity {
	SeasonList seasons;
	Season selected;

	private void showSyncLoginDialog() {
		AccountManager accountManager = AccountManager.get(this.getApplicationContext());
		final Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Please set up sync first to provide intial app data")
				.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if (accounts.length > 0) {
							ContentResolver.requestSync(accounts[0], Provider.AUTHORITY, new Bundle());
							finish();
						} else {
							final Intent intent = new Intent(getBaseContext(), AuthenticatorActivity.class);
							startActivity(intent);
						}
					}
				}).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitializeSQLCipher();
		setContentView(R.layout.screening_main_page);
		if (!DbSetUp()) {
			// showSyncLoginDialog();
		} else {
			setUpSpinners();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_update:
			server.update(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private Boolean DbSetUp() {
		this.seasons = new SeasonList(this);
		Log.e("Seasons", this.seasons.getSeasonNameList().toString());
		if (seasons.getSeasonNameList().size() == 0) {
			return false;
		}
		return true;
	}

	private void setUpSpinners() {
		Spinner spinner = (Spinner) findViewById(R.id.seasonSpin);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				seasons.getSeasonNameList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			spinner.setSelection(settings.getInt("selected_season", 0));

			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					selected = seasons.getSeasonList().get(arg2);
					SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					settings.edit().putInt("selected_season", arg2).apply();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}

			});
		} catch (Exception e) {
		}
	}

	public void ButtonOnClick(View v) {
		Intent intent;
		if (!DbSetUp()) {
			showSyncLoginDialog();
		} else {
			DbSetUp();
			setUpSpinners();
			switch (v.getId()) {
			case R.id.button1:
				intent = new Intent(this, InputPageActivity.class);
				intent.putExtra(InputPageActivity.ARG_ITEM_SEASON_NAME, selected.getSeasonName());
				intent.putExtra(InputPageActivity.ARG_ITEM_SEASON_ID, selected.getSeasonID());
				startActivity(intent);
				break;
			case R.id.button2:
				intent = new Intent(this, AnalysisPageActivity.class);
				intent.putExtra(AnalysisPageActivity.ARG_ITEM_SEASON_ID, selected.getSeasonID());
				startActivity(intent);
				break;
			}
		}
	}

	private void InitializeSQLCipher() {
		SQLiteDatabase.loadLibs(this);
		File databaseFile = getDatabasePath("com.sportsfire.db");
		databaseFile.mkdirs();
		databaseFile.delete();

	}
}
