package com.yc.jetpacklib.widget.chart

import com.github.mikephil.charting.data.Entry

/**
 * 多一个是否绘制顶点参数
 */
class YcEntry(x: Float, y: Float, isDrawCircles: Boolean = true) : Entry(x, y, isDrawCircles) {

}