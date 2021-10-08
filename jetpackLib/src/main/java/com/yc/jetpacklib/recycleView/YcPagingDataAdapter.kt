package com.yc.jetpacklib.recycleView

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/2/2 20:39
 * UseDes:
 * 封装PagingDataAdapter
 */
abstract class YcPagingDataAdapter<Data : Any, VB : ViewBinding>(
    protected val createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    diffCallback: DiffUtil.ItemCallback<Data>
) : PagingDataAdapter<Data, YcViewHolder<VB>>(diffCallback) {
    companion object {
        fun <Data : Any, VB : ViewBinding> ycLazyInit(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>
        ): Lazy<YcPagingDataAdapter<Data, VB>> = lazy {
            return@lazy object : YcPagingDataAdapter<Data, VB>(createVB, diffCallback) {}
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitPosition(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            diffCallback: DiffUtil.ItemCallback<Data>
        ): Lazy<YcPagingDataAdapter<Data, VB>> = lazy {
            return@lazy object : YcPagingDataAdapter<Data, VB>(createVB, diffCallback) {}
        }
    }

    var mItemClick: ((data: Data, position: Int) -> Unit)? = null

    protected lateinit var mContext: Context
    var mOnUpdate: (VB.(data: Data, position: Int) -> Unit)? = null


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
            mOnUpdate?.invoke(holder.viewBinding, dataBean!!, position)
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