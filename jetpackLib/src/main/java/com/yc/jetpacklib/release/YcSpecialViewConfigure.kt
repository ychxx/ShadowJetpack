package com.yc.jetpacklib.release

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.yc.jetpacklib.exception.YcException

/**
 *  通用的一些参数
 */
abstract class YcSpecialViewConfigure<VB : ViewBinding>(val mContext: Context, protected var mSpecialVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)) :
    YcSpecialViewConfigureBase() {
    private var _mViewBinding: VB? = null
    protected val mViewBinding: VB
        get() {
            if (_mViewBinding == null) {
                _mViewBinding = mSpecialVB.invoke(LayoutInflater.from(mContext), null, false)
            }
            return _mViewBinding!!
        }

    override fun getSpecialView(): View {
        return mViewBinding.root
    }

    /**
     * 优先加载（返回值：true时表示不加载默认的配置）
     */
    open var mPriorityUpdate: (VB.(specialState: Int, exception: YcException?) -> Boolean)? = null

}