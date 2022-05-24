package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import com.yc.jetpacklib.extension.ycIsTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/2/20 15:05
 * UseDes:轮询器（主动调用外部执行条件）
 */
@Deprecated("由于使用LifecycleOwner.lifecycleScope的协程作用域会导致，在onStop时挂起协程，onResume时才重新启动协程,循环会被暂停。故弃用")
class YcLoopOld2(owner: LifecycleOwner) : YcLoopOldBase(owner) {
    companion object {
        fun init(owner: LifecycleOwner, periodTime: Long = 2000L, block: () -> Unit) = lazy {
            return@lazy YcLoopOld2(owner).apply {
                mPeriodTime = periodTime
                mBlock = {
                    block()
                }
            }
        }
    }

    /**
     * 是否延迟（true 延迟，false 不延迟，立刻执行）
     */
    var mIsDelay: (() -> Boolean)? = null

    init {
        reset()
    }

    override fun reset() {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mIsDelay?.invoke().ycIsTrue()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }
}