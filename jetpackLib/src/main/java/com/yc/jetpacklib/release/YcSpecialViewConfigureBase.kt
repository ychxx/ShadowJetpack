package com.yc.jetpacklib.release

import android.view.View
import androidx.viewbinding.ViewBinding
import com.yc.jetpacklib.exception.YcException

/**
 *  通用的一些参数
 */
abstract class YcSpecialViewConfigureBase {
    var mYcSpecialBean: YcSpecialBean = YcSpecialBean()

    /**
     * 获取替换用的布局
     */
    abstract fun getSpecialView(): View
    abstract fun onUpdate(specialState: Int, exception: YcException? = null)
}