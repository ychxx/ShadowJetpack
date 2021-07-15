package com.yc.jetpacklib.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.yc.jetpacklib.utils.YcViewModelLazy
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/6/9 19:50
 * UseDes:
 */
abstract class YcBaseFragment<VB : ViewBinding>(private val createVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) : Fragment() {
    private var _mViewBinding: VB? = null
    protected val mViewBinding get() = _mViewBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (createVB != null) {
            _mViewBinding = createVB.invoke(inflater, container, false)
            _mViewBinding!!.root
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    protected abstract fun initView(view: View, savedInstanceState: Bundle?)
    override fun onDestroy() {
        super.onDestroy()
        _mViewBinding = null
    }

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) {
        this.observe(this@YcBaseFragment, observer)
    }

    @MainThread
    protected inline fun <reified VM : YcBaseViewModel> Fragment.ycViewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
        return YcViewModelLazy(VM::class, { viewModelStore }, factoryPromise, {
            it.mIsShowLoading.observe(this@YcBaseFragment) {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        })
    }

    @MainThread
    protected inline fun <reified VM : YcBaseViewModel> Fragment.ycViewModelsActivity(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: {
            requireActivity().defaultViewModelProviderFactory
        }
        return YcViewModelLazy(VM::class, { requireActivity().viewModelStore }, factoryPromise, {
            it.mIsShowLoading.observe(requireActivity()) {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        })
    }

    protected fun showLoading() {}
    protected fun hideLoading() {}

    protected fun launch(block: suspend () -> Unit) {
        lifecycleScope.launch {
            block()
        }
    }

    protected inline fun createResultLauncher(crossinline success: ((result: ActivityResult) -> Unit)) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                success(it)
            }
        }

    protected inline fun createResultLauncher(
        crossinline success: ((result: ActivityResult) -> Unit),
        crossinline fail: ((result: ActivityResult) -> Unit)
    ) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            success(it)
        } else {
            fail(it)
        }
    }
}