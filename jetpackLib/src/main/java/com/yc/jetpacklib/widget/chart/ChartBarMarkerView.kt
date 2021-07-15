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
import com.yc.jetpacklib.widget.chart.data.ChartBarData

/**
 * 首页工种分布柱状图的标记图层
 */
class ChartBarMarkerView(context: Context?, chart: Chart<*>) : MarkerView(context, R.layout.yc_chart_marker_view) {
    private val mItem1Tv: TextView
    private val mItem2Tv: TextView
    private val mItem3Tv: TextView
    private var mChartBarData: ChartBarData? = null
    fun setChartBarData(chartBarData: ChartBarData?) {
        mChartBarData = chartBarData
    }

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        if (mChartBarData != null) {
            val xIndex = e.x.toInt()
            mItem1Tv.text = "工种：" + mChartBarData!!.xData[xIndex].ycToNoEmpty("-")
            mItem2Tv.text = "未出勤：" + mChartBarData!!.yData1[xIndex].toInt() + " 人"
            mItem3Tv.text = "已出勤：" + mChartBarData!!.yData2[xIndex].toInt() + " 人"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        mItem1Tv = findViewById(R.id.chartMakerItem1Tv)
        mItem2Tv = findViewById(R.id.chartMakerItem2Tv)
        mItem3Tv = findViewById(R.id.chartMakerItem3Tv)
        chartView = chart
    }
}