package com.sportsfire.sportsfireinjury;

import com.sportsfire.InjuryReportControl;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class InjuryForm extends Activity {
	public static final String ARG_ITEM_ID = "player_injury";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_injury_form);
		InjuryReportControl details = getIntent().getParcelableExtra(ARG_ITEM_ID);
		if (details != null) {
			((TextView) findViewById(R.id.EditText01)).setText("10/10/10");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment((TextView) v);
		newFragment.show(getFragmentManager(), "datePicker");
	}
}
