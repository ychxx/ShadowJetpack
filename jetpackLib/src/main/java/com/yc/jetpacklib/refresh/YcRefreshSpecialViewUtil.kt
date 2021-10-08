package com.yc.jetpacklib.refresh

import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.recycleView.YcRefreshResult
import com.yc.jetpacklib.recycleView.doFail
import com.yc.jetpacklib.recycleView.doSuccess
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.jetpacklib.release.YcSpecialViewSmart

/**
 * Creator: yc
 * Date: 2021/7/27 15:48
 * UseDes: 刷新帮助类(刷新失败且列表数据为空时，会替换成特殊布局)
 */
open class YcRefreshSpecialViewUtil<T : Any>(mLifecycleOwner: LifecycleOwner) : YcRefreshBaseUtil<T>(mLifecycleOwner) {
    /**
     * 替换布局
     */
    lateinit var mSpecialViewSimple: YcSpecialViewSmart

    /**
     * 刷新结果回调
     */
    override var mRefreshResult: ((YcRefreshResult) -> Unit) = {
        ycLogESimple("Refresh$it")
        it.doSuccess {
            if (mPagingDataAdapter.itemCount <= 0) {//刷新成功，但数据为空
                mSpecialViewSimple?.apply {
                    mBuild.mSpecialClickListener = {
                        startRefresh()
                    }
                    show(YcSpecialState.DATA_EMPTY)
                }
            } else {//刷新成功，恢复之前布局
                mSpecialViewSimple?.recovery()
            }
        }.doFail { error ->
            if (mPagingDataAdapter.itemCount <= 0) {//之前无数据，则显示替换布局
                mSpecialViewSimple?.apply {
                    mBuild.mSpecialClickListener = {
                        startRefresh()
                    }
                    show(error)
                }
            } else {//之前有数据，则显示错误提示
                mErrorTip.invoke("刷新失败：${error.msg}")
            }
        }
    }

    /**
     * 设置必要参数
     * @param adapter PagingDataAdapter<T, *>
     * @param smartRefreshLayout SmartRefreshLayout
     * @param recyclerView RecyclerView
     * @param containerRecyclerViewFl FrameLayout   SmartRefreshLayout和RecyclerView中间的FrameLayout
     * @param isAutoRefresh Boolean                 是否进入页面就直接执行一次刷新
     * @param apply                                 当前类上下文
     */
    fun build(
        adapter: PagingDataAdapter<T, *>,
        smartRefreshLayout: SmartRefreshLayout,
        recyclerView: RecyclerView,
        containerRecyclerViewFl: FrameLayout,
        isAutoRefresh: Boolean = true,
        apply: YcRefreshBaseUtil<T>.() -> Unit
    ): YcRefreshSpecialViewUtil<T> {
        return this.build(adapter, smartRefreshLayout, recyclerView, YcSpecialViewSmart(recyclerView, containerRecyclerViewFl), isAutoRefresh, apply)
    }

    /**
     *
     * @param adapter PagingDataAdapter<T, *>
     * @param smartRefreshLayout SmartRefreshLayout
     * @param recyclerView RecyclerView
     * @param specialViewSmart YcSpecialViewSmart   替换布局辅助类
     * @param isAutoRefresh Boolean                 是否进入页面就直接执行一次刷新
     * @param apply                                 当前类上下文
     */
    fun build(
        adapter: PagingDataAdapter<T, *>,
        smartRefreshLayout: SmartRefreshLayout,
        recyclerView: RecyclerView,
        specialViewSmart: YcSpecialViewSmart,
        isAutoRefresh: Boolean = true,
        apply: YcRefreshBaseUtil<T>.() -> Unit
    ): YcRefreshSpecialViewUtil<T> {
        mPagingDataAdapter = adapter
        mSmartRefreshLayout = smartRefreshLayout
        mRecyclerView = recyclerView
        mSpecialViewSimple = specialViewSmart
        super.build(isAutoRefresh, apply)
        return this
    }
}