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


    fun <Data> ycToFlow(block: suspend () -> Data) = ycFlow<Data> {
        send(YcResult.Success(block.invoke()))
    }

    fun <Data, T1> ycToFlow(t1: T1, block: suspend (T1) -> Data) = ycFlow<Data> {
        send(YcResult.Success(block.invoke(t1)))
    }

    fun <Data, T1, T2> ycToFlow(t1: T1, t2: T2, block: suspend (T1, T2) -> Data) = ycFlow<Data> {
        send(YcResult.Success(block.invoke(t1, t2)))
    }

    fun <Data, T1, T2, T3> ycToFlow(t1: T1, t2: T2, t3: T3, block: suspend (T1, T2, T3) -> Data) = ycFlow<Data> {
        send(YcResult.Success(block.invoke(t1, t2, t3)))
    }

    fun <Data, T1, T2, T3, T4> ycToFlow(t1: T1, t2: T2, t3: T3, t4: T4, block: suspend (T1, T2, T3, T4) -> Data) = ycFlow<Data> {
        send(YcResult.Success(block.invoke(t1, t2, t3, t4)))
    }

    fun <Data, T1, T2, T3, T4, T5> ycToFlow(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, block: suspend (T1, T2, T3, T4, T5) -> Data) = ycFlow<Data> {
        send(YcResult.Success(block.invoke(t1, t2, t3, t4, t5)))
    }

    fun <Data, T1, T2, T3, T4, T5, T6> ycToFlow(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, block: suspend (T1, T2, T3, T4, T5, T6) -> Data) =
        ycFlow<Data> {
            send(YcResult.Success(block.invoke(t1, t2, t3, t4, t5, t6)))
        }
}