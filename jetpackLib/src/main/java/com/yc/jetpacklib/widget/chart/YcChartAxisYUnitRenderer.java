package com.yc.jetpacklib.widget.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;

/**
 * Creator: yc
 * Date: 2021/8/27 15:36
 * UseDes:
 */

public class YcChartAxisYUnitRenderer extends YAxisRenderer {
    private String mUnit = "单位(-)";

    public YcChartAxisYUnitRenderer(BarChart barChart, String unit) {
        super(barChart.getViewPortHandler(), barChart.getAxisLeft(), barChart.getTransformer(YAxis.AxisDependency.LEFT));
        mUnit = unit;
    }

    public YcChartAxisYUnitRenderer(LineChart lineChart, String unit) {
        super(lineChart.getViewPortHandler(), lineChart.getAxisLeft(), lineChart.getTransformer(YAxis.AxisDependency.LEFT));
        mUnit = unit;
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);
        // draw
        for (int i = from; i < to; i++) {
            String text = mYAxis.getFormattedLabel(i);
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }
        c.drawText(mUnit, fixedPosition, positions[(to - 1) * 2 + 1] + offset - 25, mAxisLabelPaint);
    }
}
