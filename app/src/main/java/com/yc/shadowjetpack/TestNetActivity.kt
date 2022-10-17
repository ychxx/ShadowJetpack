package com.yc.shadowjetpack

import android.Manifest
import android.media.MediaRecorder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.base.YcRepository
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycFlow
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.file.YcFileUtils
import com.yc.jetpacklib.init.YcJetpack
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.net.YcRetrofitUtil
import com.yc.jetpacklib.permission.YcPermissionHelper
import com.yc.shadowjetpack.databinding.TestNetActivityBinding
import com.yc.shadowjetpack.databinding.TestSoundActivityBinding
import com.yc.shadowjetpack.net.ApiService
import kotlinx.coroutines.delay
import java.io.File


class TestNetActivity : YcBaseActivityPlus<TestNetActivityBinding>(TestNetActivityBinding::inflate) {
    class NetVM : YcBaseViewModel() {
        val mNetRepository by lazy { NetRepository() }
        private val _mNewTaskResult = MutableLiveData<YcResult<Any>>()
        val mNewTaskResult: LiveData<YcResult<Any>> = _mNewTaskResult
        fun getVersion() {
            mNetRepository.getVersion().ycCollect {
                ycLogE("444444444")
                _mNewTaskResult.postValue(it)
            }
        }
    }

    class NetRepository : YcRepository() {
        protected val mApiService: ApiService by lazy { YcRetrofitUtil.Instance.getApiService(ApiService::class.java) }
        fun getVersion() = ycFlow<Any> {
            ycLogE("3333333333")
            delay(5000)
            val json = mApiService.getVersion("1", "com.hc.oa")
            send(YcResult.Success(json))
        }
    }

    private val mNetVM by ycViewModels<NetVM>()
    override fun TestNetActivityBinding.initView() {
        YcJetpack.mInstance.mDefaultBaseUrl = "http://update.jsqqy.com:8081/"
        btnTestSend.setOnClickListener {
            ycLogE("11111111")
            mNetVM.getVersion()
        }
        mNetVM.mNewTaskResult.observe {
            ycLogE("222222222222")
        }
    }
}