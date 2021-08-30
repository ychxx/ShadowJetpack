package com.yc.jetpacklib.recycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *
 */
open class YcRecyclerViewAdapterPlus<Data, VB : ViewBinding>(protected val createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    RecyclerView.Adapter<YcViewHolder<VB>>() {

    private var mData: MutableList<Data> = mutableListOf()
    private var mItemClick: ((item: Data, position: Int) -> Unit)? = null
    var mOnUpdate: (VB.(data: Data) -> Unit)? = null
    var mOnUpdate2: (VB.(position: Int, data: Data) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YcViewHolder<VB> {
        return YcViewHolder(createVB.invoke(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: YcViewHolder<VB>, position: Int) {
        try {
            val dataBean = getItem(position)
            holder.viewBinding.root.setOnClickListener {
                mItemClick?.invoke(dataBean!!, position)
            }
            mOnUpdate?.invoke(holder.viewBinding, dataBean!!)
            mOnUpdate2?.invoke(holder.viewBinding, position, dataBean!!)
            holder.viewBinding.onUpdate(position, dataBean!!)
        } catch (e: Exception) {
            Log.e("ycEvery", "onBindViewHolder爆炸啦")
            e.printStackTrace()
        }
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun addAllData(data: List<Data>, isClear: Boolean = true) {
        if (isClear)
            mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: Data, isRefresh: Boolean = false) {
        mData.add(data)
        if (isRefresh)
            notifyDataSetChanged()
    }

    fun setItemClick(block: (item: Data, position: Int) -> Unit) {
        mItemClick = block
    }

    open fun getItem(position: Int): Data? {
        return if (position < 0 || position >= mData.size) null else mData[position]
    }

    open fun VB.onUpdate(position: Int, data: Data) {

    }
}