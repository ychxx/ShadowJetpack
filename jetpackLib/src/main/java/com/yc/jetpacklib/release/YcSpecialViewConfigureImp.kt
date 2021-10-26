package com.yc.jetpacklib.release

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycLoadImageRes

/**
 *  通用的一些参数
 */
open class YcSpecialViewConfigureImp(mContext: Context) : YcSpecialViewConfigure<YcSpecialReleaseBinding>(mContext, YcSpecialReleaseBinding::inflate) {

    override fun onUpdate(specialState: Int, exception: YcException?) {
        mViewBinding.apply {
            val isFinish = mPriorityUpdate?.invoke(mViewBinding, specialState, exception)
            if (isFinish != true) {
                this.releaseActionBar.apply {
                    mYcSpecialBean.refreshActionbar(this.root, tvActionbarMid, ivActionbarLeft, tvActionbarRight, ivActionbarRight)
                }
                mYcSpecialBean.refreshContext(specialState, exception, releaseContentTv, releaseButtonBtn, releaseIv)
            }
        }
    }
}