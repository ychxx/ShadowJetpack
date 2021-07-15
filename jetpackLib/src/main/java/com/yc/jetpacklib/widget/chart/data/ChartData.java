package com.yc.jetpacklib.widget.chart.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChartData {
    private double mMaxY = 0;
    private List<Double> mYData = new ArrayList<>();
    private List<String> mXData = new ArrayList<>();

    public double getMaxY() {
        return mMaxY;
    }

    public void setMaxY(double maxY) {
        this.mMaxY = maxY;
    }


    public List<Double> getYData() {
        return mYData;
    }

    public void setYData(List<Double> yData) {
        this.mYData = yData;
    }

    public void addYData(double yData) {
        this.mYData.add(yData);
    }

    public List<String> getXData() {
        return mXData;
    }

    public void setXData(List<String> xData) {
        this.mXData = xData;
    }

    public void addXData(String xData) {
        this.mXData.add(xData);
    }

    public void clear() {
        mYData.clear();
        mXData.clear();
    }
}
