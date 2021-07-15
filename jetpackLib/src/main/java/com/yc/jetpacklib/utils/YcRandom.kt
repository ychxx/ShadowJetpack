package com.yc.jetpacklib.utils

import java.util.*

/**
 * Creator: yc
 * Date: 2021/7/9 17:17
 * UseDes:
 */
object YcRandom {
    /**
     * 生成一个[min,max]范围的随机数(包含min和max)
     *
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    @JvmStatic
    fun getInt(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt(max - min + 1) + min //nextInt(x)的范围是[0,x)不包含x，故+1
    }

    /**
     * 生成n位的整数
     *
     * @param n 整数的位数
     */
    @JvmStatic
    fun getInt(n: Int): Int {
        val min = Math.pow(10.0, n.toDouble()).toInt() / 10
        val max = min * 10 - 1
        return getInt(min, max)
    }

    /**
     * 生成n位的字符串
     *
     * @param n 整数的位数
     */
    @JvmStatic
    fun getString(n: Int): String {
        return getInt(n).toString() + ""
    }

    /**
     * 生成一个png格式的图片名
     *
     * @return
     */
    @JvmStatic
    val nameImgOfPNG: String
        get() = System.currentTimeMillis().toString() + "_" + getString(3) + ".png"

    /**
     * 生成一个JPG格式的图片名
     *
     * @return
     */
    @JvmStatic
    val nameImgOfJPG: String
        get() = System.currentTimeMillis().toString() + "_" + getString(3) + ".jpg"
}