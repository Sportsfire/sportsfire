package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.sportsfire.Player;

public class AnalysisGraphPage extends Activity {
	private GraphicalView mChart;
	public static final String ARG_ITEM_PLAYER = "argumentPlayer";
	public static final String ARG_ITEM_SEASON = "argumentSeasonID";
	public static final String ARG_ITEM_TESTS = "argumentTest";
	private Player player;
	private String season;
	private ArrayList<String> testNames = new ArrayList<String>();
	private ArrayList<Integer> testParams = new ArrayList<Integer>();
	private ScreeningData screen;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		player = getIntent().getParcelableExtra(ARG_ITEM_PLAYER);
		season = getIntent().getStringExtra(ARG_ITEM_SEASON);
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> map = (HashMap<String, Integer>) getIntent().getSerializableExtra(ARG_ITEM_TESTS);
			for (Entry<String, Integer> set : map.entrySet()) {
				testNames.add(set.getKey());
				testParams.add(set.getValue());
			}
			setContentView(R.layout.line_graph);
			screen = new ScreeningData(this, season);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private GraphicalView getPlayerStats(int pos) {
		ArrayList<double[]> values = new ArrayList<double[]>();
		ArrayList<String> titles = new ArrayList<String>();
		titles.add(testNames.get(pos));
		ArrayList<Double> list = screen.getPlayerSeasonData(player.getID(), testNames.get(pos));
		double[] array = new double[list.size()];
		for (int j = 0; j < list.size(); j++) {
			array[j] = list.get(j);
		}
		values.add(array);
		titles.add(testNames.get(pos) + " - Player Average");
		array = new double[list.size()];
		for (int j = 0; j < list.size(); j++) {
			array[j] = Double.parseDouble(screen.getAverageValue(player.getID(), testNames.get(pos)));
		}
		values.add(array);
		if (testParams.get(pos) == 1) {
			titles.add(testNames.get(pos) + " - Squad Average");
			array = new double[list.size()];
			for (int j = 0; j < list.size(); j++) {
				array[j] = screen.getPlayerSquadAverageSeasonData(player.getID(), testNames.get(pos),
						Integer.toString(j));
			}
			values.add(array);
		}
		return new LineChart(this, player.getName() + " - " + testNames.get(pos), testNames.get(pos), titles, values)
				.getChart(this);
	}

	protected void onResume() {
		super.onResume();
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		LinearLayout layout2 = (LinearLayout) findViewById(R.id.chart2);
		if (mChart == null) {
			try {
				mChart = getPlayerStats(0);
				layout.addView(mChart);
				if (testNames.size() > 1) {
					layout2.addView(getPlayerStats(1));
				}
			} catch (Exception e) {
			}

		} else {
			mChart.repaint();
		}
	}

}