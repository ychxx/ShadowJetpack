package com.yc.jetpacklib.utils

import androidx.lifecycle.*
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

open class YcLoop2(owner: LifecycleOwner) : YcLoopBase(owner) {
    /**
     * 自定义执行条件
     */
    var mWaitHandleCustom: (() -> Boolean)? = null

    init {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mWaitHandleCustom?.invoke().ycIsTrue()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }
}