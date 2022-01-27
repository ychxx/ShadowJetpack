package com.yc.jetpacklib.recycleView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

/**
 * Creator: yc
 * Date: 2021/2/2 20:39
 * UseDes:
 * 封装PagingDataAdapter
 */
open class YcPagingDataAdapter<Data : Any, VB : ViewBinding>(
    protected val createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    diffCallback: DiffUtil.ItemCallback<Data>
) : PagingDataAdapter<Data, YcViewHolder<VB>>(diffCallback), YcIAdapter<Data, VB> {
    companion object {
        fun <Data : Any, VB : ViewBinding> ycLazyInit(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            updateCall: VB.(data: Data) -> Unit
        ): Lazy<YcPagingDataAdapter<Data, VB>> = lazy {
            YcPagingDataAdapter(createVB, diffCallback).apply {
                mOnUpdate = updateCall
            }
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitApply(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            apply: (YcPagingDataAdapter<Data, VB>.() -> Unit)? = null
        ): Lazy<YcPagingDataAdapter<Data, VB>> = lazy {
            YcPagingDataAdapter(createVB, diffCallback).apply {
                apply?.invoke(this)
            }
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitPosition(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            updateCall: VB.(position: Int, data: Data) -> Unit
        ): Lazy<YcPagingDataAdapter<Data, VB>> = lazy {
            YcPagingDataAdapter(createVB, diffCallback).apply {
                mOnUpdate2 = updateCall
            }
        }
    }

    override var mItemClick: ((item: Data) -> Unit)? = null
    override var mItemClick2: ((item: Data, position: Int) -> Unit)? = null
    override var mOnUpdate: (VB.(data: Data) -> Unit)? = null
    override var mOnUpdate2: (VB.(position: Int, data: Data) -> Unit)? = null
    override var mOnUpdate3: (VB.(holder: YcViewHolder<VB>, data: Data) -> Unit)? = null
    protected lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YcViewHolder<VB> {
        mContext = parent.context
        return YcViewHolder(createVB.invoke(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: YcViewHolder<VB>, position: Int) {
        try {
            val dataBean = getItem(holder.bindingAdapterPosition)
            holder.viewBinding.root.setOnClickListener {
                mItemClick?.invoke(dataBean!!)
                mItemClick2?.invoke(dataBean!!, holder.bindingAdapterPosition)
            }
            mOnUpdate?.invoke(holder.viewBinding, dataBean!!)
            mOnUpdate2?.invoke(holder.viewBinding, holder.bindingAdapterPosition, dataBean!!)
            mOnUpdate3?.invoke(holder.viewBinding, holder, dataBean!!)
        } catch (e: Exception) {
            Log.e("ycEvery", "onBindViewHolder爆炸啦")
            e.printStackTrace()
        }
    }

    open fun ycSubmitData(lifecycleOwner: LifecycleOwner, pagingData: PagingData<Data>) {
        submitData(lifecycleOwner.lifecycle, pagingData)
    }

    open suspend fun ycSubmitData(pagingData: PagingData<Data>) {
        submitData(pagingData)
    }
}