package com.yc.jetpacklib.data.constant

import android.content.Context
import com.yc.jetpacklib.init.YcJetpack
import java.io.File

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-08-31
 * UseDes:
 */
object YcConstants {

    //获取应用内部存储的根路径
    fun internalPath(): String {
        return YcJetpack.mInstance.mApplication.filesDir.path + File.separator
    }



}