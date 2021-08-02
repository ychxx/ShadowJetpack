package com.yc.jetpacklib.refresh

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.toYcException
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.recycleView.YcRefreshResult
import com.yc.jetpacklib.recycleView.doFail
import com.yc.jetpacklib.recycleView.doSuccess
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.jetpacklib.release.YcSpecialViewCommon

/**
 * Creator: yc
 * Date: 2021/7/27 15:48
 * UseDes: 刷新帮助类
 */
open class YcRefreshBaseUtil(mLifecycleOwner: LifecycleOwner) {
    lateinit var mPagingDataAdapter: PagingDataAdapter<*, *>
    lateinit var mSmartRefreshLayout: SmartRefreshLayout
    lateinit var mRecyclerView: RecyclerView

    /**
     * 当前是否正在刷新
     */
    private var mIsRefresh: Boolean = false

    /**
     * 刷新回调
     */
    var mRefreshCall: (() -> Unit)? = null

    /**
     * 刷新结果回调
     */
    open var mRefreshResult: ((YcRefreshResult) -> Unit) = {}

    /**
     * 当前是否正在加载更多
     */
    private var mIsLoadMore: Boolean = false

    /**
     * 加载更多结果回调
     */
    var mLoadMoreResult: ((YcRefreshResult) -> Unit) = {
        ycLogESimple("LoadMoreResult$it")
        it.doFail { error ->
            mErrorTip.invoke("加载失败：${error.msg}")
        }
    }

    /**
     * 用于判断数据源是否需要改变，如果没有改变则不需设置（例如搜索列表的关键字改变）
     */
    var mDataSourceChange: (() -> Boolean)? = null

    /**
     * 是否是第一次刷新
     */
    private var mIsFirst = true


    /**
     * 错误提示
     */
    var mErrorTip: ((String) -> Unit) = {
        mRecyclerView.context.showToast(it)
    }

    init {
        mLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                mSmartRefreshLayout.finishRefresh()
                mSmartRefreshLayout.finishLoadMore()
            }
        })
    }

    open fun build(
        adapter: PagingDataAdapter<*, *>,
        smartRefreshLayout: SmartRefreshLayout,
        recyclerView: RecyclerView,
        isAutoRefresh: Boolean = true,
        call: YcRefreshBaseUtil.() -> Unit
    ): YcRefreshBaseUtil {
        mPagingDataAdapter = adapter
        mSmartRefreshLayout = smartRefreshLayout
        mRecyclerView = recyclerView
        return build(isAutoRefresh, call)
    }

    open fun build(isAutoRefresh: Boolean = true, call: YcRefreshBaseUtil.() -> Unit): YcRefreshBaseUtil {
        call.invoke(this)
        mSmartRefreshLayout.setOnRefreshListener {
            if (mIsFirst || mDataSourceChange?.invoke() == true) {
                mIsFirst = false
                mRefreshCall?.invoke()
            } else {
                mPagingDataAdapter.refresh()
            }
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            mPagingDataAdapter.retry()
        }
        mPagingDataAdapter.addLoadStateListener {
            ycLogESimple("LoadStateListener$it")
            onRefresh(it.refresh, it.source.append.endOfPaginationReached)
            onLoadMore(it.append, it.source.append.endOfPaginationReached)
        }
        mRecyclerView.ycInitLinearLayoutManage()
        mRecyclerView.adapter = mPagingDataAdapter
        if (isAutoRefresh) {
            startRefresh()
        }
        return this
    }

    private fun onRefresh(loadState: LoadState, noMoreData: Boolean) {
        when (loadState) {
            is LoadState.Loading -> {
                if (!mIsRefresh) {
                    mIsRefresh = true
                    mSmartRefreshLayout.resetNoMoreData()
                }
            }
            is LoadState.NotLoading -> {
                if (mIsRefresh) {
                    mSmartRefreshLayout.finishRefresh(true)
                    mSmartRefreshLayout.setNoMoreData(noMoreData)
                    mIsRefresh = false
                    mRefreshResult.invoke(YcRefreshResult.Success(noMoreData))
                }
            }
            is LoadState.Error -> {
                if (mIsRefresh) {
                    mIsRefresh = false
                    mSmartRefreshLayout.finishRefresh(false)
                    mRefreshResult.invoke(YcRefreshResult.Fail(loadState.error.toYcException()))
                }
            }
        }
    }

    private fun onLoadMore(loadState: LoadState, noMoreData: Boolean) {
        when (loadState) {
            is LoadState.Loading -> {
                if (!mIsLoadMore) {
                    mIsLoadMore = true
                    mSmartRefreshLayout.resetNoMoreData()
                }
            }
            is LoadState.NotLoading -> {
                if (mIsLoadMore) {
                    mIsLoadMore = false
                    if (noMoreData) {
                        mSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        mSmartRefreshLayout.finishLoadMore(true)
                    }
                    mLoadMoreResult.invoke(YcRefreshResult.Success(noMoreData))
                }
            }
            is LoadState.Error -> {
                mSmartRefreshLayout.finishLoadMore()
                if (mIsLoadMore) {
                    mIsLoadMore = false
                    mLoadMoreResult.invoke(YcRefreshResult.Fail(loadState.error.toYcException()))
                }
            }
        }
    }

    /**
     * 手动刷新
     */
    fun startRefresh() {
        mSmartRefreshLayout.autoRefresh()
    }
}