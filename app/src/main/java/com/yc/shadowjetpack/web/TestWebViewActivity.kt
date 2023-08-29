package com.yc.shadowjetpack.web

import android.Manifest
import android.content.Intent
import android.webkit.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycLogE
import com.yc.jetpacklib.permission.YcPermissionHelper
import com.yc.jetpacklib.vue3.YcVueSelectorUtil
import com.yc.jetpacklib.vue3.YcWebChromeClient
import com.yc.shadowjetpack.BuildConfig
import com.yc.shadowjetpack.databinding.TestWebViewActivityBinding

class TestWebViewActivity : YcBaseActivityPlus<TestWebViewActivityBinding>(TestWebViewActivityBinding::inflate) {
    private lateinit var mYcWebChromeClient: YcWebChromeClient
    private val mYcPermissionHelper by lazy { YcPermissionHelper(this) }
    private lateinit var mLauncherRectify: ActivityResultLauncher<String>

    override fun TestWebViewActivityBinding.initView() {
        mYcPermissionHelper.mPermission = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray()
        mYcPermissionHelper.mSuccessCall = { ycLogE("申请权限成功") }
        mYcPermissionHelper.startOnCreate()
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)//开启debug模式
        }
        mLauncherRectify = registerForActivityResult(ActivityResultContracts.GetContent()) {
            when (mYcWebChromeClient.mSelectorType) {
                YcVueSelectorUtil.IMG_SELECT -> {
                    mYcWebChromeClient.setOpenImgResult(it)
                }
                YcVueSelectorUtil.IMG_CAMERA -> {
                    mYcWebChromeClient.setOpenImgResult(it)
                }
                YcVueSelectorUtil.VIDEO_SELECT -> {
                    mYcWebChromeClient.setOpenImgResult(it)
                }
                YcVueSelectorUtil.VIDEO_CAMERA -> {
                    mYcWebChromeClient.setOpenImgResult(it)
                }
                YcVueSelectorUtil.SPEECH_RECOGNITION -> {
                    mYcWebChromeClient.setOpenImgResult(it)
                }
                YcVueSelectorUtil.IMG_CAMERA_WITNESS_SAMPLE -> {
                    mYcWebChromeClient.setOpenImgResult(it)//TODO
                }
                YcVueSelectorUtil.IMG_SELECT_1, YcVueSelectorUtil.VIDEO_SELECT_1, YcVueSelectorUtil.VIDEO_CAMERA_1, YcVueSelectorUtil.ERROR -> {
                    mYcWebChromeClient.setOpenImgResult(it)
                }
            }
        }
        mYcWebChromeClient = YcWebChromeClient().apply {
            mToNext = {
                when (it) {
                    YcVueSelectorUtil.IMG_SELECT -> {
                        mLauncherRectify.launch("image/*")
                    }
                    YcVueSelectorUtil.IMG_CAMERA -> {
                        mYcWebChromeClient.setOpenImgResult(null)
                        showToast("去拍照")
                    }
                    YcVueSelectorUtil.VIDEO_SELECT -> {
                        mLauncherRectify.launch("video/*")
                    }
                    YcVueSelectorUtil.VIDEO_CAMERA -> {
                        mYcWebChromeClient.setOpenImgResult(null)
                        showToast("录像")
                    }
                    YcVueSelectorUtil.SPEECH_RECOGNITION -> {
                        mYcWebChromeClient.setOpenImgResult(null)
                        showToast("语音识别")
                    }
                    YcVueSelectorUtil.IMG_SELECT_1, YcVueSelectorUtil.IMG_CAMERA_WITNESS_SAMPLE, YcVueSelectorUtil.VIDEO_SELECT_1, YcVueSelectorUtil.VIDEO_CAMERA_1, YcVueSelectorUtil.ERROR -> {
                        mYcWebChromeClient.setOpenImgResult(null)
                        showToast("未知,请更新app")
                    }
                }
            }
        }
        wb.settings.ycSettingInit()
        wb.webChromeClient = mYcWebChromeClient
        btnTestClear.setOnClickListener {
            wb.clearCache(true)
        }
        btnTestRefresh.setOnClickListener {
            wb.loadUrl(edtUrl.text.toString())
        }

    }

    private fun WebSettings.ycSettingInit() {
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        javaScriptEnabled = true
        loadWithOverviewMode = true
        domStorageEnabled = true
        blockNetworkImage = false
        useWideViewPort = true
    }
}