package com.yc.shadowjetpack

import android.view.View
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
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
    private var mPvTime: TimePickerView? = null
    private var mPvCondition: OptionsPickerView<String>? = null
    private val dateList = listOf("1", "2", "3", "4", "5")
    override fun TestPickerviewActivityBinding.initView() {
        btnSinglePickerView.setOnClickListener {
            if (mPvCondition == null) {
                YcPickerView.setDefaultConditionPicker(this@TestPickerViewActivity, "默认条件选择器", dateList) { options1: Int, _: Int, _: Int, _: View? ->
                    showToast("选择了$options1")
                }
            }
            mPvCondition!!.show()
        }

        btnTimePickerView.setOnClickListener {
            if (mPvTime == null) {
                mPvTime = YcPickerView.setTimePicker(this@TestPickerViewActivity, YcPickerView.YearMonthDate, "时间选择器") { date, _ ->
                    showToast("选择了$date")
                }
            }
            mPvTime!!.show()
        }
    }


}