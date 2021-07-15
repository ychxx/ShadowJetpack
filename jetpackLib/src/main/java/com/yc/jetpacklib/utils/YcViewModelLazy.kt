package com.yc.jetpacklib.utils

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.yc.jetpacklib.base.YcBaseViewModel
import kotlin.reflect.KClass

/**
 * Creator: yc
 * Date: 2021/6/24 18:21
 * UseDes:复制ViewModelLazy在其基础上添加一个init方法，用于初始化
 */
class YcViewModelLazy<VM : YcBaseViewModel>(
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val init: (VM) -> Unit
) : Lazy<VM> {
    private var cached: VM? = null
    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory).get(viewModelClass.java).also {
                    init.invoke(it)
                    cached = it
                }
            } else {
                viewModel
            }
        }
    override fun isInitialized(): Boolean = cached != null
}