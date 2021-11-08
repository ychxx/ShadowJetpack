package com.yc.jetpacklib.base

import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.checkNet
import com.yc.jetpacklib.extension.toYcException
import com.yc.jetpacklib.net.YcResult
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*

/**
 * Creator: yc
 * Date: 2021/6/15 18:27
 * UseDes:
 */

open class YcRepository {
    fun <T> ycFlow(block: suspend ProducerScope<YcResult<T>>.() -> Unit): Flow<YcResult<T>> = channelFlow {
        block()
    }.checkNet()

    /**
     *
     * @param block
     * @param failCall 异常回调（只是用于提前处理异常，如：自动登录失败，清空token数据）
     * @return Flow<YcResult<T>>
     */
    inline fun <T> ycFlow2(
        crossinline block: suspend ProducerScope<YcResult<T>>.() -> Unit,
        crossinline failCall: suspend YcException.() -> Unit
    ): Flow<YcResult<T>> = channelFlow {
        block()
    }.catch { cause ->
        failCall.invoke(cause.toYcException())
        throw cause
    }.checkNet()
}