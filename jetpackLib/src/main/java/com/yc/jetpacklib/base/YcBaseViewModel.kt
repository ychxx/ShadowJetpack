package com.yc.jetpacklib.base

import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yc.jetpacklib.data.entity.YcLoadingBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Creator: yc
 * Date: 2021/6/15 16:45
 * UseDes:
 */
open class YcBaseViewModel : ViewModel() {
    /**
     * 用于显示加载框
     */
    protected val _mIsShowLoading = MutableLiveData<YcLoadingBean>()
    val mIsShowLoading: LiveData<YcLoadingBean> = _mIsShowLoading
    protected var mLoadingJob: Job? = null
    protected var mLoadingShowJob: Job? = null
    protected open var mDefaultDelayTime = 1500L
    protected inline fun ycLaunch(crossinline block: suspend (coroutineScope: CoroutineScope) -> Unit): Job {
        return viewModelScope.launch {
            block(this)
        }
    }

    protected inline fun <Data> Flow<Data>.ycCollect(crossinline block: (Data) -> Unit) {
        viewModelScope.launch {
            this@ycCollect.collect {
                block.invoke(it)
            }
        }
    }

    /**
     * 冷流开始时，显示加载框
     * 冷流结束时，自动隐藏加载框
     */
    protected fun <T> Flow<T>.ycHasLoading(msg: String? = null, delayTime: Long = mDefaultDelayTime): Flow<T> = onStart {
        showLoading(msg, delayTime)
    }.onCompletion {
        hideLoading()
    }
//    protected fun <T> Flow<T>.ycHasLoading(delayTime: Long = mDefaultDelayTime): Flow<T> {
//        this.onStart {
//            showLoading(delayTime)
//        }.onCompletion {
//            hideLoading()
//        }
//        return this
//    }

    /**
     * 冷流开始时，显示加载框
     * 冷流结束时，不会隐藏加载框，需手动调用hideLoading方法
     */
    protected fun <T> Flow<T>.ycHasLoadingNoAutoClose(msg: String? = null, delayTime: Long = mDefaultDelayTime): Flow<T> = onStart {
        showLoading(msg, delayTime)
    }

    /**
     * 显示加载框
     * @param delayTime Long  延迟delayTime毫秒后，再显示，减少请求很快返回时，导致闪下的体验。
     * 如果在delayTime毫秒内，请求就完成了，对话框就不会显示
     */
    protected fun showLoading(msg: String? = null, delayTime: Long = mDefaultDelayTime): Job {
        mLoadingShowJob?.cancel()
        mLoadingShowJob = viewModelScope.launch {
            delay(delayTime)
            _mIsShowLoading.postValue(YcLoadingBean.show(msg))
        }
        return mLoadingShowJob!!
    }

    /**
     * 隐藏加载框
     */
    protected fun hideLoading() {
        _mIsShowLoading.postValue(YcLoadingBean.hide())
        mLoadingJob?.cancel()
        mLoadingShowJob?.cancel()
    }

    override fun onCleared() {
        hideLoading()
        super.onCleared()
    }
}