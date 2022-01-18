@file:Suppress("DUPLICATE_LABEL_IN_WHEN")

package com.yc.jetpacklib.widget.pickerview

import android.app.Activity
import android.view.View
import androidx.annotation.StringDef
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.yc.jetpacklib.extension.hideSoftInput
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

    var pvCondition: OptionsPickerView<String>? = null
    var pvStartTime: TimePickerView? = null
    var pvEndTime: TimePickerView? = null

    /*默认条件选择器*/
    fun showDefaultPickerWithCondition(
        activity: Activity, titleText: String?, pickerList: List<String>,
        selectCallBack: ((Int, Int, Int, View?) -> Unit)? = null
    ) {
        if (pvCondition == null) {
            pvCondition = OptionsPickerBuilder(activity) { options1: Int, options2: Int, options3: Int, v: View? ->
                selectCallBack?.invoke(options1, options2, options3, v)
            }.pickerDefaultStyle(activity).build()
        }
        pvCondition?.setTitleText(titleText)
        pvCondition?.setPicker(pickerList) //二级选择器
        activity.hideSoftInput()
        pvCondition?.show()
    }


    /*时间选择器*/
    fun showStartTimePicker(
        activity: Activity, @Type type: String, titleText: String? = null, selectedDefaultValue: Calendar? = null,
        selectCallBack: ((Date, View?) -> Unit)? = null
    ) {
        if (pvStartTime == null) {
            pvStartTime = TimePickerBuilder(activity) { date, v ->
                selectCallBack?.invoke(date, v)
            }.apply {
                when (type) {
                    YearMonthDate -> setType(booleanArrayOf(true, true, true, false, false, false))//只显示年月日
                    YearMonth -> setType(booleanArrayOf(true, true, false, false, false, false))//只显示年月
                    HourMinute -> setType(booleanArrayOf(false, false, false, true, true, false))//只显示时分
                    else -> setType(booleanArrayOf(true, true, true, true, true, true))//只显示年月
                }
            }.timePickerStyle(activity).build()
        }
        pvStartTime?.setTimePickerDialogStyle()
        pvStartTime?.setTitleText(titleText)
        if (selectedDefaultValue != null) pvStartTime?.setDate(selectedDefaultValue)
        activity.hideSoftInput()
        pvStartTime?.show()
    }

    fun showEndTimePicker(
        activity: Activity, @Type type: String, titleText: String? = null, selectedDefaultValue: Calendar? = null,
        selectCallBack: ((Date, View?) -> Unit)? = null
    ) {
        if (pvEndTime == null) {
            pvEndTime = TimePickerBuilder(activity) { date, v ->
                selectCallBack?.invoke(date, v)
            }.apply {
                when (type) {
                    YearMonthDate -> setType(booleanArrayOf(true, true, true, false, false, false))//只显示年月日
                    YearMonth -> setType(booleanArrayOf(true, true, false, false, false, false))//只显示年月
                    HourMinute -> setType(booleanArrayOf(false, false, false, true, true, false))//只显示时分
                    else -> setType(booleanArrayOf(true, true, true, true, true, true))//只显示年月
                }
            }.timePickerStyle(activity).build()
        }
        pvEndTime?.setTimePickerDialogStyle()
        pvEndTime?.setTitleText(titleText)
        if (selectedDefaultValue != null) pvEndTime?.setDate(selectedDefaultValue)
        activity.hideSoftInput()
        pvEndTime?.show()
    }


}




