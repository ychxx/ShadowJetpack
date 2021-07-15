package com.yc.jetpacklib.base

import com.yc.jetpacklib.extension.checkNet
import com.yc.jetpacklib.net.YcResult
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

/**
 * Creator: yc
 * Date: 2021/6/15 18:27
 * UseDes:
 */
open class YcRepository {
    public fun <T> ycFlow(block: suspend ProducerScope<YcResult<T>>.() -> Unit): Flow<YcResult<T>> = channelFlow {
        block()
    }.checkNet()
}