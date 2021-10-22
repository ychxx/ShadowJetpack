package com.yc.jetpacklib.socket

import com.yc.jetpacklib.extension.ycLogESimple
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

/**
 * Creator: yc
 * Date: 2021/10/11 11:37
 * UseDes:
 */
private const val RECONNECT_MAX_NUM = 3

open class YcSocket(private val scope: CoroutineScope, private val reconnectMaxNum: Int = RECONNECT_MAX_NUM) {
    var mFail: ((error: String, errorCode: @YcSocketState.Error Int) -> Unit)? = null
    var mConnSuccess: (() -> Unit)? = null
    protected var mSocket: Socket? = null
    protected var mState: AtomicInteger = AtomicInteger(YcSocketState.PREPARE)
    protected var mCreateSocketJob: Job? = null
    protected var mReceiveJob: Job? = null
    protected var mReconnectNum = 0

    /**
     * 接收数据缓存流
     */
    protected val mReceiveChannel = Channel<String>(Channel.CONFLATED)

    protected var mIp: String = ""
    protected var mPort: Int = 0
    private val mInputStream: InputStream?
        get() {
            return if (mSocket != null && !mSocket!!.isClosed) {
                mSocket?.getInputStream()
            } else {
                null
            }
        }
    private val mOutStream: OutputStream?
        get() {
            return if (mSocket != null && !mSocket!!.isClosed) {
                mSocket?.getOutputStream()
            } else {
                null
            }
        }
    protected open val mCoroutineCreateSocket = CoroutineExceptionHandler { context: CoroutineContext, exception: Throwable ->
        mState.set(YcSocketState.ERROR)
        ycLogESimple("socket 创建失败!${exception.stackTraceToString()}")
        mFail?.invoke("socket 创建失败!", YcSocketState.ERROR_CREATE)
    } + Dispatchers.IO
    protected open val mCoroutineReceive = CoroutineExceptionHandler { context: CoroutineContext, exception: Throwable ->
        mState.set(YcSocketState.ERROR)
        if (mReconnectNum > reconnectMaxNum) {
            ycLogESimple("socket 第${mReconnectNum}重连失败")
            mFail?.invoke("socket 重连失败!", YcSocketState.ERROR_RECONNECT)
        } else {
            ycLogESimple("socket 接收数据协程异常!${exception.stackTraceToString()}")
            mFail?.invoke("socket 接收数据协程异常!", YcSocketState.ERROR_RECEIVE)
            reconnection()
        }
    } + Dispatchers.IO
    protected open val mCoroutineSend = CoroutineExceptionHandler { context: CoroutineContext, exception: Throwable ->
        mState.set(YcSocketState.ERROR)
        ycLogESimple("socket 发送数据协程异常!${exception.stackTraceToString()}")
        mFail?.invoke("socket 发送数据!", YcSocketState.ERROR_SEND)
    } + Dispatchers.IO

    /**
     * 创建接收数据的热流
     */
    open fun createReceive() = mReceiveChannel.receiveAsFlow()

    /**
     * 创建socket
     * @param ip String ip地址
     * @param port Int  端口号
     * @return Job
     */
    open fun createSocket(ip: String, port: Int) {
        mReconnectNum = 0
        mIp = ip
        mPort = port
        createSocket()
    }

    /**
     * 创建socket
     */
    protected open fun createSocket() {
        mCreateSocketJob?.cancel()
        mCreateSocketJob = scope.launch(mCoroutineCreateSocket) {
            resetSocket()
            mSocket = Socket(mIp, mPort)
            connect()
        }
    }

    /**
     * 连接
     */
    protected open fun connect() {
        if (mSocket!!.isConnected) {
            mReconnectNum = 0//连接成功后，重连重置为0
            mState.set(YcSocketState.CONNED)
            mConnSuccess?.invoke()
            receive()
            ycLogESimple("socket 连接成功-ip:${mIp} 和port:${mPort} ")
        } else {
            mState.set(YcSocketState.ERROR)
            mFail?.invoke("socket 连接失败-ip:${mIp} 和port:${mPort}".apply {
                ycLogESimple(this)
            }, YcSocketState.ERROR_CONNECT)
        }
    }

    /**
     * 重连
     */
    protected open fun reconnection() {
        if (mState.get() == YcSocketState.CLOSE)
            return
        mReconnectNum++
        ycLogESimple("socket 第${mReconnectNum}启动重连!")
        createSocket()
    }

    /**
     * 接收数据协程
     */
    protected open fun receive() {
        mReceiveJob?.cancel()
        mReceiveJob = scope.launch(mCoroutineReceive) {
            ycLogESimple("socket 接收线程开始")
            mInputStream?.apply {
                var dataSize = 0
                val buffers = ByteArray(1024)
                while (mState.get() == YcSocketState.CONNED && mInputStream?.read(buffers).also { dataSize = it ?: 0 } != -1) {
                    mReceiveChannel.send(String(buffers, 0, dataSize).apply {
                        ycLogESimple("socket 收到数据：$this")
                    })
                }
                ycLogESimple("socket 连接断开")
                mState.set(YcSocketState.DISCONNECT)
                reconnection()
            }
        }
    }

    /**
     * 发送数据
     * @param sendData String
     */
    open fun send(sendData: String) = scope.launch(mCoroutineSend) {
        if (mSocket != null && mSocket!!.isConnected) {
            ycLogESimple("socket 发送的数据：$sendData")
            mOutStream?.write(sendData.toByteArray())
            mOutStream?.flush()
        } else {
            ycLogESimple("socket 未连接或者断开了")
        }
        cancel()
    }

    /**
     * 重置
     */
    protected open fun resetSocket() {
        mState.set(YcSocketState.PREPARE)
        mReceiveJob?.cancel()
        mCreateSocketJob?.cancel()
        mInputStream?.close()
        mOutStream?.close()
        mSocket?.close()
    }


    open fun close() {
        resetSocket()
        mState.set(YcSocketState.CLOSE)
        scope.cancel()
    }
}