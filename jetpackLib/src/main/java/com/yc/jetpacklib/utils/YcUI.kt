package com.yc.jetpacklib.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.yc.jetpacklib.init.YcJetpack

/**
 *
 */
object YcUI {
    /**
     * 将dp值转换为px值
     */
    @JvmStatic
    fun dpToPx(dp: Float): Int {
        return (YcJetpack.mInstance.getResources().displayMetrics.density * dp + 0.5f).toInt()
    }

    /**
     * 将px值转换为dp值
     */
    @JvmStatic
    fun pxToDp(px: Float): Int {
        return (px / YcJetpack.mInstance.getResources().displayMetrics.density + 0.5f).toInt()
    }

    /**
     * sp转px
     */
    @JvmStatic
    fun spToPx(sp: Float): Int {
        return (sp * YcJetpack.mInstance.getResources().displayMetrics.scaledDensity + 0.5f).toInt()
    }

    /**
     * px转sp
     */
    @JvmStatic
    fun pxToSp(px: Float): Int {
        return (YcJetpack.mInstance.getResources().displayMetrics.scaledDensity / px + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度 像素值
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        return getDisplayMetrics().widthPixels
    }

    /**
     * 获取屏幕高度 像素值
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        return getDisplayMetrics().heightPixels
    }

    @JvmStatic
    fun getDisplayMetrics(): DisplayMetrics {
        return getDisplayMetrics(YcJetpack.mInstance.mApplication)
    }

    @JvmStatic
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

}