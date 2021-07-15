package com.yc.jetpacklib.recycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *
 */
abstract class YcRecyclerViewAdapter<Data : Any, VB : ViewBinding>(
    protected val createVB: ((LayoutInflater) -> VB)? = null,
) :
    RecyclerView.Adapter<YcViewHolder<VB>>() {

    private var mData: MutableList<Data> = mutableListOf()
    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun addAllData(data: List<Data>, isClear: Boolean = true) {
        mData.clear()
        mData.addAll(data)
    }

    fun addData(data: Data) {
        mData.add(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YcViewHolder<VB> {
        return YcViewHolder(createVB!!.invoke(LayoutInflater.from(parent.context)))
    }

    private var mItemClick: ((item: Data, position: Int) -> Unit)? = null
    fun setItemClick(block: (item: Data, position: Int) -> Unit) {
        mItemClick = block
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