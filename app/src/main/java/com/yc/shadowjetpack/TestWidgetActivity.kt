package com.yc.shadowjetpack

import android.os.Build
import androidx.annotation.RequiresApi
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycLoadImageNetCircle
import com.yc.shadowjetpack.databinding.TestWidgetBinding
import java.util.*

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:测试自定义View
 */
class TestWidgetActivity : YcBaseActivityPlus<TestWidgetBinding>(TestWidgetBinding::inflate) {
//    private var progress: Float = 0f
//    private val loop: YcLoop by YcLoop.init(this, 500) {
//        progress++
//        progress %= 100
//        mViewBinding.ring.setProgress(50 / 100f)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun TestWidgetBinding.initView() {
//        loop.start(true)
        this.ring.setText("1234", "/6070")
        ivAvater.ycLoadImageNetCircle(null, Date().toInstant().toString(), R.drawable.ic_avater_default, R.drawable.ic_avater_default)

        //ivAvater.ycLoadImageNetCircle(null, R.drawable.ic_avater_default, R.drawable.ic_avater_default)

        // ivAvater.ycLoadImageNetFilletDp(null, 10f, R.drawable.ic_avater_default, R.drawable.ic_avater_default)

    }
}