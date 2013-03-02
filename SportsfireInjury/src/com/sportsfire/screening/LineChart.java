package com.sportsfire.screening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class LineChart extends AbstractDemoChart{
	private XYMultipleSeriesRenderer renderer; 
	private List<double[]> x = new ArrayList<double[]>();
	private List<double[]> y;
	private String[] lineNames;
	private Context context;
	private int lineCount;
	int[] colors = new int[] { Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW };
	PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
			PointStyle.TRIANGLE, PointStyle.SQUARE };
	public LineChart(Context context, String playerName, String chartName, List<String> lineNames, List<double[]> values){
		lineCount = lineNames.size();
		this.context = context;
		this.lineNames = (String[]) lineNames.toArray();
		for (int i = 0; i < lineCount; i++) {
			double[] week = new double[values.get(i).length];
			for (int j = 0; j < values.get(i).length; j++){
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
		setChartSettings(renderer, playerName, "Week Number", chartName, 0, values.get(0).length+1, 50,
				200, Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(26);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { -5, 55, 0, 300 });
		renderer.setZoomLimits(new double[] { -5, 55, 0, 300 });
	}
	public GraphicalView getChart(){
		return ChartFactory.getLineChartView(context, buildDataset(lineNames, x, y), renderer);
	}
}
