package com.yc.jetpacklib.release

import android.app.Activity
import android.view.View
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.init.YcJetpack

/**
 *  通用的网络异常，空数据等展示页
 */
open class YcSpecialViewCommon : YcSpecialViewBase {
    val mSpecialViewBuild: YcSpecialViewConfigureBase

    constructor(
        originalView: View,
        specialViewBuild: YcSpecialViewConfigureBase = YcJetpack.mInstance.mCreateSpecialViewBuildBase.invoke(originalView.context)
    ) : super(originalView, { specialViewBuild.getSpecialView() }) {
        mSpecialViewBuild = specialViewBuild
    }

    constructor(
        activity: Activity, specialViewBuild: YcSpecialViewConfigureBase = YcJetpack.mInstance.mCreateSpecialViewBuildBase.invoke(activity)
    ) : super(activity, { specialViewBuild.getSpecialView() }) {
        mSpecialViewBuild = specialViewBuild
    }

    override fun onUpdate(specialState: Int, exception: YcException?) {
        mSpecialViewBuild.onUpdate(specialState, exception)
    }

}