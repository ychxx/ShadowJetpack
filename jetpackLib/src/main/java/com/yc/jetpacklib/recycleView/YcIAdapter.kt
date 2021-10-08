package com.yc.jetpacklib.recycleView

import androidx.viewbinding.ViewBinding

/**
 * Creator: yc
 * Date: 2021/10/8 17:24
 * UseDes:
 */
interface YcIAdapter<Data, VB : ViewBinding> {
    /**
     * 单击事件
     */
    var mItemClick: ((item: Data) -> Unit)?

    /**
     * 单击事件
     */
    var mItemClick2: ((item: Data, position: Int) -> Unit)?

    /**
     * 布局
     */
    var mOnUpdate: (VB.(data: Data) -> Unit)?

    /**
     * 布局
     */
    var mOnUpdate2: (VB.(position: Int, data: Data) -> Unit)?
}