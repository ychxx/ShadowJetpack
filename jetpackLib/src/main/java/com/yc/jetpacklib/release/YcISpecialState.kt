package com.yc.jetpacklib.release

import androidx.viewbinding.ViewBinding

/**
 *
 */
interface YcISpecialState<VB : ViewBinding> {
    /**
     * 设置类型
     *
     * @param specialState
     */
    fun setSpecialState(@YcSpecialState specialState: Int)

    /**
     * 显示
     */
    fun show()

    /**
     * 显示
     */
    fun show(specialState: Int)

    /**
     * 恢复
     */
    fun recovery()

    var mCustomUi: (VB.() -> Unit)?
}