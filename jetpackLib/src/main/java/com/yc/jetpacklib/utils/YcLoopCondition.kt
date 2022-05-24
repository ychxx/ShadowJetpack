package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import com.yc.jetpacklib.extension.ycIsTrue
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Creator: yc
 * Date: 2022/4/19 16:41
 * UseDes:轮循器-有条件的
 * LifecycleOwner.lifecycleScope的协程作用域会导致，在onStop时挂起协程，onResume时才重新启动协程
 */
open class YcLoopCondition(val owner: LifecycleOwner) {
    companion object {
        @JvmStatic
        fun initApply(owner: LifecycleOwner, periodTime: Long = 2000L, block: YcLoopCondition.() -> Unit) = lazy {
            return@lazy YcLoopCondition(owner).apply {
                mPeriodTime = periodTime
                block()
            }
        }
    }


    /**
     *  作用域
     */
    private val mScope = CoroutineScope(Job() + Dispatchers.Main)

    /**
     * 句柄
     */
    protected var mJop: Job? = null

    /**
     * 间隔时间(单位毫秒)
     */
    var mPeriodTime: Long = 2000L

    private var mState: AtomicInteger = AtomicInteger(0)
    protected val _mPost: MutableLiveData<Any> = MutableLiveData<Any>()
    val mPost: LiveData<Any> = _mPost

    /**
     * 是否延迟（true 延迟，false 不延迟，立刻执行）
     */
    var mIsDelay: (() -> Boolean)? = null

    init {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                finish()
                mScope.cancel()
            }
        })
    }

    open fun resetStart(isWait: Boolean = false) {
        mState.set(0)
        start(isWait)
    }

    open fun start(isWait: Boolean = false) {
        if (mState.get() != -1) {
            mJop?.cancel()
            mState.set(1)
            mJop = mScope.launch {
                if (isWait)
                    delay(mPeriodTime)
                while (mIsDelay?.invoke().ycIsTrue()) {
                    delay(100)
                }
                if (mState.get() != -1) {
                    _mPost.postValue(0)
                    start(true)
                }
            }
        }
    }

    /**
     * 主动关闭（onDestroy一般会自动调用该方法）
     */
    fun finish() {
        mJop?.cancel()
        mState.set(-1)
    }
}