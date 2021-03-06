package com.sportsfire.screening.input;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sportsfire.objects.Season;
import com.sportsfire.objects.Squad;
import com.sportsfire.objects.SquadList;
import com.sportsfire.screening.R;
import com.sportsfire.screening.ScreeningData;
import com.sportsfire.screening.ScreeningMainPage;
import com.sportsfire.screening.analysis.FormValues;

public class InputPageActivity extends FragmentActivity implements TestSelectionFragment.Callbacks {
	SquadList squads;
	Season season;
	public static final String ARG_ITEM_SEASON_NAME = "argumentSeasonName";
	public static final String ARG_ITEM_SEASON_ID = "argumentSeasonId";
	TestSelectionFragment fragment;
	ScreeningData screenData;
	Squad selected;
	String week;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_input_page);
		season = new Season(getIntent().getStringExtra(ARG_ITEM_SEASON_NAME), getIntent()
				.getStringExtra(ARG_ITEM_SEASON_ID), "1");
		squads = new SquadList(this);
		setUpSquadSpinner();
		setUpWeekSpinner();
		fragment = new TestSelectionFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.test_selection_container, fragment).commit();
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	private void setUpSquadSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.squadSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, squads.getSquadNameList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selected = squads.getSquadList().get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	private void setUpWeekSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.weekSpinner);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, season.getWeeklist());
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter2);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			spinner.setSelection(settings.getInt("selected_week", 0));
		} catch (Exception e) {
		}
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					week = ((TextView) arg1).getText().toString();
					screenData = new ScreeningData(arg0.getContext(), season.getSeasonName(), week);
					SharedPreferences settings = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					settings.edit().putInt("selected_week", arg2).apply();

				} catch (Exception e) {
					setUpWeekSpinner();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, ScreeningMainPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onSwitchClicked(View view) {
		fragment.onSwitchClicked(view);
	}

	public void sendData(View view) {
		fragment.sendData(view);
	}

	@Override
	public void onTestsChosen(LinkedHashMap<String, String> map) {
		if (map.isEmpty()){
			Toast toast = Toast.makeText(this, "Please select tests",
					Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		String[] params = new String[2];
		params[0] = season.getSeasonID();
		params[1] = week;
		Intent intent = new Intent(this, TestInputForm.class);
		intent.putExtra(TestInputForm.ARG_ITEM_PARAM, params);
		intent.putExtra(TestInputForm.ARG_ITEM_SQUAD, selected);
		final Bundle bundle = new Bundle();
		List<List<String>> column = new ArrayList<List<String>>();
		// add column headings
		List<String> headerRow = new ArrayList<String>();
		headerRow.add("Full Name");
		for (Entry<String, String> test : map.entrySet()) {
			bundle.putString(test.getKey(), test.getValue());
			headerRow.add(test.getKey());
			if (test.getValue().compareTo("Show previous and average") == 0) {
				headerRow.add(test.getKey() + " Avg");
				headerRow.add(test.getKey() + " Pre");
			} else if (test.getValue().compareTo("Show previous") == 0) {
				headerRow.add(test.getKey() + " Pre");
			} else if (test.getValue().compareTo("Show average") == 0) {
				headerRow.add(test.getKey() + " Avg");
			}
		}
		ArrayList<String> a = new ArrayList<String>(map.keySet());
		bundle.putStringArrayList(TestInputForm.ARG_ITEM_DATA, a);
		column.add(headerRow);
		intent.putExtra(TestInputForm.ARG_ITEM_TESTS, bundle);
		FormValues values = new FormValues(column);
		intent.putExtra(TestInputForm.ARG_ITEM_DATA, values);
		startActivity(intent);
	}
}
