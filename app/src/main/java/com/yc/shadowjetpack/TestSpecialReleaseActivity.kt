package com.yc.shadowjetpack

import android.R
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.release.YcSpecialViewSimple
import com.yc.shadowjetpack.databinding.TestSpecialReleaseActivityBinding
/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
class TestSpecialReleaseActivity : YcBaseActivityPlus<TestSpecialReleaseActivityBinding>(TestSpecialReleaseActivityBinding::inflate) {
    private lateinit var mSpecialView: YcSpecialViewSimple
    private lateinit var mSpecialView2: YcSpecialViewSimple
    override fun TestSpecialReleaseActivityBinding.initView() {
        mSpecialView = YcSpecialViewSimple(this@TestSpecialReleaseActivity)
        mSpecialView2 = YcSpecialViewSimple(rvTestSpecial)
        btnTestSpecial1.setOnClickListener {
            mSpecialView.apply {
                mCustomUi = {
                    releaseContentTv.text = "测试111"
                    releaseButtonBtn.text = "恢复"
                    releaseButtonBtn.setOnClickListener {
                        mSpecialView.recovery()
                    }
                }
                mSpecialView.show()
            }
        }
        btnTestSpecial2.setOnClickListener {
            mSpecialView2.apply {
                mCustomUi = {
                    releaseContentTv.text = "测试111"
                    releaseButtonBtn.text = "恢复"
                    releaseButtonBtn.setOnClickListener {
                        mSpecialView2.recovery()
                    }
                }
                mSpecialView2.show()
            }
        }
    }
}