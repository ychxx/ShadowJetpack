package com.yc.jetpacklib.release

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.yc.jetpacklib.databinding.YcSpecialReleaseBinding
import com.yc.jetpacklib.exception.YcException

/**
 *  兼容SmartRefreshLayout
 */
open class YcSpecialViewSmart(originalView: RecyclerView, private val containerFl: FrameLayout) :
    YcSpecialViewBase<YcSpecialReleaseBinding>(originalView, YcSpecialReleaseBinding::inflate) {
    open val mBuild: YcSpecialViewBuild = YcSpecialViewBuild()

    override fun YcSpecialReleaseBinding.onUpdate(specialState: Int) {
        mBuild.apply {
            onBuildUpdate(specialState)
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