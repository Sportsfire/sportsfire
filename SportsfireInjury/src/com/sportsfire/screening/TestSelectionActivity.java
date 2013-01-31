package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.List;

import com.sportsfire.R;
import com.sportsfire.SquadList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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
		for (int i = 0; i <=53; i++){
			weekList.add("Week " + Integer.toString(i));
		}
		spinner = (Spinner) findViewById(R.id.weekSpinner);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, weekList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter2);
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}
	
	public void onSwitchClicked(View view){
	    if (((CompoundButton) view).isChecked()) {
	    	if (view.getId() == R.id.WeightSwitch){
				((Spinner) findViewById(R.id.WeightSpinner)).setVisibility(View.VISIBLE);
	    	}
	    } else {
	    	if (view.getId() == R.id.WeightSwitch){
	    		((Spinner) findViewById(R.id.WeightSpinner)).setVisibility(View.INVISIBLE);
	    	}
	    }
	}
}
