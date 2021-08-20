package com.yc.jetpacklib.widget.chart

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.yc.jetpacklib.R
import com.yc.jetpacklib.databinding.YcChartMarkerItemBinding
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycSetTextColorRes
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapterPlus

/**
 * 通用图表的标记图层
 */
@SuppressLint("ViewConstructor")
open class YcChartMarkerView(chart: Chart<*>) : YcChartBaseMarkerView<YcMarkerEntity>(chart) {
    var mUpdateData: (YcRecyclerViewAdapterPlus<YcMarkerEntity, YcChartMarkerItemBinding>.(index: Int) -> Unit)? = null

    init {
        mAdapter.mOnUpdate = {
            if (it.bgResId == null) {
                vChartMarkerItem.visibility = GONE
            } else {
                vChartMarkerItem.visibility = visibility
                vChartMarkerItem.setBackgroundResource(it.bgResId)
            }
            tvChartMakerItem.text = it.text
            tvChartMakerItem.ycSetTextColorRes(it.textColorResId)
        }
    }

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        mAdapter.clearData()
        val xIndex = e.x.toInt()
        mUpdateData?.invoke(mAdapter, xIndex)
        mAdapter.notifyDataSetChanged()
        super.refreshContent(e, highlight)
    }

}