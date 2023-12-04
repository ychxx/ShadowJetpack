package com.yc.jetpacklib.socket

import androidx.annotation.IntDef

/**
 * Creator: yc
 * Date: 2021/10/11 11:53
 * UseDes:
 */


class YcSocketState {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
    @IntDef(PREPARE, CONNED, DISCONNECT, ERROR)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class State

    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
    @IntDef(ERROR_CREATE, ERROR_CONNECT, ERROR_RECONNECT, ERROR_RECEIVE, ERROR_SEND)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Error
    companion object {
        const val PREPARE = 0 //准备中
        const val CONNED = 1 //连接成功
        const val DISCONNECT = 2 //连接断开
        const val ERROR = 3 //连接断开
        const val CLOSE = 4 //关闭状态
        const val ERROR_CREATE = 101 //socket创建异常
        const val ERROR_CONNECT = 102//socket 连接异常
        const val ERROR_RECONNECT = 103 //socket重连接异常
        const val ERROR_RECEIVE = 104 //socket接收数据协程异常
        const val ERROR_SEND = 105 //socket发送数据协程异常

    }
}