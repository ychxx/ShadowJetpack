package com.yc.jetpacklib.refresh

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
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
open class YcRefreshSpecialViewUtil(mLifecycleOwner: LifecycleOwner) : YcRefreshBaseUtil(mLifecycleOwner) {
    /**
     * 替换布局
     */
    var mSpecialViewSimple: YcSpecialViewSmart? = null

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

    fun build(
        adapter: PagingDataAdapter<*, *>,
        smartRefreshLayout: SmartRefreshLayout,
        recyclerView: RecyclerView,
        specialViewUtil: YcSpecialViewSmart? = null,
        isAutoRefresh: Boolean = true,
        call: YcRefreshBaseUtil.() -> Unit
    ): YcRefreshBaseUtil {
        mSpecialViewSimple = specialViewUtil
        mPagingDataAdapter = adapter
        mSmartRefreshLayout = smartRefreshLayout
        mRecyclerView = recyclerView
        return build(isAutoRefresh, call)
    }
}