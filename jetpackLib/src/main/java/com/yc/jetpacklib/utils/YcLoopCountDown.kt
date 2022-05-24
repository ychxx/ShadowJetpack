package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Creator: yc
 * Date: 2022/4/19 16:41
 * UseDes:用倒计时(自定义的协程作用域)
 * LifecycleOwner.lifecycleScope的协程作用域会导致，在onStop时挂起协程，onResume时才重新启动协程
 */
open class YcLoopCountDown(val owner: LifecycleOwner) {
    companion object {
//        @JvmStatic
//        fun init(owner: LifecycleOwner, periodTime: Long = 2000L, totalTime: Long = 10000L, block: () -> Unit) = lazy {
//            return@lazy YcLoopCountDown(owner).apply {
//                mPeriodTime = periodTime
//                mTotalTime = totalTime
//                mBlock = {
//                    block()
//                }
//            }
//        }

        @JvmStatic
        fun initApply(owner: LifecycleOwner, periodTime: Long = 2000L, totalTime: Long = 10000L, block: YcLoopCountDown.() -> Unit) = lazy {
            return@lazy YcLoopCountDown(owner).apply {
                mPeriodTime = periodTime
                mTotalTime = totalTime
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

    /**
     * 总时长(单位毫秒)
     */
    var mTotalTime: Long = 10000L

    /**
     * 总时长(单位毫秒)
     */
    private var mCurrentTime: Long = 10000L

    /**
     * 回调 //TODO 应用处于后台时，回调中有显示对话框，则对话框msg设置了值，但显示却是为空问题，故暂注释掉
     */
//    var mBlock: ((currentTime: Long) -> Unit)? = null
    private var mState: AtomicInteger = AtomicInteger(0)
    protected val _mPost: MutableLiveData<Long> = MutableLiveData<Long>()
    val mPost: LiveData<Long> = _mPost
    open fun resetStart(isWait: Boolean = false) {
        mState.set(0)
        mCurrentTime = mTotalTime
        start(isWait)
    }

    open fun start(isWait: Boolean = false) {
        if (mState.get() != -1) {
            mJop?.cancel()
            mState.set(1)
            mJop = mScope.launch {
                if (isWait)
                    delay(mPeriodTime)
                mCurrentTime -= mPeriodTime
                if (mState.get() != -1) {
//                    mBlock?.invoke(mCurrentTime)
                    _mPost.postValue(mCurrentTime)
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

    init {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                finish()
            }
        })
    }
}