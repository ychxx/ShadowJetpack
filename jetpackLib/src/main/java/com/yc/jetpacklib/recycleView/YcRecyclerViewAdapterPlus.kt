package com.yc.jetpacklib.recycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *
 */
abstract class YcRecyclerViewAdapterPlus<Data, VB : ViewBinding>(protected val createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    RecyclerView.Adapter<YcViewHolder<VB>>() {
    var mItemClick: ((item: Data, position: Int) -> Unit)? = null

    private var mData: MutableList<Data> = mutableListOf()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YcViewHolder<VB> {
        return YcViewHolder(createVB.invoke(LayoutInflater.from(parent.context), parent, false))
    }


    open fun getItem(position: Int): Data? {
        return if (position < 0 || position >= mData.size) null else mData[position]
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
            holder.viewBinding.onUpdate(position, dataBean!!)
        } catch (e: Exception) {
            Log.e("ycEvery", "onBindViewHolder爆炸啦")
            e.printStackTrace()
        }
    }

    abstract fun VB.onUpdate(position: Int, data: Data)
}