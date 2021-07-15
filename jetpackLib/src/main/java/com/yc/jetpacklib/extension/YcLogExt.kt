package com.yc.jetpacklib.extension

import android.util.Log
import com.orhanobut.logger.Logger

/**
 * log
 */
object YcLogExt {
    var mIsShowLogger: Boolean = true
}

fun ycLogESimple(msg: String? = "", tag: String = "log") {
    if (YcLogExt.mIsShowLogger) {
        Log.e(tag, msg.orEmpty())
    }
}

fun ycLogE(msg: String? = "") {
    if (YcLogExt.mIsShowLogger) {
        Logger.e(msg.orEmpty())
    }
}

fun ycLogDSimple(msg: String? = "", tag: String = "log") {
    if (YcLogExt.mIsShowLogger) {
        Log.d(tag, msg.orEmpty())
    }
}

fun ycLogD(msg: String? = "") {
    if (YcLogExt.mIsShowLogger) {
        Logger.d(msg.orEmpty())
    }
}

fun ycLogEJson(json: String? = "") {
    if (YcLogExt.mIsShowLogger) {
        Logger.json(json)
    }
}
