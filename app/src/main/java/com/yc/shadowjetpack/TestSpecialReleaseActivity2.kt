package com.yc.shadowjetpack

import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.jetpacklib.release.YcSpecialViewSmart
import com.yc.shadowjetpack.databinding.TestSpecialReleaseActivity2Binding

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
class TestSpecialReleaseActivity2 : YcBaseActivityPlus<TestSpecialReleaseActivity2Binding>(TestSpecialReleaseActivity2Binding::inflate) {
    private lateinit var mSpecialView: YcSpecialViewSmart

    override fun TestSpecialReleaseActivity2Binding.initView() {
        mSpecialView = YcSpecialViewSmart(rvTestSpecial, flSpecial)
        btnTestSpecial1.setOnClickListener {
            mSpecialView.show(YcSpecialState.DATA_EMPTY)
        }
        btnTestSpecial2.setOnClickListener {
            mSpecialView.recovery()
        }
    }
}