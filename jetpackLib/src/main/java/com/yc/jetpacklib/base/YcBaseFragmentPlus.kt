package com.yc.jetpacklib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Creator: yc
 * Date: 2021/6/9 19:50
 * UseDes:
 */
abstract class YcBaseFragmentPlus<VB : ViewBinding>(createVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) :
    YcBaseFragment<VB>(createVB) {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mViewBinding.initView(view, savedInstanceState)
    }

    abstract fun VB.initView(view: View, savedInstanceState: Bundle?)
}