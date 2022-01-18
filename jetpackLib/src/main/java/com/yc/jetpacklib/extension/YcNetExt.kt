package com.yc.jetpacklib.extension

import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.net.YcNetUtil
import com.yc.jetpacklib.net.YcResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*

/**
 * 网络状态检测和异常处理
 */
fun <T> Flow<YcResult<T>>.checkNet(): Flow<YcResult<T>> = onStart {
    if (!YcNetUtil.isNetworkAvailable()) {
        throw YcException("网络不可用", YcNetErrorCode.NETWORK_NO)
    }
}.catch {
    it.printStackTrace()
    YcJetpack.mInstance.isContinueWhenExceptionSuspend(it.toYcException()) {
        emit(YcResult.Fail(this))
    }
}.flowOn(Dispatchers.IO)


fun <T> ycFlow(block: suspend ProducerScope<YcResult<T>>.() -> Unit): Flow<YcResult<T>> = channelFlow {
    block()
}.checkNet()

fun <Data> ycToFlow(block: suspend () -> Data) = ycFlow<Data> {
    send(YcResult.Success(block.invoke()))
}

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

