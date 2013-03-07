package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.util.MathHelper;

import com.sportsfire.Player;
import com.sportsfire.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AnalysisGraphPage extends Activity {
	private GraphicalView mChart;
	public static final String ARG_ITEM_PLAYER = "argumentPlayer";
	private Player player;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		player = getIntent().getParcelableExtra(ARG_ITEM_PLAYER);
		setContentView(R.layout.line_graph);

	}

	protected void onResume() {
		super.onResume();
		String[] titles = new String[] { "Crete", "Corfu", "Thassos", "Skiathos" };
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
				13.9 });
		values.add(new double[] { 10, MathHelper.NULL_VALUE, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
		values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
		values.add(new double[] { 9, MathHelper.NULL_VALUE, 11, MathHelper.NULL_VALUE, 19, 23, 26, 25, 22, 18, 13, 10 });
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		LinearLayout layout2 = (LinearLayout) findViewById(R.id.chart2);
		if (mChart == null) {
			LineChart a = new LineChart("Darth Vader", "Weight (kg)", Arrays.asList(titles), values);
			mChart = a.getChart(this);
			layout.addView(mChart);
			layout2.addView(a.getChart(this));
		} else {
			mChart.repaint();
		}
	}

}