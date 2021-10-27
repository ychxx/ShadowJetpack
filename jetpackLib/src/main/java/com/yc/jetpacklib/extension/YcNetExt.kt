package com.yc.jetpacklib.extension

import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.net.YcNetUtil
import com.yc.jetpacklib.net.YcResult
import kotlinx.coroutines.Dispatchers
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
    emit(YcResult.Fail(it.toYcException()))
}.flowOn(Dispatchers.IO)

//TODO 待完成 用于替代请求时创建flow
//@OptIn(ExperimentalTypeInference::class)
//public fun <T > ycFlowIO(@BuilderInference block: suspend FlowCollector<T>.() -> Unit): Flow<T> = flow(block)
//    .onStart {
//        if (!YcNetUtil.isNetworkAvailable()) {
//            throw YcException("网络不可用", YcNetError.NETWORK_ERROR)
//        }
//    }.catch {
//        it.printStackTrace()
//        emit(YcResult.Fail(it.toYcException()))
//    }.flowOn(Dispatchers.IO)
//
