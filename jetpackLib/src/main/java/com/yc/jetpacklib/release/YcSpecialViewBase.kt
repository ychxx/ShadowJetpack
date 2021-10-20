package com.yc.jetpacklib.release

import android.R
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 *
 */
abstract class YcSpecialViewBase<VB : ViewBinding> : YcISpecialState<VB> {
    /**
     * 原始View（即要被替换的View）
     */
    lateinit var mOriginalView: View

    /**
     * 替换View
     */
    protected var mReleaseView: View? = null

    /**
     * 是否显示替换View
     */
    protected var isReleaseViewShow = false

    @YcSpecialState
    protected var mSpecialState = YcSpecialState.NET_ERROR

    private var _mViewBinding: VB? = null
    protected val mViewBinding get() = _mViewBinding!!

    private var mReleaseVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null

    /**
     * 修改UI布局和绑定事件（开放给外部类调用）
     */
    override var mCustomUi: (VB.() -> Unit)? = null

    /**
     * 修改UI布局和绑定事件(开放给子类调用，优先级弱于mCustomUi)
     */
    var mCustomUiProtected: (VB.() -> Unit)? = null

    /**
     * 只替换部分View
     */
    constructor(originalView: View, releaseVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) {
        mOriginalView = originalView
        mReleaseVB = releaseVB
    }

    /**
     * 替换整个页面View
     */
    constructor(activity: Activity, releaseVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) {
        mOriginalView = activity.findViewById(R.id.content)
        mReleaseVB = releaseVB
    }

    override fun setSpecialState(specialState: Int) {
        mSpecialState = specialState
    }

    /**
     * 显示
     */
    @Synchronized
    override fun show(specialState: Int) {
        setSpecialState(specialState)
        show()
    }

    /**
     * 显示
     */
    @Synchronized
    override fun show() {
        if (isReleaseViewShow) return
        isReleaseViewShow = true
        createReleaseView()
        mViewBinding.onUpdate(mSpecialState)
        mCustomUiProtected?.invoke(mViewBinding)
        mCustomUi?.invoke(mViewBinding)
        replaceReal()
    }

    /**
     * 替换
     */
    open fun replaceReal() {
        YcReleaseLayoutUtils.replace(mOriginalView, mReleaseView!!)
    }

    /**
     * 恢复
     */
    @Synchronized
    override fun recovery() {
        if (!isReleaseViewShow) return
        isReleaseViewShow = false
        recoveryReal()
    }

    open fun recoveryReal() {
        YcReleaseLayoutUtils.recovery(mOriginalView)
    }

    /**
     * 创建一个替换用的布局
     */
    protected open fun createReleaseView() {
        if (mReleaseView == null) {
            _mViewBinding = mReleaseVB!!.invoke(LayoutInflater.from(mOriginalView.context), null, false)
            mReleaseView = _mViewBinding!!.root
        }
    }

    /**
     * 根据状态更新ui
     */
    abstract fun VB.onUpdate(specialState: Int)
}