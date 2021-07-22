package com.yc.jetpacklib.widget.chart

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycToNoEmpty
import com.yc.jetpacklib.widget.chart.data.ChartData

/**
 * 首页考勤
 */
class ChartLineMarkerView(context: Context?, chart: Chart<*>?) : MarkerView(context, R.layout.yc_chart_marker_view) {
    private val mItem1Tv: TextView = findViewById(R.id.chartMakerItem1Tv)
    private val mItem2Tv: TextView = findViewById(R.id.chartMakerItem2Tv)
    private var mChartBarData: ChartData? = null
    fun setChartBarData(chartBarData: ChartData?) {
        mChartBarData = chartBarData
    }

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        if (mChartBarData != null) {
            val xIndex = e.x.toInt()
            mItem1Tv.text = "时间：" + mChartBarData!!.xData[xIndex].ycToNoEmpty("-")
            mItem2Tv.text = "出勤人数：" + mChartBarData!!.yData[xIndex].toInt() + " 人"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        chartView = chart
    }
}