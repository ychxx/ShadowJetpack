package com.yc.jetpacklib.release

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.exception.YcException

/**
 *  兼容SmartRefreshLayout
 */
class YcSpecialViewSmart(originalView: RecyclerView, private val containerFl: FrameLayout) :
    YcSpecialViewBase<YcSpecialReleaseBinding>(originalView, YcSpecialReleaseBinding::inflate) {
    val mBuild: YcSpecialViewBuild by YcSpecialViewBuild.create()

    override fun YcSpecialReleaseBinding.onUpdate(specialState: Int) {
        mBuild.apply {
            onBulidUpdate(specialState)
        }
    }

    fun show(exception: YcException) {
        mBuild.apply {
            buildShow(exception)
        }
    }

    override fun replaceReal() {
        YcReleaseLayoutUtils.replaceSmart(mOriginalView, mReleaseView!!, containerFl)
    }

    override fun recoveryReal() {
        YcReleaseLayoutUtils.recoverySmart(mOriginalView, containerFl)
    }
}