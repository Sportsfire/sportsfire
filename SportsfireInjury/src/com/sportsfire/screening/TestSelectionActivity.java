package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sportsfire.R;
import com.sportsfire.SquadList;
import com.sportsfire.injury.InjuryForm;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

public class TestSelectionActivity extends Activity {
	SquadList squads;
	List<String> squadList;
	List<String> weekList = new ArrayList<String>();
	HashMap<CompoundButton, Spinner> testSelectionMap = new HashMap<CompoundButton, Spinner>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_testselection_page);
		squads = new SquadList(this);
		squadList = squads.getSquadNameList();
		Spinner spinner = (Spinner) findViewById(R.id.squadSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, squadList);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		// Fill list of weeks
		for (int i = 0; i <= 53; i++) {
			weekList.add("Week " + Integer.toString(i));
		}
		spinner = (Spinner) findViewById(R.id.weekSpinner);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, weekList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter2);
		testSelectionMap.put((CompoundButton) findViewById(R.id.WeightSwitch),
				(Spinner) findViewById(R.id.WeightSpinner));
		testSelectionMap.put((CompoundButton) findViewById(R.id.SqueezeSwitch),
				(Spinner) findViewById(R.id.SqueezeSpinner));
		testSelectionMap.put((CompoundButton) findViewById(R.id.CMJSwitch),
				(Spinner) findViewById(R.id.CMJSpinner));
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
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

	public void onSwitchClicked(View view) {
		if (((CompoundButton) view).isChecked()) {
			(testSelectionMap.get(view)).setVisibility(View.VISIBLE);
		} else {
			(testSelectionMap.get(view)).setVisibility(View.INVISIBLE);
		}
	}

	public void sendData(View view) {
		Map<String, Integer> selectedTests = new HashMap<String, Integer>();
		for (CompoundButton k : testSelectionMap.keySet()) {
			if (k.isChecked()) {
				selectedTests.put(k.getText().toString(), (testSelectionMap.get(k))
						.getSelectedItemPosition());
			}
		}
		Intent intent = new Intent(this, TestInputForm.class);
		intent.putExtra(TestInputForm.ARG_ITEM_TESTS, selectedTests.keySet().toArray());
		intent.putExtra(TestInputForm.ARG_ITEM_TESTVAL, selectedTests.values().toArray());
		startActivity(intent);
	}
}
