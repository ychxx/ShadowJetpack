package com.yc.jetpacklib.widget.chart

import android.annotation.SuppressLint
import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.hcc.plugindust.sindi.YcDINTextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycIsNotEmpty

/**
 *
 */
class ChartLineMarkerViewMonitor private constructor(context: Context, layoutResource: Int, unit: String, threshold: String, name: String) :
    MarkerView(context, layoutResource) {
    private val mContentTv: YcDINTextView
    private val mThresholdTv: YcDINTextView
    private var mUnit = ""
    private var mThreshold = ""
    private var mName = ""

    constructor(context: Context) : this(context, R.layout.yc_chart_marker_view_monitor, "", "", "")

    fun setThreshold(threshold: String) {
        mThreshold = threshold
    }

    fun setName(name: String) {
        mName = name
    }

    fun setUnit(unit: String) {
        mUnit = unit
    }

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        mThresholdTv.text = "阈值: + ${mThreshold.ycIsNotEmpty()} $mUnit"
        mContentTv.text = "$mName : ${e.y} $mUnit"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        mUnit = unit
        mThreshold = threshold
        mName = name
        mThresholdTv = findViewById(R.id.chartMakerThresholdTv)
        mContentTv = findViewById(R.id.chartMakerContentTv)
    }
}