package com.yc.jetpacklib.recycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *
 */
open class YcRecyclerViewAdapter<Data : Any, VB : ViewBinding>(protected val createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    RecyclerView.Adapter<YcViewHolder<VB>>(), YcIAdapter<Data, VB> {
    companion object {
        fun <Data : Any, VB : ViewBinding> ycLazyInit(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            updateCall: VB.(data: Data) -> Unit
        ): Lazy<YcRecyclerViewAdapter<Data, VB>> = lazy {
            YcRecyclerViewAdapter<Data, VB>(createVB).apply {
                mOnUpdate = updateCall
            }
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitApply(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            apply: (YcRecyclerViewAdapter<Data, VB>.() -> Unit)? = null
        ): Lazy<YcRecyclerViewAdapter<Data, VB>> = lazy {
            YcRecyclerViewAdapter<Data, VB>(createVB).apply {
                apply?.invoke(this)
            }
        }

        fun <Data : Any, VB : ViewBinding> ycLazyInitPosition(
            createVB: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            updateCall: VB.(position: Int, data: Data) -> Unit
        ): Lazy<YcRecyclerViewAdapter<Data, VB>> = lazy {
            YcRecyclerViewAdapter<Data, VB>(createVB).apply {
                mOnUpdate2 = updateCall
            }
        }
    }

    protected open var mData: MutableList<Data> = mutableListOf()
    override var mItemClick: ((item: Data) -> Unit)? = null
    override var mItemClick2: ((item: Data, position: Int) -> Unit)? = null
    override var mOnUpdate: (VB.(data: Data) -> Unit)? = null
    override var mOnUpdate2: (VB.(position: Int, data: Data) -> Unit)? = null

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
                mItemClick?.invoke(dataBean!!)
                mItemClick2?.invoke(dataBean!!, position)
            }
            mOnUpdate?.invoke(holder.viewBinding, dataBean!!)
            mOnUpdate2?.invoke(holder.viewBinding, position, dataBean!!)
        } catch (e: Exception) {
            Log.e("ycEvery", "onBindViewHolder爆炸啦")
            e.printStackTrace()
        }
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun addAllData(data: List<Data>?, isClear: Boolean = true) {
        if (isClear)
            mData.clear()
        if (data == null) {
            mData.clear()
        } else {
            mData.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun addData(data: Data, isRefresh: Boolean = false) {
        mData.add(data)
        if (isRefresh)
            notifyDataSetChanged()
    }
}