package com.yc.jetpacklib.extension

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.exception.YcIoException
import org.json.JSONException
import retrofit2.HttpException
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * 异常转换扩展
 */
fun Throwable.toYcException(): YcException {
    return when (this) {
        is YcException -> this
        is YcIoException -> YcException(msg, code)
        is JsonParseException -> YcException("接口解析出错", YcNetErrorCode.JSON_ERROR)
        is JSONException -> YcException("接口解析出错", YcNetErrorCode.JSON_ERROR)
        is ParseException -> YcException("接口解析出错", YcNetErrorCode.JSON_ERROR)
        is MalformedJsonException -> YcException("接口解析出错", YcNetErrorCode.JSON_ERROR)
        is IllegalStateException -> YcException("接口解析出错", YcNetErrorCode.JSON_ERROR)
        is HttpException -> YcException("网络请求错误", code())
        is ConnectException -> YcException("连接失败", YcNetErrorCode.NETWORK_NO)
        is SocketTimeoutException -> YcException("网络超时", YcNetErrorCode.TIME_OUT_ERROR)
        is NullPointerException -> YcException("空异常", YcNetErrorCode.DATE_NULL_ERROR)
        else -> YcException("未知错误", YcNetErrorCode.UN_KNOWN_ERROR)
    }
}

/**
 * 异常捕获，无返回结果
 */
inline fun <reified T, reified R> T.ycTry(block: (T) -> R) {
    try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

inline fun <reified T, reified R> T.ycTry(block: (T) -> R, error: ((Throwable) -> R)) {
    try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        error.invoke(e)
    }
}

/**
 * 异常捕获，有返回结果
 */
inline fun <reified T, reified R> T.ycTryReturnData(block: (T) -> R): R? {
    return try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T, reified R> T.ycTryReturnData(block: (T) -> R, error: (Throwable) -> R): R {
    return try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
        error(e)
    }
}
