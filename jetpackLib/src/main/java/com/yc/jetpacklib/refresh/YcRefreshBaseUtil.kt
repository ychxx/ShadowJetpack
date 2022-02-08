package com.yc.jetpacklib.refresh

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.toYcException
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.recycleView.YcPagingDataAdapter
import com.yc.jetpacklib.recycleView.YcPagingDataAdapterChange
import com.yc.jetpacklib.recycleView.YcRefreshResult
import com.yc.jetpacklib.recycleView.doFail

/**
 * Creator: yc
 * Date: 2021/7/27 15:48
 * UseDes: 刷新帮助类
 */
open class YcRefreshBaseUtil<T : Any>(mLifecycleOwner: LifecycleOwner) {
    lateinit var mPagingDataAdapter: PagingDataAdapter<T, *>
    lateinit var mSmartRefreshLayout: SmartRefreshLayout
    lateinit var mRecyclerView: RecyclerView

    /**
     * 数据源
     */
    protected var mPagingData: PagingData<T>? = null

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
     * 是否是第一次刷新
     */
    private var mIsFirst = true

    /**
     * 是否强制更新数据源（用于外部调用，重新获取新的数据源）
     */
    private var mIsForceDataSource: Boolean = false

    /**
     * 错误提示
     */
    var mErrorTip: ((String) -> Unit) = {
        mSmartRefreshLayout.context.showToast(it)
    }

    init {
        mLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                if (::mSmartRefreshLayout.isInitialized) {
                    mSmartRefreshLayout.finishRefresh()
                    mSmartRefreshLayout.finishLoadMore()
                }
            }
        })
    }

    open fun build(
        adapter: PagingDataAdapter<T, *>,
        smartRefreshLayout: SmartRefreshLayout,
        isAutoRefresh: Boolean = true,
        apply: YcRefreshBaseUtil<T>.() -> Unit
    ): YcRefreshBaseUtil<T> {
        mPagingDataAdapter = adapter
        mSmartRefreshLayout = smartRefreshLayout
        return build(isAutoRefresh, apply)
    }

    open fun build(isAutoRefresh: Boolean = true, apply: (YcRefreshBaseUtil<T>.() -> Unit)? = null): YcRefreshBaseUtil<T> {
        apply?.invoke(this)
        mSmartRefreshLayout.setOnRefreshListener {
            //用于判断是否需要改变数据源，如果没有改变则不需设置（例如搜索列表的关键字改变）
            if (mIsFirst || mPagingData == null || mIsForceDataSource) {
                mIsFirst = false
                mIsForceDataSource = false
                mRefreshCall?.invoke()
            } else {
                mPagingDataAdapter.refresh()
            }
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            if (mPagingDataAdapter.itemCount <= 0) {
                mSmartRefreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                mPagingDataAdapter.retry()
            }
        }
        mPagingDataAdapter.addLoadStateListener {
            ycLogESimple("LoadStateListener$it")
            onRefresh(it.refresh, it.source.append.endOfPaginationReached)
            onLoadMore(it.append, it.source.append.endOfPaginationReached)
        }
        if (isAutoRefresh) {
            startRefresh()
        }
        return this
    }

    /**
     * 刷新
     */
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
                    YcJetpack.mInstance.isContinueWhenException(loadState.error.toYcException()) {
                        mRefreshResult.invoke(YcRefreshResult.Fail(this))
                    }
                }
            }
        }
    }

    /**
     * 加载更多
     */
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
                    YcJetpack.mInstance.isContinueWhenException(loadState.error.toYcException()) {
                        mLoadMoreResult.invoke(YcRefreshResult.Fail(this))
                    }
                }
            }
        }
    }

    /**
     * 主动刷新
     * @param isDataSourceChange Boolean    数据源是否改变
     */
    fun startRefresh(isDataSourceChange: Boolean = false) {
        if (isDataSourceChange) {
            mPagingData = null
        }
        mSmartRefreshLayout.finishRefresh()
        mSmartRefreshLayout.finishLoadMore()
        mSmartRefreshLayout.autoRefresh(0)
    }

    /**
     * 设置数据源
     * @param isAutoRefresh Boolean 是否自动执行刷新
     */
    fun setDataSourceRefresh(isAutoRefresh: Boolean = false) {
        mIsForceDataSource = true
        if (isAutoRefresh) {
            startRefresh(true)
        }
    }

    /**
     * 清空数据
     */
    fun clearPagingData(lifecycleOwner: LifecycleOwner) {
        mPagingData = null
        mPagingDataAdapter.submitData(lifecycleOwner.lifecycle, PagingData.empty())
    }

    /**
     * 设置数据源
     */
    fun setPagingData(lifecycleOwner: LifecycleOwner, pagingData: PagingData<T>) {
        mPagingData = pagingData
        if (mPagingDataAdapter is YcPagingDataAdapter<T, *>) {
            (mPagingDataAdapter as YcPagingDataAdapter<T, *>).ycSubmitData(lifecycleOwner, pagingData)
        } else {
            mPagingDataAdapter.submitData(lifecycleOwner.lifecycle, pagingData)
        }
    }

    /**
     * 设置数据源
     */
    suspend fun setPagingData(pagingData: PagingData<T>) {
        mPagingData = pagingData
        if (mPagingDataAdapter is YcPagingDataAdapter<T, *>) {
            (mPagingDataAdapter as YcPagingDataAdapter<T, *>).ycSubmitData(pagingData)
        } else {
            mPagingDataAdapter.submitData(pagingData)
        }
    }
}
