package com.yc.jetpacklib.release

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  布局替换
 */
abstract class YcSpecialRecycleView(protected val mContext: Context) : FrameLayout(mContext), YcISpecialState {

    var mRecyclerView: RecyclerView
        protected set
    protected var mReleaseView: View? = null
    protected var mIsReleaseViewShow = false

    @YcSpecialState
    protected var mSpecialState = 0

    init {
        mRecyclerView = object : RecyclerView(context) {
            override fun setVisibility(visibility: Int) {
                mIsReleaseViewShow = visibility != VISIBLE
                super.setVisibility(visibility)
            }
        }
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this.addView(mRecyclerView)
    }


    override fun setSpecialState(specialState: Int) {
        mSpecialState = specialState
    }

    /**
     * 显示
     */
    @Synchronized
    override fun show() {
        if (mIsReleaseViewShow) return
        mIsReleaseViewShow = true
        if (mReleaseView == null) {
            createReleaseView()
            this.addView(mReleaseView)
        }
        onUpdate(mSpecialState)
        mRecyclerView.visibility = GONE
        mReleaseView!!.visibility = VISIBLE
    }

    /**
     * 恢复
     */
    @Synchronized
    override fun recovery() {
        if (mReleaseView != null) {
            mReleaseView!!.visibility = GONE
            mRecyclerView.visibility = VISIBLE
        }
    }

    fun smoothScrollToPosition(position: Int) {
        if (position >= 0) {
            mRecyclerView.smoothScrollToPosition(position)
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        mRecyclerView.adapter = adapter
    }

    /**
     * 创建一个替换用的布局
     */
    abstract fun createReleaseView()

    /**
     * 根据状态更新ui
     */
    protected abstract fun onUpdate(specialState: Int)
}