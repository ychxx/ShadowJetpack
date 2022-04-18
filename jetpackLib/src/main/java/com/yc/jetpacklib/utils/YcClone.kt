package com.yc.jetpacklib.utils

import com.google.gson.Gson
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Creator: yc
 * Date: 2022/3/14 16:05
 * UseDes:深拷贝
 */
object YcClone {
    private val mGson by lazy { Gson() }

    @JvmStatic
    fun <T> deepClone(data: T, type: Type): T? {
        return try {
            mGson.fromJson(mGson.toJson(data), type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}