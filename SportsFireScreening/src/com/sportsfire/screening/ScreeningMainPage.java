package com.sportsfire.screening;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sportsfire.screening.R;
import com.sportsfire.Season;
import com.sportsfire.SeasonList;

public class ScreeningMainPage extends Activity {
	SeasonList seasons;
	Season selected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		seasons = new SeasonList(this);

		setContentView(R.layout.screening_main_page);
		Spinner spinner = (Spinner) findViewById(R.id.seasonSpin);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, seasons.getSeasonNameList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			spinner.setSelection(settings.getInt("selected_season", 0));
		} catch (Exception e) {
		}

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selected = seasons.getSeasonList().get(arg2);
				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				settings.edit().putInt("selected_season", arg2).apply();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public void ButtonOnClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button1:
			intent = new Intent(this, InputPageActivity.class);
			// setContentView(R.layout.activity_list_page);
			intent.putExtra(InputPageActivity.ARG_ITEM_SEASON_NAME, selected.getSeasonName());
			intent.putExtra(InputPageActivity.ARG_ITEM_SEASON_ID, selected.getSeasonID());
			startActivity(intent);
			break;
		case R.id.button2:
			intent = new Intent(this, AnalysisPageActivity.class);
			intent.putExtra(AnalysisPageActivity.ARG_ITEM_SEASON_NAME, selected.getSeasonName());
			intent.putExtra(AnalysisPageActivity.ARG_ITEM_SEASON_ID, selected.getSeasonID());
			startActivity(intent);
			break;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
