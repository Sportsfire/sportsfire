package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.sportsfire.Player;
import com.sportsfire.screening.R;
import com.sportsfire.Squad;

public class TestInputForm extends Activity {
	public static final String ARG_ITEM_TESTS = "argumentTest";
	public static final String ARG_ITEM_PARAM = "argumentParam";
	public static final String ARG_ITEM_SQUAD = "argumentSquad";
	public static final String ARG_ITEM_DATA = "argumentScreen";
	FormValues values;
	HashMap<String, Integer> TestsMap;
	Squad Asquad;
	ArrayList<Player> squad;
	String[] params;

	private void setCellStyle(TextView cell) {
		LayoutParams layout = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
		layout.setMargins(1, 1, 1, 1);
		cell.setLayoutParams(layout);
		cell.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
		cell.setBackgroundColor(Color.WHITE);
		cell.setGravity(Gravity.CENTER_HORIZONTAL);
		cell.requestLayout();
	}

	private TableRow createDummyRow() {
		TableRow dummyRow = new TableRow(this);
		for (String heading : values.getDummy()) {
			TextView cell = new EditText(this);
			LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, 0, 1.0f);
			layout.setMargins(1, 1, 1, 1);
			cell.setLayoutParams(layout);
			cell.setText(heading);
			cell.setTextAppearance(this, android.R.style.TextAppearance_Holo_Large);
			cell.setFocusable(false);
			dummyRow.addView(cell);
		}
		return dummyRow;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_input_form);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		values = getIntent().getParcelableExtra(ARG_ITEM_DATA);
		TestsMap = (HashMap<String, Integer>) getIntent().getSerializableExtra(ARG_ITEM_TESTS);
		params = getIntent().getStringArrayExtra(ARG_ITEM_PARAM);
		squad = (ArrayList<Player>) getIntent().getSerializableExtra(ARG_ITEM_SQUAD);

		TableRow headRow = new TableRow(this);
		for (String heading : values.getHeader()) {
			TextView cell = new TextView(this);
			setCellStyle(cell);
			if (heading.compareTo("Full Name") == 0) {
				cell.setBackgroundColor(Color.parseColor("#ffcccccc"));
			}
			cell.setTextAppearance(this, android.R.style.TextAppearance_Holo_Large);
			cell.setText(heading);
			headRow.addView(cell);
		}
		((TableLayout) findViewById(R.id.headerTable)).addView(headRow);
		((TableLayout) findViewById(R.id.headerTable)).addView(createDummyRow());
		((TableLayout) findViewById(R.id.mainTable)).addView(createDummyRow());

		String week = params[1].substring(5); // we need the number of the week, without "Week "
		
		final ScreeningData screenData = new ScreeningData(this, params[0], week);
		for (final Player player : squad) {
			TableRow bodyRow = new TableRow(this);
			TextView cell = new TextView(this);
			setCellStyle(cell);
			cell.setBackgroundColor(Color.parseColor("#ffcccccc"));
			// cell.setFocusable(false);
			cell.setText(player.getName());
			bodyRow.addView(cell);
			for (final Entry<String, Integer> test : TestsMap.entrySet()) {
				EditText tCell = new EditText(this);

				setCellStyle(tCell);
				tCell.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				tCell.setText(screenData.getValue(player.getID(), test.getKey()));
				tCell.setOnFocusChangeListener(new OnFocusChangeListener(){

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if(!hasFocus){
							// TODO Auto-generated method stub
							screenData.setValue(player.getID(), test.getKey(), ((EditText)v).getText().toString());
							System.out.println(player.getID() + " " + test.getKey());
					    }
						
					}
					});
				
				bodyRow.addView(tCell);
				TextView aCell = new TextView(this);
				TextView pCell = new TextView(this);
				setCellStyle(aCell);
				setCellStyle(pCell);
				aCell.setBackgroundColor(Color.YELLOW);
				pCell.setBackgroundColor(Color.CYAN);
				if (test.getValue() == 0) {
					aCell.setText(screenData.getAverageValue(player.getID(), test.getKey()));
					pCell.setText(screenData.getPreviousValue(player.getID(), test.getKey()));
					bodyRow.addView(aCell);
					bodyRow.addView(pCell);
				} else if (test.getValue() == 1) {
					pCell.setText(screenData.getPreviousValue(player.getID(), test.getKey()));
					bodyRow.addView(pCell);
				} else if (test.getValue() == 2) {
					aCell.setText(screenData.getAverageValue(player.getID(), test.getKey()));
					bodyRow.addView(aCell);
				}
			}
			((TableLayout) findViewById(R.id.mainTable)).addView(bodyRow);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_input_form, menu);
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
}
