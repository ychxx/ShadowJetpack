package com.yc.jetpacklib.widget.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

/**
 * 折线图里的自定义x轴
 */

public class ChartAxisXTime extends ValueFormatter {
    private List<String> mValues;

    //    private final List<String> DEFAULT_VALUES = Arrays.asList("")
    public ChartAxisXTime(List<String> values) {
        this.mValues = values;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (value >= mValues.size() || value < 0) {
            return "";
        } else {
            String xData = mValues.get((int) value);
            if (xData.length() > 4) {
                xData = xData.substring(0, 3) + "...";
            }
            return xData;
        }
    }
}
