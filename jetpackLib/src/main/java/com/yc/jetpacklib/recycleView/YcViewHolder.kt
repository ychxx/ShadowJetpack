package com.yc.jetpacklib.recycleView

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *
 */
open class YcViewHolder<VB : ViewBinding>(val viewBinding: VB) : RecyclerView.ViewHolder(viewBinding.root) {

}

