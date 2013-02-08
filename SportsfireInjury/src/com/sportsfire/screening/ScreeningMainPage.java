package com.sportsfire.screening;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sportsfire.R;
import com.sportsfire.Season;
import com.sportsfire.SeasonList;

public class ScreeningMainPage extends Activity {
	SeasonList seasons = new SeasonList(this);
	Season selected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_main_page);
		Spinner spinner = (Spinner) findViewById(R.id.seasonSpin);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, seasons.getSeasonNameList());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selected = seasons.getSeasonList().get(arg2);
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
		switch (v.getId()) {
		case R.id.button1:
			Intent intent2 = new Intent(this, TestSelectionActivity.class);
			// setContentView(R.layout.activity_list_page);
			intent2.putExtra(TestSelectionActivity.ARG_ITEM_SEASON, selected);
			startActivity(intent2);
			break;
		case R.id.button2:

			break;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, com.sportsfire.MainPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
