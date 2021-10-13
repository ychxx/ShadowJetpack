package com.yc.shadowjetpack.socket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.socket.YcSocket
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect

/**
 * Creator: yc
 * Date: 2021/10/11 11:33
 * UseDes:
 */
class TestSocketVM : YcBaseViewModel() {
    private val _mReceiverData = MutableLiveData<String>()
    val mReceiverData: LiveData<String> = _mReceiverData
    private val mYcSocket: YcSocket by lazy {
        YcSocket(viewModelScope).apply {
            mFail = { error, errorCode ->
                //出错
            }
            ycLaunch { createReceive().collect { _mReceiverData.postValue(it) } }
        }
    }

    fun start(ip: String, port: Int) {
        mYcSocket.createSocket(ip, port)
    }

    fun send(data: String) {
        mYcSocket.send(data)
    }

    override fun onCleared() {
        super.onCleared()
        mYcSocket.close()
    }
}