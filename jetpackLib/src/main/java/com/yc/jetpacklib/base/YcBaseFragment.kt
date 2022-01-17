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
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.utils.YcViewModelLazy
import com.yc.jetpacklib.widget.dialog.YcLoadingDialog
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/6/9 19:50
 * UseDes:
 */
abstract class YcBaseFragment<VB : ViewBinding>(private val createVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) : Fragment() {
    private var _mViewBinding: VB? = null
    protected val mViewBinding get() = _mViewBinding!!
    protected open lateinit var mYcLoadingDialog: YcLoadingDialog
    protected var mIsShow: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (createVB != null) {
            mYcLoadingDialog = YcLoadingDialog(requireContext(), this)
            _mViewBinding = createVB.invoke(inflater, container, false)
            _mViewBinding!!.root
        } else {
            mYcLoadingDialog = YcLoadingDialog(requireContext(), this)
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
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

    open fun showLoading() {
        mYcLoadingDialog.show()
    }

    open fun hideLoading() {
        mYcLoadingDialog.hide()
    }

    protected fun launch(block: suspend () -> Unit) {
        lifecycleScope.launch {
            block()
        }
    }

    protected fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.ycSubmitData(pagingData: PagingData<T>) {
        this.submitData(this@YcBaseFragment.viewLifecycleOwner.lifecycle, pagingData)
    }

    /**
     * @param defaultMsg String 当错误msg为空时，显示的提示
     */
    protected fun YcResult<*>.ycShowNetError(defaultMsg: String = "请求失败,错误msg为空") {
        if (this is YcResult.Fail) {
            exception.ycShowNetError(defaultMsg)
        }
    }

    protected fun YcException.ycShowNetError(defaultMsg: String = "请求失败,错误msg为空") {
        showToast(msg ?: defaultMsg)
    }
}