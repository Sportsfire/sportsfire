package com.sportsfire.injury;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sportsfire.InjuryReportControl;
import com.sportsfire.InjuryReportID;
import com.sportsfire.Player;
import com.sportsfire.R;
import com.sportsfire.db.InjuryTable;

public class InjuryForm extends Activity {
	public static final String ARG_ITEM_INJURY = "argumentInjuryID";
	public static final String ARG_ITEM_PLAYER = "argumentPlayer";
	private InjuryReportControl reportControl;
	private String orchardCode = "";
	private boolean initializedView = false;
	private boolean initializedView2 = false;
	Map<String, String[]> orchardSectionMap = new HashMap<String, String[]>();
	Map<String, Integer> orchTypeMap = new HashMap<String, Integer>();
	Map<String, String> orchFirstMap = new HashMap<String, String>();
	Map<String, String> orchSecondMap = new HashMap<String, String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_injury_form);
		ActionBar actionBar = getActionBar();
		Player p = getIntent().getParcelableExtra(ARG_ITEM_PLAYER);
		initialiseSpinners();
		initialiseText();
		if (p != null) {
			reportControl = new InjuryReportControl(p, this);
			actionBar.setTitle(p.getName() + "  PlayerID:" + p.getID() + "  NEW INJURY");
		} else {
			InjuryReportID id = getIntent().getParcelableExtra(ARG_ITEM_INJURY);
			reportControl = new InjuryReportControl(id, this);
			actionBar.setTitle("Injury for PlayerID:" + reportControl.getValue("playerID")
					+ " InjuryID:" + reportControl.getValue("_id"));
			orchardCode = reportControl.getOrchardCode();
			loadData();
		}

	}

	private void initialiseSpinners() {
		String[] keys = getResources().getStringArray(R.array.orchardFirstSections);
		orchardSectionMap.put(keys[1], getResources().getStringArray(R.array.orchardFirst1));
		orchardSectionMap.put(keys[2], getResources().getStringArray(R.array.orchardFirst2));
		orchardSectionMap.put(keys[3], getResources().getStringArray(R.array.orchardFirst3));
		orchardSectionMap.put(keys[4], getResources().getStringArray(R.array.orchardFirst4));
		keys = getResources().getStringArray(R.array.orchardFirst);
		String[] value = getResources().getStringArray(R.array.orchardFirstCodes);
		for (int i = 0; i < keys.length; i++) {
			orchFirstMap.put(keys[i], value[i]);
		}
		keys = getResources().getStringArray(R.array.orchardSecond);
		value = getResources().getStringArray(R.array.orchardSecondCodes);
		for (int i = 0; i < keys.length; i++) {
			orchSecondMap.put(keys[i], value[i]);
		}
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
				ArrayAdapter<String> adapter = null;

				switch (pos) {
				case 0:
					spinner.setVisibility(View.INVISIBLE);
					break;
				default:
					adapter = new ArrayAdapter<String>(parent.getContext(),
							android.R.layout.simple_spinner_item, orchardSectionMap
									.get(((TextView) view).getText()));
					break;
				}

				if (pos > 0) {
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setVisibility(View.VISIBLE);
					if (initializedView == false) {
						initializedView = true;
					} else {
						spinner.setAdapter(adapter);
					}
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
				if (initializedView2 == false) {
					initializedView2 = true;
				} else {
					String code = orchFirstMap.get((String) ((TextView) arg1).getText());
					((EditText) findViewById(R.id.OrchCodeTxt)).setText(code,
							TextView.BufferType.EDITABLE);
					((Spinner) findViewById(R.id.irOrchTypeSpin)).setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		Spinner spinner2 = (Spinner) findViewById(R.id.irOrchTypeSpin);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
				R.array.orchardSecond, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner2.setAdapter(adapter2);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				EditText orchard = ((EditText) findViewById(R.id.OrchCodeTxt));
				String mapping = orchSecondMap.get(((TextView) view).getText());
				if (orchard.getText().length() == 1) {
					(orchard).setText(orchard.getText().append(mapping));
				}
				if (orchard.getText().length() > 1) {
					Editable code = orchard.getText().replace(1, 2, mapping);
					(orchard).setText(code);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void initialiseText() {
		((TextView) findViewById(R.id.OrchCodeTxt)).addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				orchardCode = s.toString();
				reportControl.setOrchardCode(orchardCode);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

	}

	public void loadData() {
		// Setup everything
		((EditText) findViewById(R.id.OrchCodeTxt))
				.setText(orchardCode, TextView.BufferType.NORMAL);
		((TextView) findViewById(R.id.ir1a)).setText(reportControl
				.getValue(InjuryTable.KEY_DATE_OF_INJURY));
		((TextView) findViewById(R.id.ir1b)).setText(reportControl
				.getValue(InjuryTable.KEY_DATE_OF_RETURN));
		((TextView) findViewById(R.id.OrchCodeTxt)).setText(reportControl
				.getValue(InjuryTable.KEY_ORCHARD));
		String bodyPart = reportControl.getValue(InjuryTable.KEY_INJURED_BODY_PART);
		if (bodyPart.compareTo("") != 0) {
			Integer index = Integer.parseInt(bodyPart);
			RadioButton selected = (RadioButton) (((RadioGroup) findViewById(R.id.irBodyPrt))
					.getChildAt(index));
			selected.setChecked(true);
		}
		String orchard = new String(orchardCode);
		System.out.println(orchard);
		if (orchardCode.length() > 0) {
			String first = getKey(orchFirstMap, orchard.substring(0, 1));
			if (first != null) {
				Integer firstpos = 0, secpos = 0;
				System.out.println(first);
				for (String[] set : orchardSectionMap.values()) {
					if (Arrays.asList(set).contains(first)) {
						String group = getKey(orchardSectionMap, set);
						firstpos = Arrays.asList(
								getResources().getStringArray(R.array.orchardFirstSections))
								.indexOf(group);
						secpos = Arrays.asList(set).indexOf(first);
					}
				}
				((Spinner) findViewById(R.id.orchardSpinner)).setSelection(firstpos);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item,
						orchardSectionMap.get(((Spinner) findViewById(R.id.orchardSpinner))
								.getSelectedItem().toString()));
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// ((Spinner) findViewById(R.id.orchardSpinnerDetail)).setVisibility(View.VISIBLE);
				((Spinner) findViewById(R.id.orchardSpinnerDetail)).setAdapter(adapter);
				((Spinner) findViewById(R.id.orchardSpinnerDetail)).setSelection(secpos);
				((Spinner) findViewById(R.id.orchardSpinnerDetail)).getSelectedItem().toString();
				if (orchard.length() > 1) {
					String second = getKey(orchSecondMap, orchard.substring(1, 2));
					int pos = Arrays.asList(getResources().getStringArray(R.array.orchardSecond))
							.indexOf(second);
					((Spinner) findViewById(R.id.irOrchTypeSpin)).setSelection(pos);
				}
			}
		}
		if (reportControl.getValue(InjuryTable.KEY_PREVIOUS).startsWith("0")) {
			((RadioButton) findViewById(R.id.irReOccurYesRd)).setChecked(true);
			findViewById(R.id.irPreDORtext).setEnabled(true);
			((TextView) findViewById(R.id.irPreDORtext)).setText(reportControl
					.getValue(InjuryTable.KEY_PREVIOUS_DATE));
		} else if (reportControl.getValue(InjuryTable.KEY_PREVIOUS).startsWith("1")) {
			((RadioButton) findViewById(R.id.irReOccurNoRd)).setChecked(true);
		}
		if (reportControl.getValue(InjuryTable.KEY_OVERUSE_TRAUMA).startsWith("0")) {
			((RadioButton) findViewById(R.id.irOverRadBtn)).setChecked(true);
		} else if (reportControl.getValue(InjuryTable.KEY_OVERUSE_TRAUMA).startsWith("1")) {
			((RadioButton) findViewById(R.id.irTrauRadBtn)).setChecked(true);
		}
		if (reportControl.getValue(InjuryTable.KEY_TRAINING_MATCH).startsWith("0")) {
			((RadioButton) findViewById(R.id.irTrainInjRad)).setChecked(true);
		} else if (reportControl.getValue(InjuryTable.KEY_OVERUSE_TRAUMA).startsWith("1")) {
			((RadioButton) findViewById(R.id.irMatchInjRad)).setChecked(true);
		}
		if (reportControl.getValue(InjuryTable.KEY_CONTACT_NO).startsWith("1")) {
			((CheckBox) findViewById(R.id.irNoContactChk)).setChecked(true);
		} else {
			if (reportControl.getValue(InjuryTable.KEY_CONTACT_PLAYER).startsWith("1")) {
				((CheckBox) findViewById(R.id.irContactPlyrChk)).setChecked(true);
			}
			if (reportControl.getValue(InjuryTable.KEY_CONTACT_BALL).startsWith("1")) {
				((CheckBox) findViewById(R.id.irContactBallChk)).setChecked(true);
			}
			if (!reportControl.getValue(InjuryTable.KEY_CONTACT_OTHER).isEmpty()) {
				((CheckBox) findViewById(R.id.irContactOtherChk)).setChecked(true);
				findViewById(R.id.irContactOtherTxt).setEnabled(true);
				((TextView) findViewById(R.id.irContactOtherTxt)).setText(reportControl
						.getValue(InjuryTable.KEY_CONTACT_OTHER));
			}
		}
		if (reportControl.getValue(InjuryTable.KEY_REFEREE).startsWith("0")) {
			((RadioButton) findViewById(R.id.irNoViolRd)).setChecked(true);
		} else {
			if (reportControl.getValue(InjuryTable.KEY_REFEREE).startsWith("1")) {
				((RadioButton) findViewById(R.id.irFreeKRd)).setChecked(true);
			} else if (reportControl.getValue(InjuryTable.KEY_REFEREE).startsWith("2")) {
				((RadioButton) findViewById(R.id.irYelCrdRd)).setChecked(true);
			} else if (reportControl.getValue(InjuryTable.KEY_REFEREE).startsWith("3")) {
				((RadioButton) findViewById(R.id.irRedCrdRd)).setChecked(true);
			}
			if (reportControl.getValue(InjuryTable.KEY_SANCTION_PLAYER).startsWith("1")) {
				((CheckBox) findViewById(R.id.irPlyrSanChk)).setChecked(true);
			}
			if (reportControl.getValue(InjuryTable.KEY_SANCTION_OPPONENT).startsWith("1")) {
				((CheckBox) findViewById(R.id.irOpponSanChk)).setChecked(true);
			}
			findViewById(R.id.irPlyrSanChk).setEnabled(true);
			findViewById(R.id.irOpponSanChk).setEnabled(true);
		}
	}

	public static <T, E> T getKey(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	private String idToField(int id) {
		switch (id) {
		case R.id.ir1a:
			return InjuryTable.KEY_DATE_OF_INJURY;
		case R.id.ir1b:
			return InjuryTable.KEY_DATE_OF_RETURN;
		case R.id.OrchCodeTxt:
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
			(findViewById(R.id.irOtherInjuryTxt)).setEnabled(true);
			(findViewById(R.id.irOrchTypeSpin)).setEnabled(false);

		} else {
			(findViewById(R.id.irOtherInjuryTxt)).setEnabled(false);
			(findViewById(R.id.irOrchTypeSpin)).setEnabled(true);
		}
	}// Recurrence

	public void onRecurrenceClick(View v) {
		if (((RadioButton) findViewById(R.id.irReOccurYesRd)).isChecked()) {
			(findViewById(R.id.irPreDORtext)).setEnabled(true);
		} else {
			(findViewById(R.id.irPreDORtext)).setEnabled(false);
		}
	}

	public void onContactCheckBoxClick(View v) {
		if (((CheckBox) findViewById(R.id.irNoContactChk)).isChecked()) {
			findViewById(R.id.irContactBallChk).setEnabled(false);
			findViewById(R.id.irContactPlyrChk).setEnabled(false);
			findViewById(R.id.irContactOtherChk).setEnabled(false);
			findViewById(R.id.irContactOtherTxt).setEnabled(false);
		} else {
			findViewById(R.id.irContactBallChk).setEnabled(true);
			findViewById(R.id.irContactPlyrChk).setEnabled(true);
			findViewById(R.id.irContactOtherChk).setEnabled(true);
			if (((CheckBox) findViewById(R.id.irContactOtherChk)).isChecked()) {
				findViewById(R.id.irContactOtherTxt).setEnabled(true);
			} else {
				findViewById(R.id.irContactOtherTxt).setEnabled(false);
			}

		}
	}

	public void onViolationRadioClick(View v) {
		RadioButton Radio = (RadioButton) v;
		if (Radio.getId() == R.id.irNoViolRd) {
			findViewById(R.id.irPlyrSanChk).setEnabled(false);
			findViewById(R.id.irOpponSanChk).setEnabled(false);
		} else {
			findViewById(R.id.irPlyrSanChk).setEnabled(true);
			findViewById(R.id.irOpponSanChk).setEnabled(true);
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
		reportControl.setValue(InjuryTable.KEY_ORCHARD, ((TextView) findViewById(R.id.OrchCodeTxt))
				.getText().toString());
		reportControl.setValue(idToField(R.id.ir1a), ((TextView) findViewById(R.id.ir1a)).getText()
				.toString());
		reportControl.setValue(idToField(R.id.ir1b), ((TextView) findViewById(R.id.ir1b)).getText()
				.toString());
		if (((CheckBox) findViewById(R.id.irOtherInjuryChk)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_DIAGNOSIS,
					((TextView) findViewById(R.id.irOtherInjuryTxt)).getText().toString());
		}
		if (((RadioButton) findViewById(R.id.irReOccurYesRd)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_PREVIOUS, "0");
			reportControl.setValue(InjuryTable.KEY_PREVIOUS_DATE,
					((TextView) findViewById(R.id.irPreDORtext)).getText().toString());
		}
		if (((RadioButton) findViewById(R.id.irReOccurNoRd)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_PREVIOUS, "1");
		}
		if (((RadioButton) findViewById(R.id.irOverRadBtn)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_OVERUSE_TRAUMA, "0");
		} else if (((RadioButton) findViewById(R.id.irTrauRadBtn)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_OVERUSE_TRAUMA, "1");
		}
		if (((RadioButton) findViewById(R.id.irTrainInjRad)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_TRAINING_MATCH, "0");
		} else if (((RadioButton) findViewById(R.id.irMatchInjRad)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_TRAINING_MATCH, "1");
		}
		if (((CheckBox) findViewById(R.id.irNoContactChk)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_CONTACT_NO, "1");
		} else {
			if (((CheckBox) findViewById(R.id.irContactBallChk)).isChecked()) {
				reportControl.setValue(InjuryTable.KEY_CONTACT_BALL, "1");
			}
			if (((CheckBox) findViewById(R.id.irContactPlyrChk)).isChecked()) {
				reportControl.setValue(InjuryTable.KEY_CONTACT_PLAYER, "1");
			}
			if (((CheckBox) findViewById(R.id.irContactOtherChk)).isChecked()) {
				reportControl.setValue(InjuryTable.KEY_CONTACT_OTHER,
						((TextView) findViewById(R.id.irContactOtherTxt)).getText().toString());
			}
		}
		if (((RadioButton) findViewById(R.id.irNoViolRd)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_REFEREE, "0");
		} else if (((RadioButton) findViewById(R.id.irFreeKRd)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_REFEREE, "1");
		} else if (((RadioButton) findViewById(R.id.irYelCrdRd)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_REFEREE, "2");
		} else if (((RadioButton) findViewById(R.id.irRedCrdRd)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_REFEREE, "3");
		}
		if (((CheckBox) findViewById(R.id.irPlyrSanChk)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_SANCTION_PLAYER, "1");
		} else if (((CheckBox) findViewById(R.id.irOpponSanChk)).isChecked()) {
			reportControl.setValue(InjuryTable.KEY_SANCTION_OPPONENT, "1");
		}
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
