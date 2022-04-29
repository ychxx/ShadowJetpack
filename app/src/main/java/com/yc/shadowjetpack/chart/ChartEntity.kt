package com.yc.shadowjetpack.chart

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import com.yc.jetpacklib.widget.chart.YcEntry

/**
 * Creator: yc
 * Date: 2021/8/17 15:36
 * UseDes:
 */
data class ChartEntity(
    val lineXList: List<String>,
    val lineEntryList: List<YcEntry>,
    val lineLimitLineY: Float,
    val lineYMax: Float,
    val barXList: List<String>,
    val barEntryList: List<BarEntry>,
    val barEntryList2: List<BarEntry>,
    val barYMax: Float,

    val pieXList: List<String>,
    val pieEntryList: List<PieEntry>,
    val pieYMax: Float
)