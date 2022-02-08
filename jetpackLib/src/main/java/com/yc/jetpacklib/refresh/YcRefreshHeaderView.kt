package com.yc.jetpacklib.refresh

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent
import com.yc.jetpacklib.databinding.YcRefreshHeaderViewBinding

class YcRefreshHeaderView(context: Context) : SimpleComponent(context, null, 0), RefreshHeader {
    private val mViewBinding: YcRefreshHeaderViewBinding by lazy {
        YcRefreshHeaderViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private val mAnimDraw: AnimationDrawable by lazy {
        mViewBinding.ivRefreshHeader.drawable as AnimationDrawable
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        super.onFinish(layout, success)
        mAnimDraw.stop()
        return 0
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.ReleaseToRefresh -> {
                mAnimDraw.start()
            }
        }
    }
}