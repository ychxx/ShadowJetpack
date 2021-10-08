package com.yc.jetpacklib.widget.chart

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.utils.MPPointF
import com.yc.jetpacklib.R
import com.yc.jetpacklib.databinding.YcChartMarkerItemBinding
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter

/**
 * 通用图表的标记图层
 */
@SuppressLint("SetTextI18n", "ViewConstructor")
open class YcChartBaseMarkerView<T:Any>(chart: Chart<*>) : MarkerView(chart.context, R.layout.yc_chart_marker) {
    private val mRecyclerView: RecyclerView = findViewById(R.id.rvChartMaker)
    protected val mAdapter by lazy { YcRecyclerViewAdapter<T, YcChartMarkerItemBinding>(YcChartMarkerItemBinding::inflate) }

    init {
        chartView = chart
        mRecyclerView.ycInitLinearLayoutManage()
        mRecyclerView.adapter = mAdapter
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}