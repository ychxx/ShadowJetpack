package com.yc.jetpacklib.release

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  用于SmartRefreshLayout
 */
abstract class YcSpecialRecycleView(protected val mContext: Context) : FrameLayout(mContext) {
    var mRecyclerView: RecyclerView
        protected set

    init {
        mRecyclerView = RecyclerView(context)
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this.addView(mRecyclerView)
    }
}