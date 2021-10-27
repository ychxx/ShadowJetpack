package com.yc.jetpacklib.release

import android.R
import android.app.Activity
import android.view.View
import com.yc.jetpacklib.exception.YcException

/**
 *
 */
abstract class YcSpecialViewBase : YcISpecial {
    /**
     * 原始View（即要被替换的View）
     */
    protected var mOriginalView: View

    /**
     * 替换View
     */
    protected var mReleaseView: () -> View

    /**
     * 是否显示替换View
     */
    protected var mIsReleaseViewShow = false

    @YcSpecialState
    protected var mSpecialState = YcSpecialState.NETWORK_NO

    /**
     * 替换整个页面View
     */
    constructor(activity: Activity, releaseView: () -> View) : this(activity.findViewById(R.id.content) as View, releaseView)

    /**
     * 只替换部分View
     */
    constructor(originalView: View, releaseView: () -> View) {
        mOriginalView = originalView
        mReleaseView = releaseView
    }

    override fun setSpecialState(@YcSpecialState specialState: Int) {
        mSpecialState = specialState
    }

    /**
     * 显示
     */
    @Synchronized
    override fun show(@YcSpecialState specialState: Int, exception: YcException?) {
        setSpecialState(specialState)
        onUpdate(mSpecialState, exception)
        if (mIsReleaseViewShow) return
        mIsReleaseViewShow = true
        replaceReal()
    }

    /**
     * 替换
     */
    open fun replaceReal() {
        YcReleaseLayoutUtils.replace(mOriginalView, mReleaseView.invoke())
    }

    /**
     * 恢复
     */
    @Synchronized
    override fun recovery() {
        if (!mIsReleaseViewShow) return
        mIsReleaseViewShow = false
        recoveryReal()
    }

    open fun recoveryReal() {
        YcReleaseLayoutUtils.recovery(mOriginalView)
    }
}