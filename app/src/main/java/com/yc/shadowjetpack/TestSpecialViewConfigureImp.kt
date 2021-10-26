package com.yc.shadowjetpack

import android.content.Context
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.release.YcSpecialViewConfigure
import com.yc.jetpacklib.release.refreshActionbar
import com.yc.jetpacklib.release.refreshContext
import com.yc.shadowjetpack.databinding.TestOtherSpecialReleaseBinding

/**
 *  通用的一些参数
 */
open class TestSpecialViewConfigureImp(mContext: Context) :
    YcSpecialViewConfigure<TestOtherSpecialReleaseBinding>(mContext, TestOtherSpecialReleaseBinding::inflate) {

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