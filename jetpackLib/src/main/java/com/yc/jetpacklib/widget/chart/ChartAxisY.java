package com.yc.jetpacklib.widget.chart;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 折线图里的自定义x轴
 */

public class ChartAxisY extends ValueFormatter {
    private List<String> mValues;

    //    private final List<String> DEFAULT_VALUES = Arrays.asList("")
    public ChartAxisY(List<String> values) {
        this.mValues = values;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (value >= mValues.size() || value < 0) {
            return "";
        } else {
            String xData = mValues.get((int) value);
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(xData);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                xData = new SimpleDateFormat("MM.dd").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return xData;
        }
    }
}
