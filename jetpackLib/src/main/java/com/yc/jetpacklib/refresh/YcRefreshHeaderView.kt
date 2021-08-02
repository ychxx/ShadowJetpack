package com.yc.jetpacklib.refresh

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.internal.InternalAbstract
import com.yc.jetpacklib.databinding.YcRefreshHeaderViewBinding

class YcRefreshHeaderView(context: Context) : InternalAbstract(context, null, 0), RefreshHeader {
    protected val mViewBinding: YcRefreshHeaderViewBinding = YcRefreshHeaderViewBinding.inflate(LayoutInflater.from(context), this, true)
    protected val mAnimDraw: AnimationDrawable get() = mViewBinding.ivRefreshHeader.drawable as AnimationDrawable

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        super.onFinish(layout, success)
        mAnimDraw.stop();
        return 500 //延迟500毫秒之后再弹回
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.ReleaseToRefresh -> {
                mAnimDraw.start()
            }
        }
    }
}