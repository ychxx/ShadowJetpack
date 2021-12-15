package com.yc.shadowjetpack

import androidx.lifecycle.viewModelScope
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.shadowjetpack.databinding.TestLoadingActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
class TestShowLoadingActivity : YcBaseActivityPlus<TestLoadingActivityBinding>(TestLoadingActivityBinding::inflate) {
    private val mVM: VM by ycViewModels()
    override fun TestLoadingActivityBinding.initView() {
        btnTestShow.setOnClickListener {
            launch {
                mVM.testShowFlow()
            }
        }
    }

    class VM : YcBaseViewModel() {
        fun testShowFlow() = ycLaunch {
            flow {
                delay(5000)
                emit(true)
            }.ycHasLoading().collectLatest {
                ycLogESimple("$it")
            }
        }


        fun testShowFlowTime() = ycLaunch {
            channelFlow {
                ycLogESimple("emit1：${System.currentTimeMillis()}")
                delay(5000)
                ycLogESimple("emit2：${System.currentTimeMillis()}")
                send(true)
                ycLogESimple("emit3：${System.currentTimeMillis()}")
            }.onStart {
                ycLogESimple("showLoading1：${System.currentTimeMillis()}")
                showLoading()
                ycLogESimple("showLoading2：${System.currentTimeMillis()}")
            }.onCompletion {
                ycLogESimple("hideLoading1：${System.currentTimeMillis()}")
                hideLoading()
                ycLogESimple("hideLoading2：${System.currentTimeMillis()}")
            }.collectLatest {
                ycLogESimple("$it")
                ycLogESimple("collectLatest：${System.currentTimeMillis()}")
            }
        }
    }
}