package com.yc.jetpacklib.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/2/2 20:39
 * UseDes:
 * 可以单独修改单个item
 */
abstract class YcPagingDataAdapterChange<Data : Any, VB : ViewBinding>(
    createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    diffCallback: DiffUtil.ItemCallback<Data>
) : YcPagingDataAdapter<Data, VB>(createVB, diffCallback) {
    companion object {
        fun <Data : Any, VB : ViewBinding> ycLazyInit(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            block: YcPagingDataAdapterChange<Data, VB>.() -> Unit
        ): Lazy<YcPagingDataAdapterChange<Data, VB>> = lazy {
            return@lazy object : YcPagingDataAdapterChange<Data, VB>(createVB, diffCallback) {
                init {
                    block.invoke(this)
                }
            }
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitPosition(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            block: YcPagingDataAdapterChange<Data, VB>.() -> Unit
        ): Lazy<YcPagingDataAdapterChange<Data, VB>> = lazy {
            return@lazy object : YcPagingDataAdapterChange<Data, VB>(createVB, diffCallback) {
                init {
                    block.invoke(this)
                }
            }
        }
    }

    protected lateinit var mPagingData: PagingData<Data>
    override fun ycSubmitData(lifecycleOwner: LifecycleOwner, pagingData: PagingData<Data>) {
        mPagingData = pagingData
        submitData(lifecycleOwner.lifecycle, pagingData)
    }

    override suspend fun ycSubmitData(pagingData: PagingData<Data>) {
        mPagingData = pagingData
        submitData(pagingData)
    }

    /**
     * 移除数据
     * @param item 要移除的条目
     */
    open suspend fun removeItem(item: Data) {
        filterItem { it != item }
    }

    /**
     * 移除数据
     * @param item 要移除的条目
     */
    open fun removeItem(lifecycle: Lifecycle, item: Data) {
        filterItem(lifecycle) { it != item }
    }

    /**
     * 移除数据
     * @param position 要移除的条目的索引
     */
    open suspend fun removeItem(position: Int) {
        filterItem { it != getItem(position) }
    }

    /**
     * 移除数据
     * @param position 要移除的条目的索引
     */
    open fun removeItem(lifecycle: Lifecycle, position: Int) {
        filterItem(lifecycle) { it != getItem(position) }
    }


    /**
     * 修改条目内容
     * @param position 条目索引
     * @param payload 局部刷新
     */
    fun edit(@IntRange(from = 0) position: Int, payload: Any? = null, block: (Data?) -> Unit = {}) {
        if (position >= itemCount) {
            return
        }
        block(getItem(position))
        notifyItemChanged(position, payload)
    }

    /**
     * 过滤数据
     * @param predicate 条件为false的移除，为true的保留
     */
    suspend fun filterItem(predicate: suspend (Data) -> Boolean) {
        mPagingData = mPagingData.filter(predicate)
        submitData(mPagingData)
    }

    fun filterItem(lifecycle: Lifecycle, predicate: suspend (Data) -> Boolean) {
        mPagingData = mPagingData.filter(predicate)
        submitData(lifecycle, mPagingData)
    }
}