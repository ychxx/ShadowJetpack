package com.yc.shadowjetpack

import android.app.Application
import com.yc.jetpacklib.init.YcJetpack

/**
 * Creator: yc
 * Date: 2021/7/29 14:53
 * UseDes:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        YcJetpack.mInstance.apply {
            init(this@App)
            mCreateSpecialViewBuildBase = {
                TestSpecialViewConfigureImp(it)
            }
            mIsForceNoHandle = {
                when (it.code) {
                    401 -> {
                        //处理特定code 例如token过期之类的
                        true
                    }
                    else -> false
                }
            }
        }
    }
}