package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import com.yc.jetpacklib.extension.ycIsTrue
import com.yc.jetpacklib.utils.YcLoopBase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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