package com.yc.shadowjetpack

import android.view.View
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.widget.pickerview.YcPickerView
import com.yc.shadowjetpack.databinding.TestPickerviewActivityBinding


/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-09-29
 * UseDes:
 */
class TestPickerViewActivity : YcBaseActivityPlus<TestPickerviewActivityBinding>(TestPickerviewActivityBinding::inflate) {

    private val dateList = listOf("1", "2", "3", "4", "5")
    override fun TestPickerviewActivityBinding.initView() {
        btnSinglePickerView.setOnClickListener {
            YcPickerView.showDefaultPickerWithCondition(this@TestPickerViewActivity, "默认条件选择器", dateList) { options1: Int, _: Int, _: Int, _: View? ->
                showToast("选择了$options1")
            }
        }

        btnTimePickerView.setOnClickListener {
            YcPickerView.showStartTimePicker(this@TestPickerViewActivity, YcPickerView.YearMonthDate, "时间选择器") { date, v ->
                showToast("选择了${date.toString()}")
            }
        }
    }


}