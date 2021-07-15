package com.yc.jetpacklib.widget.dialog

/**
 * Creator: yc
 * Date: 2021/6/28 11:39
 * UseDes:
 */
interface YcIDialog<T> {
    fun setMsg(msg: String?): T
    fun setLeftBtnText(leftBtnText: String?): T
    fun setOnLeftClick(onLeftClick: YcOnClick): T
    fun setRightBtnText(rightBtnText: String?): T
    fun setOnRightClick(onRightClick: YcOnClick): T
    fun show()
}