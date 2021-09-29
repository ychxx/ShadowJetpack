package com.yc.shadowjetpack

import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.shadowjetpack.databinding.TestPickerviewActivityBinding


/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-09-29
 * UseDes:
 */
class TestPickerViewActivity : YcBaseActivityPlus<TestPickerviewActivityBinding>(TestPickerviewActivityBinding::inflate) {


    override fun TestPickerviewActivityBinding.initView() {
        btnSinglePickerView.setOnClickListener {


        }
    }


}