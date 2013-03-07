package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class LineChart extends AbstractDemoChart {
	private XYMultipleSeriesRenderer renderer;
	private List<double[]> x = new ArrayList<double[]>();
	private List<double[]> y;
	private String[] lineNames;
	private int lineCount;
	int[] colors = new int[] { Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW };
	PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
			PointStyle.TRIANGLE, PointStyle.SQUARE };

	public LineChart(String playerName, String chartName, List<String> lineNames,
			List<double[]> values) {
		lineCount = lineNames.size();
		this.lineNames = (String[]) lineNames.toArray();
		for (int i = 0; i < lineCount; i++) {
			double[] week = new double[values.get(i).length];
			for (int j = 0; j < values.get(i).length; j++) {
				week[j] = j;
			}
			x.add(week);
		}
		this.y = values;
		int colors[] = Arrays.copyOfRange(this.colors, 0, lineCount);
		PointStyle[] styles = Arrays.copyOfRange(this.styles, 0, lineCount);
		renderer = buildRenderer(colors, styles);
		for (int i = 0; i < lineCount; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		}
		setChartSettings(renderer, playerName, "Week Number", chartName, 0, values.get(0).length,
				50, 150, Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(x.get(0).length);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setApplyBackgroundColor(true);
		renderer.setMargins(new int[] { 30, 50, 5, 1 });
		renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { -0.5, 55, 0, 300 });
		renderer.setZoomLimits(new double[] { -0.5, 55, 0, 300 });
	}

	public GraphicalView getChart(Context context) {
		return ChartFactory.getLineChartView(context, buildDataset(lineNames, x, y), renderer);
	}

	public Intent getIntent(Context context) {
		return ChartFactory.getLineChartIntent(context, buildDataset(lineNames, x, y), renderer);
	}
}
