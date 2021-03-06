package com.yc.jetpacklib.widget.pickerview

import android.app.Activity
import android.view.View
import androidx.annotation.StringDef
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.yc.jetpacklib.extension.ycIsNotEmpty
import java.util.*

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-09-29
 * UseDes:滚轮选择器工具
 */

object YcPickerView {

    const val YearMonthDate = "YearMonthDate"  //只显示年月日
    const val YearMonth = "YearMouth" //只显示年月
    const val HourMinute = "HourMinute" //只显示时分
    const val Dafult = "Dafult" //默认

    @StringDef(Dafult, YearMonthDate, YearMonth, HourMinute)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class Type

    /*默认条件选择器*/
    fun setDefaultConditionPicker(
        activity: Activity,
        titleText: String? = null,
        pickerList: List<String>? = null,
        selectCallBack: ((Int, Int, Int, View?) -> Unit)? = null
    ): OptionsPickerView<String> {
        val pvCondition: OptionsPickerView<String> = OptionsPickerBuilder(activity) { options1: Int, options2: Int, options3: Int, v: View? ->
            selectCallBack?.invoke(options1, options2, options3, v)
        }.pickerDefaultStyle(activity).build()
        if (titleText.ycIsNotEmpty()) pvCondition.setTitleText(titleText)
        if (pickerList.ycIsNotEmpty()) pvCondition.setPicker(pickerList) //二级选择器
        return pvCondition
    }


    /*时间选择器*/
    fun setTimePicker(
        activity: Activity,
        @Type type: String,
        titleText: String? = null,
        selectedDefaultValue: Calendar? = null,
        selectCallBack: ((Date, View?) -> Unit)? = null
    ): TimePickerView {
        val pvTime = TimePickerBuilder(activity) { date, v ->
            selectCallBack?.invoke(date, v)
        }.apply {
            when (type) {
                YearMonthDate -> setType(booleanArrayOf(true, true, true, false, false, false))//只显示年月日
                YearMonth -> setType(booleanArrayOf(true, true, false, false, false, false))//只显示年月
                HourMinute -> setType(booleanArrayOf(false, false, false, true, true, false))//只显示时分
                else -> setType(booleanArrayOf(true, true, true, true, true, true))//只显示年月
            }
        }.timePickerStyle(activity).build()
        pvTime.setTimePickerDialogStyle()
        if (titleText.ycIsNotEmpty()) pvTime.setTitleText(titleText)
        if (selectedDefaultValue != null) pvTime.setDate(selectedDefaultValue)
        return pvTime
    }
}




