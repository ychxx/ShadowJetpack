package com.yc.jetpacklib.recycleView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

/**
 * Creator: yc
 * Date: 2021/2/2 20:39
 * UseDes:
 * 封装PagingDataAdapter
 */
abstract class YcPagingDataAdapter<Data : Any, VB : ViewBinding>(
    private val createVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null,
    diffCallback: DiffUtil.ItemCallback<Data>

) : PagingDataAdapter<Data, YcViewHolder<VB>>(diffCallback) {
    private var mItemClick: ((Data, Int) -> Unit)? = null

    protected lateinit var mContext: Context

    fun setItemClick(block: (Data, Int) -> Unit) {
        mItemClick = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YcViewHolder<VB> {
        mContext = parent.context
        return YcViewHolder(createVB!!.invoke(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: YcViewHolder<VB>, position: Int) {
        try {
            val dataBean = getItem(position)
            holder.viewBinding.apply {
                this.root.setOnClickListener {
                    mItemClick?.let { it1 -> it1(dataBean!!, position) }
                }
            }
            onUpdate(holder, position, dataBean!!)
        } catch (e: Exception) {
            Log.e("ycEvery", "onBindViewHolder爆炸啦")
            e.printStackTrace()
        }
    }

    abstract fun onUpdate(holder: YcViewHolder<VB>, position: Int, data: Data)
}