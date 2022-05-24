package com.yc.jetpacklib.widget.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.yc.jetpacklib.extension.ycIsEmpty
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 有限长度的格式化
 * 当数据大于maxLength时，（maxLength-1）之后数据用省略号表示
 */
class YcChartFiniteLength(private val maxLength: Int = 5) : ValueFormatter() {
    var mValues: List<String> = listOf()
    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return if (mValues.ycIsEmpty() || mValues.size <= value) {
            ""
        } else {
            sub(mValues[abs(value).roundToInt() % mValues.size])
        }
    }

    protected fun sub(data: String) = if (data.length > maxLength) {
        data.substring(0, maxLength - 1) + "..."
    } else {
        data
    }
}