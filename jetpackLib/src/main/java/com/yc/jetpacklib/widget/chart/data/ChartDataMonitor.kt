package com.yc.jetpacklib.widget.chart.data

import java.util.*

/**
 *
 */
class ChartDataMonitor {
    private var mYData: MutableList<Double> = ArrayList()
    private var mXData: MutableList<String> = ArrayList()
    var threshold = "" //阈值
    val yData: List<Double>
        get() = mYData

    fun setYData(yData: MutableList<Double>) {
        mYData = yData
    }

    fun addYData(yData: Double) {
        mYData.add(yData)
    }

    val xData: List<String>
        get() = mXData

    fun setXData(xData: MutableList<String>) {
        mXData = xData
    }

    fun addXData(xData: String?) {
        if (xData != null)
            mXData.add(xData)
    }

    fun clear() {
        mYData.clear()
        mXData.clear()
        threshold = ""
    }
}