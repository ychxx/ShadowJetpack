package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import com.yc.jetpacklib.extension.ycIsFalse
import com.yc.jetpacklib.extension.ycIsTrue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

/**
 * Creator: yc
 * Date: 2021/2/20 15:05
 * UseDes:轮询器（主动调用外部执行条件）
 */

class YcLoop2(owner: LifecycleOwner) : YcLoopBase(owner) {
    companion object {
        fun init(owner: LifecycleOwner, periodTime: Long = 2000L, block: () -> Unit) = lazy {
            return@lazy YcLoop2(owner).apply {
                mPeriodTime = periodTime
                mBlock = {
                    block()
                }
            }
        }
    }

    /**
     * 等待执行
     * @return true 时不等待，false 时继续等待
     */
    var mWaitHandle: (() -> Boolean)? = null

    init {
        reset()
    }

    override fun reset() {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mWaitHandle?.invoke().ycIsFalse()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }
}