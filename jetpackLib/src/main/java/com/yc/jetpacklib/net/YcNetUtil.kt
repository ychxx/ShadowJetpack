package com.yc.jetpacklib.net

import android.content.Context
import android.net.ConnectivityManager
import com.yc.jetpacklib.init.YcJetpack

/**
 *
 */
object YcNetUtil {
    fun isNetworkAvailable(): Boolean {
        val manager = YcJetpack.mInstance.mApplication.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return null != info && info.isAvailable
    }
}