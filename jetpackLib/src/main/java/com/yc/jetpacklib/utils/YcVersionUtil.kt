package com.yc.jetpacklib.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * app版本相关信息
 */
object YcVersionUtil {
    /**
     * 获取版本号
     */
    @JvmStatic
    fun getVersionCode(context: Context): Long {
        try {
            val packageInfo: PackageInfo? = context.packageManager.getPackageInfo(context.packageName, 0)
            if (packageInfo != null) {
                return packageInfo.longVersionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 获取版本名
     */
    @JvmStatic
    fun getVersionName(context: Context): String {
        try {
            val packageInfo: PackageInfo? = context.packageManager.getPackageInfo(context.packageName, 0)
            if (packageInfo != null) {
                return packageInfo.versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取包名
     */
    @JvmStatic
    fun getPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}