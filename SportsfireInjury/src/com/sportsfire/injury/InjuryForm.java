package com.sportsfire.injury;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.sportsfire.*;
import com.sportsfire.db.InjuryTable;
import com.sportsfire.R;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InjuryForm extends Activity {
	public static final String ARG_ITEM_INJURY = "argumentInjuryID";
	public static final String ARG_ITEM_PLAYER = "argumentPlayer";
	private InjuryReportControl reportControl;
	private String orchardCode;
	HashMap<String, String> orchardFirstMap = new HashMap<String, String>();
	HashMap<String, String> orchardSecondMap = new HashMap<String, String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_injury_form);
		ActionBar actionBar = getActionBar();
		Player p = getIntent().getParcelableExtra(ARG_ITEM_PLAYER);
		if (p != null) {
			reportControl = new InjuryReportControl(p, this);
			actionBar.setTitle(p.getName() + "  PlayerID:" + p.getID() + "  NEW INJURY");
		} else {
			InjuryReportID id = getIntent().getParcelableExtra(ARG_ITEM_INJURY);
			reportControl = new InjuryReportControl(id, this);
			actionBar.setTitle("Injury for PlayerID:" + reportControl.getValue("playerID")
					+ " InjuryID:" + reportControl.getValue("_id"));
			orchardCode = reportControl.getOrchardCode();
			((EditText) findViewById(R.id.ir4)).setText(orchardCode, TextView.BufferType.NORMAL);

		}
		initialiseSpinners();
		initialiseText();
	}

	private void initialiseSpinners() {
		Spinner spinner = (Spinner) findViewById(R.id.orchardSpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.orchardFirstSections, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Spinner spinner = (Spinner) findViewById(R.id.orchardSpinnerDetail);
				ArrayAdapter<CharSequence> adapter = null;

				switch (pos) {
				case 0:
					spinner.setVisibility(View.INVISIBLE);
					break;
				case 1:
					adapter = ArrayAdapter.createFromResource(getBaseContext(),
							R.array.orchardFirst1, android.R.layout.simple_spinner_item);
					break;
				case 2:
					adapter = ArrayAdapter.createFromResource(getBaseContext(),
							R.array.orchardFirst2, android.R.layout.simple_spinner_item);
					break;
				case 3:
					adapter = ArrayAdapter.createFromResource(getBaseContext(),
							R.array.orchardFirst3, android.R.layout.simple_spinner_item);
					break;
				case 4:
					adapter = ArrayAdapter.createFromResource(getBaseContext(),
							R.array.orchardFirst4, android.R.layout.simple_spinner_item);
					break;
				}

				if (pos > 0) {
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setVisibility(View.VISIBLE);
					spinner.setAdapter(adapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		Spinner spinnerD = (Spinner) findViewById(R.id.orchardSpinnerDetail);
		spinnerD.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String[] keys = getResources().getStringArray(R.array.orchardFirst);
				String[] value = getResources().getStringArray(R.array.orchardFirstCodes);
				for (int i = 0; i < keys.length; i++) {
					orchardFirstMap.put(keys[i], value[i]);
				}
				orchardCode = orchardFirstMap.get((String) ((TextView) arg1).getText());
				((EditText) findViewById(R.id.ir4)).setText(orchardCode,
						TextView.BufferType.EDITABLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		Spinner spinner2 = (Spinner) findViewById(R.id.orchardSpinner2);
		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
				R.array.orchardSecond, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner2.setAdapter(adapter2);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			String[] keys = getResources().getStringArray(R.array.orchardSecond);
			String[] value = getResources().getStringArray(R.array.orchardSecondCodes);

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				for (int i = 0; i < keys.length; i++) {
					orchardSecondMap.put(keys[i], value[i]);
				}
				if (orchardCode.length() > 0) {
					orchardCode = orchardCode.charAt(0)
							+ orchardSecondMap.get((String) ((TextView) view).getText());
					((EditText) findViewById(R.id.ir4)).setText(orchardCode);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void initialiseText() {
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
				orchardCode = s.toString();
				reportControl.setOrchardCode(orchardCode);
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
		case R.id.ir1a:
			return InjuryTable.KEY_DATE_OF_INJURY;
		case R.id.ir1b:
			return InjuryTable.KEY_DATE_OF_RETURN;
		case R.id.ir4:
			return InjuryTable.KEY_DIAGNOSIS;
		default:
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

	public void onOtherInjuryCheckboxClick(View v) {
		if (((CheckBox) v).isChecked()) {
			(findViewById(R.id.ir314text)).setEnabled(true);
		} else {
			(findViewById(R.id.ir314text)).setEnabled(false);
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
