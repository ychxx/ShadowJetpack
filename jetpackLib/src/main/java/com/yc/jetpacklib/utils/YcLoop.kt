package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

/**
 * Creator: yc
 * Date: 2021/2/20 15:05
 * UseDes:轮询器（外部设置执行条件）
 */
class YcLoop(owner: LifecycleOwner) : YcLoopBase(owner) {
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

    /**
     * 是否延迟
     * @return true 延迟，false 不延迟，立刻执行
     */
    private var mIsDelay: AtomicReference<Boolean> = AtomicReference(false)

    init {
        reset()
    }

    fun stop() {
        mIsDelay.set(true)
    }

    fun recovery() {
        mIsDelay.set(false)
    }

    fun stopOrRecovery(isStop: Boolean) {
        mIsDelay.set(isStop)
    }

    override fun reset() {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mIsDelay.get()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }
}