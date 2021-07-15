package com.yc.jetpacklib.net

import com.yc.jetpacklib.exception.YcException


/**
 *
 */
sealed class YcResult<out T> {
    data class Success<out T>(val data: T) : YcResult<T>()
    data class Fail(val exception: YcException) : YcResult<Nothing>()
}

inline fun <reified T> YcResult<T>.doSuccess(success: (T) -> Unit): YcResult<T> {
    if (this is YcResult.Success) {
        success(data)
    }
    return this
}

inline fun <reified T> YcResult<T>.doFail(failure: (YcException) -> Unit): YcResult<T> {
    if (this is YcResult.Fail) {
        failure(exception)
    }
    return this
}
