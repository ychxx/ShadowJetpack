package com.yc.shadowjetpack

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.alipay.mobile.android.verify.sdk.ServiceFactory
import com.alipay.mobile.android.verify.sdk.interfaces.IService
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.shadowjetpack.databinding.TestAlipayLivingBodyTestActivityBinding
import com.yc.shadowjetpack.databinding.TestLoadingActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
class TestAliPayLivingBodyTestActivity : YcBaseActivityPlus<TestAlipayLivingBodyTestActivityBinding>(TestAlipayLivingBodyTestActivityBinding::inflate) {
    private lateinit var mAliService: IService
    override fun TestAlipayLivingBodyTestActivityBinding.initView() {
        mAliService = ServiceFactory.create(this@TestAliPayLivingBodyTestActivity).build()
        button2.setOnClickListener {
            AliveFaceUtil().startFace(
                "https://gw.alipayobjects.com/as/g/mPaaS-Resources/iv-mPaaS/1.0.4/iv/index.html#/IV/liveness/dtfv_3263d76381796dc7154914d3ba71e5c2",
                "dtfv_3263d76381796dc7154914d3ba71e5c2",
                mAliService
            ) {
                when (it["resultStatus"]) {
                    "9000", "6003" -> {
//                        val intent = Intent()
//                        intent.putExtra(LIVING_BODY_TEST_RESULT, true)
//                        setResult(RESULT_OK, intent)
//                        finish()
                    }
                    "4000" -> ("活体检测取消!")
                    "6001" -> showToast("活体检测取消!")
                    "6002" -> showToast("活体网络异常!")
                    else -> showToast("活体检测失败，请重试!")
                }
            }
        }
    }
}