package com.yc.jetpacklib.recycleView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.filter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

/**
 * Creator: yc
 * Date: 2021/2/2 20:39
 * UseDes:
 * 封装PagingDataAdapter
 */
abstract class YcPagingDataAdapterPlus<Data : Any, VB : ViewBinding>(
    protected val createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    diffCallback: DiffUtil.ItemCallback<Data>
) : PagingDataAdapter<Data, YcViewHolder<VB>>(diffCallback) {
    companion object {
        fun <Data : Any, VB : ViewBinding> ycLazyInit(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            block: VB.(data: Data) -> Unit
        ): Lazy<YcPagingDataAdapterPlus<Data, VB>> = lazy {
            return@lazy object : YcPagingDataAdapterPlus<Data, VB>(createVB, diffCallback) {
                override fun VB.onUpdate(position: Int, data: Data) {
                    block.invoke(this, data)
                }
            }
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitPosition(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>,
            block: VB.(position: Int) -> Unit
        ): Lazy<YcPagingDataAdapterPlus<Data, VB>> = lazy {
            return@lazy object : YcPagingDataAdapterPlus<Data, VB>(createVB, diffCallback) {
                override fun VB.onUpdate(position: Int, data: Data) {
                    block.invoke(this, position)
                }
            }
        }
    }

    var mItemClick: ((Data, Int) -> Unit)? = null

    protected lateinit var mContext: Context
    protected lateinit var mPagingData: PagingData<Data>
    abstract fun VB.onUpdate(position: Int, data: Data)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YcViewHolder<VB> {
        mContext = parent.context
        return YcViewHolder(createVB.invoke(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: YcViewHolder<VB>, position: Int) {
        try {
            val dataBean = getItem(position)
            holder.viewBinding.root.setOnClickListener {
                mItemClick?.invoke(dataBean!!, position)
            }
            holder.viewBinding.onUpdate(position, dataBean!!)
        } catch (e: Exception) {
            Log.e("ycEvery", "onBindViewHolder爆炸啦")
            e.printStackTrace()
        }
    }
}