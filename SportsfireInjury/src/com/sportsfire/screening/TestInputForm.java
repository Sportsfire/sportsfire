package com.sportsfire.screening;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sportsfire.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TestInputForm extends Activity {
	public static final String ARG_ITEM_TESTS = "argumentTest";
	public static final String ARG_ITEM_TESTVAL = "argumentTestValue";
	public static final String ARG_ITEM_DATA = "argumentScreen";
	FormValues values;
	Map<String, Integer> TestsMap = new HashMap<String, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_input_form);
		List test = getIntent().getParcelableExtra(ARG_ITEM_TESTS);
		values = getIntent().getParcelableExtra(ARG_ITEM_DATA);
		TableRow headRow = new TableRow(this);
		for (String heading : values.getHeader()) {
			TextView cell = new TextView(this);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			layout.setMargins(1, 1, 1, 1);
			cell.setLayoutParams(layout);
			if (heading.compareTo("Full Name")==0)
			cell.setBackgroundColor(Color.parseColor("#ffcccccc"));
			cell.setBackgroundColor(Color.WHITE);
			cell.setGravity(Gravity.CENTER_HORIZONTAL);
			cell.setTextColor(Color.BLACK);
			cell.setText(heading);
			headRow.addView(cell);
		}
		((TableLayout) findViewById(R.id.headerTable)).addView(headRow);
		TableRow dummyRow = new TableRow(this);
		for (String heading : values.getHeader()) {
			TextView cell = new TextView(this);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 0, 1.0f);
			layout.setMargins(1, 0, 1, 0);
			cell.setText(heading);
			dummyRow.addView(cell);
		}
		((TableLayout) findViewById(R.id.mainTable)).addView(dummyRow);
		for (List<String> row : values.getValues()) {
			TableRow bodyRow = new TableRow(this);
			for (String value: row){
				TextView cell = new TextView(this);
				LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
				layout.setMargins(1, 0, 1, 0);
				cell.setText(value);
				bodyRow.addView(cell);
			}
			((TableLayout) findViewById(R.id.mainTable)).addView(bodyRow);
		}
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
