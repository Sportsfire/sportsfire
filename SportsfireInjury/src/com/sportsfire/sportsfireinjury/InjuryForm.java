package com.sportsfire.sportsfireinjury;

import com.sportsfire.*;
import com.sportsfire.db.InjuryTable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class InjuryForm extends Activity {
	public static final String ARG_ITEM_INJURY = "argumentInjuryID";
	public static final String ARG_ITEM_PLAYER = "argumentPlayer";
	private InjuryReportControl reportControl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_injury_form);
		ActionBar actionBar = getActionBar();
		Player p = getIntent().getParcelableExtra(ARG_ITEM_PLAYER);
		if (p != null) {
			reportControl = new InjuryReportControl(p, this);
			actionBar.setTitle(p.getName() + "  PlayerID:" + p.getID() + "  NEW INJURY" );
		} else {
			InjuryReportID id = getIntent().getParcelableExtra(ARG_ITEM_INJURY);
			reportControl = new InjuryReportControl(id, this);
			actionBar.setTitle("Injury for PlayerID:" + reportControl.getValue("playerID")
					+ " InjuryID:" + reportControl.getValue("_id"));
		}

		((TextView) findViewById(R.id.ir1a)).addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// Log.d("seachScreen", "afterTextChanged");
				reportControl.setValue(idToField(R.id.ir1a), s.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Log.d("seachScreen", "beforeTextChanged");
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Log.d("seachScreen", "onTextChanged");
			}
		});

		((TextView) findViewById(R.id.ir1b)).addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// Log.d("seachScreen", "afterTextChanged");
				reportControl.setValue(idToField(R.id.ir1b), s.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Log.d("seachScreen", "beforeTextChanged");
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Log.d("seachScreen", "onTextChanged");
			}
		});

		((TextView) findViewById(R.id.ir4)).addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// Log.d("seachScreen", "afterTextChanged");
				reportControl.setValue(idToField(R.id.ir4), s.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Log.d("seachScreen", "beforeTextChanged");
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Log.d("seachScreen", "onTextChanged");
			}
		});

		// Setup everything
		((TextView) findViewById(R.id.ir1a)).setText(reportControl
				.getValue(InjuryTable.KEY_DATE_OF_INJURY));
		((TextView) findViewById(R.id.ir1b)).setText(reportControl
				.getValue(InjuryTable.KEY_DATE_OF_RETURN));
		((TextView) findViewById(R.id.ir4)).setText(reportControl
				.getValue(InjuryTable.KEY_DIAGNOSIS));
		textBoxSetup(R.id.ir2a1, InjuryTable.KEY_BODYPART_HEAD);
		textBoxSetup(R.id.ir2a2, InjuryTable.KEY_BODYPART_SHOULDER);
		textBoxSetup(R.id.ir2a3, InjuryTable.KEY_BODYPART_HIP);
		textBoxSetup(R.id.ir2a4, InjuryTable.KEY_BODYPART_NECK);
		textBoxSetup(R.id.ir2a5, InjuryTable.KEY_BODYPART_UPPERARM);
		textBoxSetup(R.id.ir2a6, InjuryTable.KEY_BODYPART_THIGH);
		textBoxSetup(R.id.ir2a7, InjuryTable.KEY_BODYPART_STERNUM);
		textBoxSetup(R.id.ir2a8, InjuryTable.KEY_BODYPART_ELBOW);
		textBoxSetup(R.id.ir2a9, InjuryTable.KEY_BODYPART_KNEE);
		textBoxSetup(R.id.ir2a10, InjuryTable.KEY_BODYPART_ABDOMEN);
		textBoxSetup(R.id.ir2a11, InjuryTable.KEY_BODYPART_FOREARM);
		textBoxSetup(R.id.ir2a12, InjuryTable.KEY_BODYPART_LOWERLEG);
		textBoxSetup(R.id.ir2a13, InjuryTable.KEY_BODYPART_LOWBACK);
		textBoxSetup(R.id.ir2a14, InjuryTable.KEY_BODYPART_WRIST);
		textBoxSetup(R.id.ir2a15, InjuryTable.KEY_BODYPART_ANKLE);
		textBoxSetup(R.id.ir2a16, InjuryTable.KEY_BODYPART_HAND);
		textBoxSetup(R.id.ir2a17, InjuryTable.KEY_BODYPART_FOOT);
	}

	private void textBoxSetup(int id, String field) {
		if (reportControl.getValue(field).equals("0")) {
			((CheckBox) findViewById(id)).setChecked(false);
		} else {
			((CheckBox) findViewById(id)).setChecked(true);
		}
	}

	private String idToField(int id) {
		switch (id) {
			case R.id.ir1a :
				return InjuryTable.KEY_DATE_OF_INJURY;
			case R.id.ir1b :
				return InjuryTable.KEY_DATE_OF_RETURN;
			case R.id.ir2a1 :
				return InjuryTable.KEY_BODYPART_HEAD;
			case R.id.ir2a2 :
				return InjuryTable.KEY_BODYPART_SHOULDER;
			case R.id.ir2a3 :
				return InjuryTable.KEY_BODYPART_HIP;
			case R.id.ir2a4 :
				return InjuryTable.KEY_BODYPART_NECK;
			case R.id.ir2a5 :
				return InjuryTable.KEY_BODYPART_UPPERARM;
			case R.id.ir2a6 :
				return InjuryTable.KEY_BODYPART_THIGH;
			case R.id.ir2a7 :
				return InjuryTable.KEY_BODYPART_STERNUM;
			case R.id.ir2a8 :
				return InjuryTable.KEY_BODYPART_ELBOW;
			case R.id.ir2a9 :
				return InjuryTable.KEY_BODYPART_KNEE;
			case R.id.ir2a10 :
				return InjuryTable.KEY_BODYPART_ABDOMEN;
			case R.id.ir2a11 :
				return InjuryTable.KEY_BODYPART_FOREARM;
			case R.id.ir2a12 :
				return InjuryTable.KEY_BODYPART_LOWERLEG;
			case R.id.ir2a13 :
				return InjuryTable.KEY_BODYPART_LOWBACK;
			case R.id.ir2a14 :
				return InjuryTable.KEY_BODYPART_WRIST;
			case R.id.ir2a15 :
				return InjuryTable.KEY_BODYPART_ANKLE;
			case R.id.ir2a16 :
				return InjuryTable.KEY_BODYPART_HAND;
			case R.id.ir2a17 :
				return InjuryTable.KEY_BODYPART_FOOT;
			case R.id.ir31 :
				return InjuryTable.KEY_TYPE_CONCUSSION;
			case R.id.ir32 :
				return InjuryTable.KEY_TYPE_LESION;
			case R.id.ir33 :
				return InjuryTable.KEY_TYPE_HAEMATOMA;
			case R.id.ir34 :
				return InjuryTable.KEY_TYPE_FRACTURE;
			case R.id.ir35 :
				return InjuryTable.KEY_TYPE_MUSCLE;
			case R.id.ir36 :
				return InjuryTable.KEY_TYPE_ABRASION;
			case R.id.ir37 :
				return InjuryTable.KEY_TYPE_OTHERBONE;
			case R.id.ir38 :
				return InjuryTable.KEY_TYPE_LACERATION;
			case R.id.ir39 :
				return InjuryTable.KEY_TYPE_DISLOCATION;
			case R.id.ir310 :
				return InjuryTable.KEY_TYPE_TENDON;
			case R.id.ir311 :
				return InjuryTable.KEY_TYPE_NERVE;
			case R.id.ir312 :
				return InjuryTable.KEY_TYPE_SPRAIN;
			case R.id.ir313 :
				return InjuryTable.KEY_TYPE_DENTAL;
			case R.id.ir4 :
				return InjuryTable.KEY_DIAGNOSIS;
			default :
				return "";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.player_injury_form, menu);
		return true;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment((TextView) v);
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void onCheckBoxClick(View v) {
		CheckBox c = (CheckBox) v;
		if (c.isChecked()) {
			reportControl.setValue(idToField(c.getId()), "1");
		} else {
			reportControl.setValue(idToField(c.getId()), "0");
		}
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, ListPageActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.menu_save:
	        	onSaveForm(findViewById(android.R.id.home));
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void onSaveForm(View v) {
		// Intent intent2 = new Intent(this,ListPageActivity.class);
		// startActivity(intent2);
		finish();
		Context context = getApplicationContext();
		CharSequence text = "Saved successfully";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		reportControl.saveForm();
		// TODO: Make a check to see if the report saved sucessfully
		toast.show();
	}
}
