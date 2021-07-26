package com.yc.jetpacklib.release

/**
 *
 */
interface YcISpecialState {
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
     * 恢复
     */
    fun recovery()
}