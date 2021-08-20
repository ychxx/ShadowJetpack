package com.yc.jetpacklib.widget.chart

import com.github.mikephil.charting.data.Entry

/**
 * 多一个是否绘制顶点参数
 */
class YcEntry : Entry {
    constructor(x: Float, y: Float, isDrawCircles: Boolean = true) : super(x, y, isDrawCircles)
    constructor(x: Int, y: Float, isDrawCircles: Boolean = true) : this(x.toFloat(), y, isDrawCircles)
}