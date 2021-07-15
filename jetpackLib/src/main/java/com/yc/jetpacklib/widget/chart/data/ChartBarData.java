package com.yc.jetpacklib.widget.chart.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChartBarData {
    private double mMaxY = 0;
    private List<Double> mYData1 = new ArrayList<>();
    private List<Double> mYData2 = new ArrayList<>();
    private List<String> mXData = new ArrayList<>();

    public double getMaxY() {
        return mMaxY;
    }

    public void setMaxY(double maxY) {
        this.mMaxY = maxY;
    }

    public List<Double> getYData1() {
        return mYData1;
    }

    public void setYData1(List<Double> yData) {
        this.mYData1 = yData;
    }

    public void addYData1(double yData) {
        this.mYData1.add(yData);
    }


    public List<Double> getYData2() {
        return mYData2;
    }

    public void setYData2(List<Double> yData) {
        this.mYData2 = yData;
    }

    public void addYData2(double yData) {
        this.mYData2.add(yData);
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
        mYData1.clear();
        mYData2.clear();
        mXData.clear();
    }
}
