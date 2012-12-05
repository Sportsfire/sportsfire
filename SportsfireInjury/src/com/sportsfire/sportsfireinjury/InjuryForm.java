package com.sportsfire.sportsfireinjury;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InjuryForm extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_injury_form);
		ArrayList<String> details = getIntent().getStringArrayListExtra("LIST");
		if (details != null) {
			((TextView) findViewById(R.id.EditText01)).setText(details.get(0));
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
