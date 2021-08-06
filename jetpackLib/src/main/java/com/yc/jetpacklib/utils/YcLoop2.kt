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
    /**
     * 是否等待执行条件
     * mIsWait = true, 卡住
     * mIsWait = false
     */
    var mIsWait: (() -> Boolean)? = null

    init {
        reset()
    }

    override fun reset() {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mIsWait?.invoke().ycIsTrue()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }
}