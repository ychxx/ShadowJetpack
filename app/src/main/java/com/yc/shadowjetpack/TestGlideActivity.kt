package com.yc.shadowjetpack

import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.download.YcDownload
import com.yc.jetpacklib.extension.ycLogE
import com.yc.shadowjetpack.databinding.TestGlideActivityBinding


class TestGlideActivity : YcBaseActivityPlus<TestGlideActivityBinding>(TestGlideActivityBinding::inflate) {
    override fun TestGlideActivityBinding.initView() {
        btnLoadImg.setOnClickListener {
            YcDownload.createDownloadPng("http://10.1.3.242:9009/files/1.jpg", onSuccess = {
                ycLogE("下载成功" + it?.name)
            }, onFail = {
                ycLogE(it!!)
            }).start() {
                it.addHeader("token", "123")
            }
//            ivTest.ycLoadImageToken("http://10.1.3.242:9009/files/1.jpg", HashMap<String, String>())
        }
    }
}