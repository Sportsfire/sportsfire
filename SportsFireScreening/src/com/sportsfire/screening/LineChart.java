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
import android.util.DisplayMetrics;

public class LineChart extends AbstractDemoChart {
	private XYMultipleSeriesRenderer renderer;
	private List<double[]> x = new ArrayList<double[]>();
	private List<double[]> y;
	private String[] lineNames;
	private int lineCount;
	int[] colors = new int[] { Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED };
	PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
			PointStyle.TRIANGLE, PointStyle.SQUARE };

	public LineChart(Context context, String playerName, String chartName, ArrayList<String> lineNames,
			List<double[]> values) {
		lineCount = lineNames.size();
		this.lineNames = lineNames.toArray(new String[lineNames.size()]);
		for (int i = 0; i < lineCount; i++) {
			double[] week = new double[values.get(i).length];
			for (int j = 0; j < values.get(i).length; j++) {
				week[j] = j+1;
			}
			x.add(week);
		}
		this.y = values;
		int colors[] = Arrays.copyOfRange(this.colors, 0, lineCount);
		PointStyle[] styles = Arrays.copyOfRange(this.styles, 0, lineCount);
		renderer = buildRenderer(colors, styles);
		for (int i = 0; i < lineCount; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setLineWidth(3f);
		}
		double yMin = y.get(0)[0]*0.8;
		double yMax = y.get(0)[y.get(0).length-1]*1.1;
		setChartSettings(renderer, playerName, "Week Number", chartName, 0.5, values.get(0).length+0.5,
				yMin, yMax, Color.BLACK, Color.BLACK);
		renderer.setXLabels(x.get(0).length+1);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(context.getResources().getColor(
				android.R.color.background_light));
		renderer.setMarginsColor(context.getResources().getColor(android.R.color.background_light));
		renderer.setShowGrid(true);
		renderer.setGridColor(context.getResources().getColor(android.R.color.tertiary_text_light));
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
	    switch (context.getResources().getDisplayMetrics().densityDpi) {
	        case DisplayMetrics.DENSITY_XHIGH:
	        	renderer.setAxisTitleTextSize(24);
                renderer.setChartTitleTextSize(28);
                renderer.setLabelsTextSize(24);
                renderer.setLegendTextSize(24);
                break;
	        case DisplayMetrics.DENSITY_HIGH:
                renderer.setAxisTitleTextSize(20);
                renderer.setChartTitleTextSize(24);
                renderer.setLabelsTextSize(20);
                renderer.setLegendTextSize(20);
                break;
	       default:
	    	   renderer.setAxisTitleTextSize(20);
               renderer.setChartTitleTextSize(24);
               renderer.setLabelsTextSize(20);
               renderer.setLegendTextSize(18);
               break;
	    }
		renderer.setMargins(new int[] { 30, 60, 10, 10 });
		renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { 0.5, 52, 0, yMax });
		renderer.setZoomLimits(new double[] {0.5, 52, 0, yMax });
	}

	public GraphicalView getChart(Context context) {
		return ChartFactory.getLineChartView(context, buildDataset(lineNames, x, y), renderer);
	}

	public Intent getIntent(Context context) {
		return ChartFactory.getLineChartIntent(context, buildDataset(lineNames, x, y), renderer);
	}
}
