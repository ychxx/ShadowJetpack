package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/2/20 15:05
 * UseDes:轮询器
 */
@Deprecated("由于使用LifecycleOwner.lifecycleScope的协程作用域会导致，在onStop时挂起协程，onResume时才重新启动协程,循环会被暂停。故弃用")
open class YcLoopOldBase(val owner: LifecycleOwner) {
    companion object {
        fun init(owner: LifecycleOwner, periodTime: Long = 2000L, block: () -> Unit) = lazy {
            return@lazy YcLoopOldBase(owner).apply {
                mPeriodTime = periodTime
                mBlock = {
                    block()
                }
            }
        }
    }

    /**
     * 执行条件
     */
    protected val mPost: MutableLiveData<Any?> = MutableLiveData<Any?>()

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

    /**
     * 回调
     */
    var mBlock: (() -> Unit)? = null


    open fun start(isWait: Boolean = false) {
        mJop?.cancel()
        mJop = owner.lifecycleScope.launch {
            if (isWait)
                delay(mPeriodTime)
            mPost.postValue(null)
        }
    }

    /**
     * 主动关闭（onDestroy一般会自动调用该方法）
     */
    fun finish() {
        mJop?.cancel()
        mJopHandle?.cancel()
    }

    /**
     * 一般不需要主动调用，除非主动调用了finish()
     */
    open fun reset() {

    }

    init {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                finish()
            }
        })
    }
}