package com.yc.shadowjetpack

import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycLoadImageResCircleCenterInside
import com.yc.shadowjetpack.databinding.TestGlideActivityBinding


class TestGlideActivity : YcBaseActivityPlus<TestGlideActivityBinding>(TestGlideActivityBinding::inflate) {
    override fun TestGlideActivityBinding.initView() {
        ivCertificateBg.ycLoadImageResCircleCenterInside(R.drawable.ic_certificate_bg,15)
    }


}