package com.yc.jetpacklib.recycleView

import com.yc.jetpacklib.exception.YcException

/**
 * Creator: yc
 * Date: 2021/7/28 17:11
 * UseDes:
 */
sealed class YcRefreshResult {
    data class Success(val noMoreData: Boolean) : YcRefreshResult()
    data class Fail(val exception: YcException) : YcRefreshResult()
}

inline fun YcRefreshResult.doSuccess(success: () -> Unit): YcRefreshResult {
    if (this is YcRefreshResult.Success) {
        success()
    }
    return this
}

inline fun YcRefreshResult.doFail(failure: (YcException) -> Unit): YcRefreshResult {
    if (this is YcRefreshResult.Fail) {
        failure(exception)
    }
    return this
}
