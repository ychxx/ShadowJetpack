package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

/**
 * Creator: yc
 * Date: 2021/2/20 15:05
 * UseDes:轮询器
 */

class YcLoop(val owner: LifecycleOwner) {
    companion object {
        fun init(owner: LifecycleOwner, periodTime: Long = 2000L, block: () -> Unit) = lazy {
            return@lazy YcLoop(owner).apply {
                mPeriodTime = periodTime
                mBlock = {
                    block()
                }
            }
        }
    }

    private val _mPost = MutableLiveData<Any?>()
    private val mPost: LiveData<Any?> = _mPost

    /**
     * 句柄
     */
    private var mJop: Job? = null

    /**
     * 句柄
     */
    private var mJopHandle: Job? = null

    /**
     * 等待执行
     */
    private var mWaitHandle: AtomicReference<Boolean> = AtomicReference(false)

    /**
     * 间隔时间(单位毫秒)
     */
    var mPeriodTime: Long = 2000L

    var mBlock: (() -> Unit)? = null
    fun stop() {
        mWaitHandle.set(true)
    }

    fun recovery() {
        mWaitHandle.set(false)
    }

    fun stopOrRecovery(isStop: Boolean) {
        mWaitHandle.set(isStop)
    }

    fun start(isWait: Boolean = false) {
        mJop?.cancel()
        mJop = owner.lifecycleScope.launch {
            if (isWait)
                delay(mPeriodTime)
            _mPost.postValue(null)
        }
    }


    init {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                mJop?.cancel()
                mJopHandle?.cancel()
            }
        })
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mWaitHandle.get()) {
                    delay(100)
                }
                mBlock?.let { it() }
                start(true)
            }
        })
    }
}