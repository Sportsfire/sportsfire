package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.sportsfire.Player;
import com.sportsfire.Squad;

public class TestInputForm extends Activity {
	public static final String ARG_ITEM_TESTS = "argumentTest";
	public static final String ARG_ITEM_PARAM = "argumentParam";
	public static final String ARG_ITEM_SQUAD = "argumentSquad";
	public static final String ARG_ITEM_DATA = "argumentScreen";
	private FormValues values;
	private LinkedHashMap<String, String> TestsMap;
	private ArrayList<String> testList;
	private Squad squad;
	private Bundle bundle;
	private String[] params;
	private ScreeningData screen;

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

	// @SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screening_input_form);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		values = getIntent().getParcelableExtra(ARG_ITEM_DATA);
		bundle = getIntent().getBundleExtra(ARG_ITEM_TESTS);
		testList = bundle.getStringArrayList(ARG_ITEM_DATA);
		params = getIntent().getStringArrayExtra(ARG_ITEM_PARAM);
		squad = getIntent().getParcelableExtra(ARG_ITEM_SQUAD);

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
		FillTable task = new FillTable();
		task.setContext(this);
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_input_form, menu);
		getActionBar()
				.setTitle("Test Input Form - " + squad.getSquadName() + ": Season " + params[0] + " " + params[1]);
		return true;
	}

	public void onPause() {
		super.onPause();
		screen.closeCursor();
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

	private class FillTable extends AsyncTask<Void, TableRow, Void> {
		private Context context;

		// private TableRow bodyRow;
		public void setContext(Context contextf) {
			context = contextf;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				String week = params[1].substring(5);
				final ScreeningData screenData = new ScreeningData(context, params[0], week);
				screen = screenData;
				for (final Player player : squad.getPlayerList()) {
					TableRow bodyRow = new TableRow(context);
					TextView cell = new TextView(context);
					setCellStyle(cell);
					cell.setBackgroundColor(Color.parseColor("#ffcccccc"));
					// cell.setFocusable(false);
					cell.setText(player.getName());
					bodyRow.addView(cell);
					for (final String test : testList) {
						EditText tCell = new EditText(context);
						final TextView aCell = new TextView(context);
						final TextView pCell = new TextView(context);
						setCellStyle(aCell);
						setCellStyle(pCell);
						aCell.setBackgroundColor(Color.YELLOW);
						pCell.setBackgroundColor(Color.CYAN);
						setCellStyle(tCell);
						tCell.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
						tCell.setText(screenData.getValue(player.getID(), test));
						tCell.setOnFocusChangeListener(new OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									String textInput = ((EditText) v).getText().toString();
									if (textInput.compareTo("") == 0) {
										((EditText) v).setText("0.00");
									}
									screenData.setValue(player.getID(), test, textInput);
									System.out.println(player.getID() + " " + test);
									aCell.setText(screenData.getAverageValue(player.getID(), test));
									try {
										Double current = Double.parseDouble(textInput);
										Double pre = Double.parseDouble(pCell.getText().toString());
										if (current > pre * 1.2 || current < pre * 0.8) {
											((EditText) v).setTextColor(Color.RED);
										}
									} catch (Exception e) {
										// TODO: handle exception
									}

								}

							}
						});
						tCell.setOnKeyListener(new OnKeyListener() {

							@Override
							public boolean onKey(View v, int keyCode, KeyEvent event) {
								if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
									String textInput = ((EditText) v).getText().toString();
									if (textInput.compareTo("") == 0) {
										((EditText) v).setText("0.00");
									}
									screenData.setValue(player.getID(), test, textInput);
									aCell.setText(screenData.getAverageValue(player.getID(), test));
									try {
										Double current = Double.parseDouble(textInput);
										Double pre = Double.parseDouble(pCell.getText().toString());
										if (current > pre * 1.2 || current < pre * 0.8) {
											((EditText) v).setTextColor(Color.RED);
										}
									} catch (Exception e) {
										// TODO: handle exception
									}
									return true;
								}
								return false;
							}

						});

						bodyRow.addView(tCell);
						String testType = bundle.getString(test);
						if (testType.compareTo("Show previous and average") == 0) {
							aCell.setText(screenData.getAverageValue(player.getID(), test));
							Log.e("avg", screenData.getAverageValue(player.getID(), test));
							pCell.setText(screenData.getPreviousValue(player.getID(), test));
							bodyRow.addView(aCell);
							bodyRow.addView(pCell);
						} else if (testType.compareTo("Show previous") == 0) {
							pCell.setText(screenData.getPreviousValue(player.getID(), test));
							bodyRow.addView(pCell);
						} else if (testType.compareTo("Show average") == 0) {
							aCell.setText(screenData.getAverageValue(player.getID(), test));
							bodyRow.addView(aCell);
						}
						try {
							Double current = Double.parseDouble(tCell.getText().toString());
							Double pre = Double.parseDouble(pCell.getText().toString());
							if (current > pre * 1.2 || current < pre * 0.8) {
								tCell.setTextColor(Color.RED);
							}
						} catch (Exception e) {
						}
					}

					// ((TableLayout)
					// findViewById(R.id.mainTable)).addView(bodyRow);
					publishProgress(bodyRow);
				}

			} catch (Exception e) {
				Log.e("UpdateAPP", "Update error! " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(TableRow... progress) {
			((TableLayout) findViewById(R.id.mainTable)).addView(progress[0]);
			return;
		}

		@Override
		protected void onPostExecute(Void result) {
			return;
			// On a successful authentication, call back into the Activity to
			// communicate the authToken (or null for an error).
			// ((TableLayout) findViewById(R.id.mainTable)).
		}

	}
}
