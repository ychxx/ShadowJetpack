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

open class YcLoopBase(val owner: LifecycleOwner) {
    companion object {
        fun init(owner: LifecycleOwner, periodTime: Long = 2000L, block: () -> Unit) = lazy {
            return@lazy YcLoopBase(owner).apply {
                mPeriodTime = periodTime
                mBlock = {
                    block()
                }
            }
        }
    }

    protected val _mPost = MutableLiveData<Any?>()
    protected val mPost: LiveData<Any?> = _mPost

    /**
     * 句柄
     */
    protected var mJop: Job? = null

    /**
     * 句柄
     */
    protected var mJopHandle: Job? = null

    /**
     * 间隔时间(单位毫秒)
     */
    var mPeriodTime: Long = 2000L

    var mBlock: (() -> Unit)? = null


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
    }
}