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
import com.yc.jetpacklib.release.YcSpecialViewCommon
import com.yc.jetpacklib.release.YcSpecialViewSmart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/7/27 15:48
 * UseDes: 替换布局帮助类(无分页，刷新的)
 */

open class YcSpecialUtil(
    private val mLifecycleOwner: LifecycleOwner,
    val mAdapter: RecyclerView.Adapter<*>,
    var mSpecialViewSimple: YcSpecialViewCommon,
    isAutoRefresh: Boolean = true,
) {
    @PublishedApi
    internal val `access$mAdapter`: RecyclerView.Adapter<*>
        get() = mAdapter

    suspend fun <T> Flow<YcResult<YcDataSourceEntity<T>>>.ycCollect(block: (YcResult<YcDataSourceEntity<T>>) -> Unit) {
        this.collect {
            block(it)
            it.doSuccess {
                mRefreshResult.onCall(mAdapter.itemCount > 0, YcRefreshResult.Success(false))
            }.doFail {
                mRefreshResult.onCall(mAdapter.itemCount > 0, YcRefreshResult.Fail(it))
            }
        }
    }

    suspend inline fun <reified T> Flow<YcResult<T>>.ycCollect2(crossinline block: (YcResult<T>) -> Unit) {
        this.collect {
            block(it)
            it.doSuccess {
                mRefreshResult.onCall(`access$mAdapter`.itemCount > 0, YcRefreshResult.Success(false))
            }.doFail {
                mRefreshResult.onCall(`access$mAdapter`.itemCount > 0, YcRefreshResult.Fail(it))
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
        mSpecialViewSimple.mSpecialViewBuild.getSpecialView().context.showToast(it)
    }
    var mRefreshAndLoadMore: (suspend YcSpecialUtil.() -> Unit)? = null

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
                mRefreshJob?.cancel()
            }
        })
        mSpecialViewSimple.mSpecialViewBuild.mYcSpecialBean.mSpecialClickListener = {
            startRefresh()
        }
        if (isAutoRefresh) {
            startRefresh()
        }
    }

    protected var mRefreshJob: Job? = null

    /**
     * 主动刷新
     */
    fun startRefresh() {
        mRefreshJob?.cancel()
        mRefreshJob = mLifecycleOwner.lifecycleScope.launch {
            mRefreshAndLoadMore?.invoke(this@YcSpecialUtil)
        }
    }
}
