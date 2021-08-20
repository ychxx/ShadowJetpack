package com.yc.jetpacklib.widget.chart

/**
 * Creator: yc
 * Date: 2021/2/2 21:44
 * UseDes:
 */
data class YcMarkerEntity(
    /**
     * 为空时，左侧View不显示
     */
    val bgResId: Int?,
    val text: String,
    val textColorResId: Int
)