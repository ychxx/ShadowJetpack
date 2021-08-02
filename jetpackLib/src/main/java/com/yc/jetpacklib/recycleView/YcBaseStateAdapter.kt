package com.yc.jetpacklib.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.viewbinding.ViewBinding

/**
 *
 */
abstract class YcBaseStateAdapter<VB : ViewBinding>(protected val createVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) :
    LoadStateAdapter<YcViewHolder<VB>>() {
    var mViewHolder: YcViewHolder<VB>? = null
    override fun onBindViewHolder(holder: YcViewHolder<VB>, loadState: LoadState) {
        holder.viewBinding.onUpdate(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): YcViewHolder<VB> {
        return YcViewHolder(createVB!!.invoke(LayoutInflater.from(parent.context), parent, false)).apply {
            mViewHolder = this
        }
    }

    abstract fun VB.onUpdate(loadState: LoadState)
}