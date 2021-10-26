package com.yc.jetpacklib.release

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

/**
 *  兼容SmartRefreshLayout
 */
open class YcSpecialViewSmart(originalView: RecyclerView, private val containerFl: FrameLayout) : YcSpecialViewCommon(originalView) {
    override fun replaceReal() {
        YcReleaseLayoutUtils.replaceSmart(mOriginalView, mReleaseView.invoke(), containerFl)
    }

    override fun recoveryReal() {
        YcReleaseLayoutUtils.recoverySmart(mOriginalView, containerFl)
    }
}