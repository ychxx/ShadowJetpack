package com.yc.shadowjetpack

import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.jetpacklib.release.YcSpecialViewCommon
import com.yc.jetpacklib.release.YcSpecialViewConfigure
import com.yc.jetpacklib.release.YcSpecialViewConfigureImp
import com.yc.shadowjetpack.databinding.TestSpecialReleaseActivityBinding

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
class TestSpecialReleaseActivity : YcBaseActivityPlus<TestSpecialReleaseActivityBinding>(TestSpecialReleaseActivityBinding::inflate) {
    private lateinit var mSpecialView: YcSpecialViewCommon
    private lateinit var mSpecialView2: YcSpecialViewCommon
    override fun TestSpecialReleaseActivityBinding.initView() {
        mSpecialView = YcSpecialViewCommon(this@TestSpecialReleaseActivity)
        mSpecialView2 = YcSpecialViewCommon(rvTestSpecial)
        btnTestSpecial1.setOnClickListener {
            (mSpecialView.mSpecialViewBuild as TestSpecialViewConfigureImp).apply {
                this.mPriorityUpdate = { specialState: Int, exception: YcException? ->
                    this.releaseContentTv.text = ""
                    releaseButtonBtn.text = "恢复"
                    releaseButtonBtn.setOnClickListener {
                        mSpecialView.recovery()
                    }
                    true
                }
            }
//            (mSpecialView.mSpecialViewBuild as YcSpecialViewBuildImp).mFinally = {
//                releaseContentTv.text = "测试111"
//                releaseButtonBtn.text = "恢复"
//                releaseButtonBtn.setOnClickListener {
//
//                }
//            }
            mSpecialView.show(YcSpecialState.DATA_EMPTY_ERROR)
        }
        btnTestSpecial2.setOnClickListener {
//            mSpecialView2.apply {
//                (mSpecialViewBuild as YcSpecialViewBuildImp).mViewBinding.apply {
//                    releaseContentTv.text = "测试111"
//                    releaseButtonBtn.text = "恢复"
//                    releaseButtonBtn.setOnClickListener {
//                        mSpecialView2.recovery()
//                    }
//                }
//                mSpecialView2.show()
//            }
        }
    }
}