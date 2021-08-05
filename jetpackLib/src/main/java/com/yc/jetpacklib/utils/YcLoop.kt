package com.yc.jetpacklib.utils

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

/**
 * Creator: yc
 * Date: 2021/2/20 15:05
 * UseDes:轮询器（外部设置执行条件）
 */

open class YcLoop(owner: LifecycleOwner) : YcLoopBase(owner) {
    /**
     * 等待执行
     */
    private var mWaitHandle: AtomicReference<Boolean> = AtomicReference(false)

    init {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mWaitHandle.get()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }

    fun stop() {
        mWaitHandle.set(true)
    }

    fun recovery() {
        mWaitHandle.set(false)
    }

    fun stopOrRecovery(isStop: Boolean) {
        mWaitHandle.set(isStop)
    }
}