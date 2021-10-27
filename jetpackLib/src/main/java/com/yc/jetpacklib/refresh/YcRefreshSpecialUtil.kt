package com.yc.jetpacklib.refresh

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.net.doFail
import com.yc.jetpacklib.net.doSuccess
import com.yc.jetpacklib.recycleView.YcRefreshResult
import com.yc.jetpacklib.recycleView.doFail
import com.yc.jetpacklib.recycleView.doSuccess
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.jetpacklib.release.YcSpecialViewSmart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/7/27 15:48
 * UseDes: 刷新帮助类
 */
interface IPageConfigure {
    var mPageIndex: Int
    val mPageSize: Int
    var mPageSum: Int
    val mPageIndexStart: Int
}

class PageConfigure : IPageConfigure {
    override var mPageIndex: Int = 1
    override val mPageSize: Int = 10
    override var mPageSum: Int = 0
    override val mPageIndexStart: Int = 1
}

open class YcRefreshSpecialUtil(
    private val mLifecycleOwner: LifecycleOwner,
    private val mSmartRefreshLayout: SmartRefreshLayout,
    private val mAdapter: RecyclerView.Adapter<*>,
    var mSpecialViewSimple: YcSpecialViewSmart,
    isAutoRefresh: Boolean = true,
    protected val mPageConfigure: IPageConfigure = PageConfigure()
) {
    suspend fun <T> Flow<YcResult<YcDataSourceEntity<T>>>.ycCollect(block: (YcResult<YcDataSourceEntity<T>>) -> Unit) {
        this.collect {
            block(it)
            it.doSuccess {
                mRefreshResult.onCall(mAdapter.itemCount > 0, YcRefreshResult.Success(mPageConfigure.mPageIndex > it.pageSum))
            }.doFail {
                mRefreshResult.onCall(mAdapter.itemCount > 0, YcRefreshResult.Fail(it))
            }
        }
    }

    open fun interface RefreshResult {
        fun onCall(isHasPreData: Boolean, refreshResult: YcRefreshResult)
    }

    /**
     * 错误提示
     */
    var mErrorTip: ((String) -> Unit) = {
        mSmartRefreshLayout.context.showToast(it)
    }
    var mRefreshAndLoadMore: (suspend YcRefreshSpecialUtil.(isRefresh: Boolean, pageIndex: Int, pageSize: Int) -> Unit)? =
        null

    /**
     * 刷新结果回调
     */
    var mRefreshResult: RefreshResult = RefreshResult { isHasPreData, refreshResult ->
        ycLogESimple("Refresh$refreshResult")
        refreshResult.doSuccess {
            if (isHasPreData) {//刷新成功，恢复之前布局
                mSpecialViewSimple.recovery()
            } else {//刷新成功，但数据为空
                mSpecialViewSimple.show(YcSpecialState.DATA_EMPTY)
            }
        }.doFail { error ->
            if (isHasPreData) {//之前有数据，则显示错误提示
                mErrorTip.invoke("刷新失败：${error.msg}")
            } else {//之前无数据，则显示替换布局
                mSpecialViewSimple.show(YcJetpack.mInstance.mYcExceptionToSpecialState(error), error)
            }
        }
    }

    init {
        mLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                finish()
            }
        })

        mSmartRefreshLayout.setOnRefreshListener {
            initRefresh()
            mLifecycleOwner.lifecycleScope.launch {
                mRefreshAndLoadMore?.invoke(this@YcRefreshSpecialUtil, true, mPageConfigure.mPageIndex, mPageConfigure.mPageSize)
                finish()
            }
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            mPageConfigure.apply {
                if (mPageIndex > mPageSum) {
                    finish()
                    mSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    mLifecycleOwner.lifecycleScope.launch {
                        mRefreshAndLoadMore?.invoke(this@YcRefreshSpecialUtil, false, mPageIndex, mPageSize)
                        finish()
                    }
                }
            }
        }
        mSpecialViewSimple.mSpecialViewBuild.mYcSpecialBean.mSpecialClickListener = {
            startRefresh()
        }
        if (isAutoRefresh) {
            startRefresh()
        }
    }

    /**
     * 还原成初始状态
     */
    protected open fun initRefresh() {
        mPageConfigure.apply {
            mPageIndex = mPageIndexStart
            mPageSum = 0
        }
        mSmartRefreshLayout.setNoMoreData(false)
    }

    /**
     * 结束
     */
    fun finish() {
        mSmartRefreshLayout.finishLoadMore()
        mSmartRefreshLayout.finishRefresh()
    }

    /**
     * 主动刷新
     */
    fun startRefresh() {
        mSmartRefreshLayout.autoRefresh()
    }
}
