package com.yc.shadowjetpack.chart

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import com.yc.jetpacklib.extension.ycIsEmpty
import com.yc.jetpacklib.mapper.IMapper
import com.yc.jetpacklib.widget.chart.YcEntry

/**
 * Creator: yc
 * Date: 2021/8/17 15:36
 * UseDes:
 */
class ChartJsonToEntity : IMapper<ChartJson?, ChartEntity?> {
    override fun map(input: ChartJson?): ChartEntity? {
        return if (input == null || input.xList.ycIsEmpty() || input.yList.ycIsEmpty()) {
            null
        } else {
            val entry: MutableList<YcEntry> = mutableListOf()
            val barEntry: MutableList<BarEntry> = mutableListOf()
            val barEntry2: MutableList<BarEntry> = mutableListOf()
            val pieEntry: MutableList<PieEntry> = mutableListOf()
            var yMax = 0f
            for ((i, item) in input.yList!!.withIndex()) {
                //间隔5，且最后一个定点也显示
                barEntry.add(BarEntry(i.toFloat(), item))
                barEntry2.add(BarEntry(i.toFloat(), item / 2))
                pieEntry.add(PieEntry(i.toFloat(), item))
                entry.add(YcEntry(i.toFloat(), item, (i == input.yList.size - 1) || i % 5 == 0))
                if (item > yMax) {
                    yMax = item
                }
            }
            ChartEntity(input.xList!!, entry, yMax / 2, yMax, input.xList, barEntry, barEntry2, yMax, input.xList, pieEntry, yMax)
        }
    }
}