package com.yc.jetpacklib.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
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
import com.yc.jetpacklib.manager.YcActivityManager
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.recycleView.YcPagingDataAdapter
import com.yc.jetpacklib.refresh.YcRefreshBaseUtil
import com.yc.jetpacklib.utils.YcViewModelLazy
import com.yc.jetpacklib.widget.dialog.YcLoadingDialog
import kotlinx.coroutines.launch


/**
 * Creator: yc
 * Date: 2021/6/9 16:47
 * UseDes:
 */
@SuppressLint("SetTextI18n")
abstract class YcBaseActivity<VB : ViewBinding>(private val createVB: ((LayoutInflater) -> VB)? = null) : AppCompatActivity() {
    protected lateinit var mViewBinding: VB
    protected open lateinit var mYcLoadingDialog: YcLoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mYcLoadingDialog = YcLoadingDialog(this, this)
        if (createVB != null) {
            mViewBinding = createVB.invoke(LayoutInflater.from(this as Context))
            setContentView(mViewBinding.root)
            initView()
        }
        YcActivityManager.addActivity(this)
    }

    override fun onDestroy() {
        YcActivityManager.finishActivity(this)
        super.onDestroy()
    }

    fun getContext(): Context {
        return this
    }

    abstract fun initView()

    @MainThread
    protected inline fun <reified VM : YcBaseViewModel> ComponentActivity.ycViewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
        return YcViewModelLazy(VM::class, { viewModelStore }, factoryPromise, {
            it.mIsShowLoading.observe(this@YcBaseActivity) {
                if (it.isShow) {
                    showLoading(it.msg)
                } else {
                    hideLoading()
                }
            }
        })
    }

    open fun showLoading(msg: String? = null) {
        mYcLoadingDialog.show(msg)
    }

    open fun hideLoading() {
        mYcLoadingDialog.hide()
    }

    protected fun launch(block: suspend () -> Unit) = lifecycleScope.launch {
        block()
    }

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) {
        this.observe(this@YcBaseActivity, observer)
    }

    protected fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.acSubmitData(pagingData: PagingData<T>) {
        this.submitData(this@YcBaseActivity.lifecycle, pagingData)
    }

    protected fun <Data : Any, VB : ViewBinding> YcPagingDataAdapter<Data, VB>.acSubmitData(pagingData: PagingData<Data>) {
        this.ycSubmitData(this@YcBaseActivity, pagingData)
    }

    protected fun <Data : Any> YcRefreshBaseUtil<Data>.acSetPagingData(pagingData: PagingData<Data>) {
        this.setPagingData(this@YcBaseActivity, pagingData)
    }

    protected fun <Data : Any> YcRefreshBaseUtil<Data>.acClearPagingData() {
        this.clearPagingData(this@YcBaseActivity)
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
