package com.yc.jetpacklib.utils

import androidx.annotation.ColorInt

/**
 * Creator: yc
 * Date: 2021/8/27 15:27
 * UseDes:颜色工具类
 */
object YcColorUtil {
    @ColorInt
    fun rgb(red: Int, green: Int, blue: Int): Int {
        return -0x1000000 or (red shl 16) or (green shl 8) or blue
    }

    @ColorInt
    fun argb(alpha: Int, red: Int, green: Int, blue: Int): Int {
        return alpha shl 24 or (red shl 16) or (green shl 8) or blue
    }
}