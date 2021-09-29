package com.yc.jetpacklib.widget.pickerview


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.getColor
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView

import com.yc.jetpacklib.R
import com.yc.jetpacklib.init.YcJetpack

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-03-09
 * UseDes:滚轮选择器
 */


fun OptionsPickerBuilder.pickerDefaultStyle(context: Context): OptionsPickerBuilder {
    setContentTextSize(20) //设置滚轮文字大小
    setDividerColor(getColor(context, YcJetpack.mInstance.mPickerColor.dividerColor)) //设置分割线的颜色
    setSelectOptions(0) //默认选中项
    setBgColor(Color.WHITE)
    setTitleBgColor(getColor(context, YcJetpack.mInstance.mPickerColor.titleBgColor))
    setSubmitColor(getColor(context, YcJetpack.mInstance.mPickerColor.submitColor))
    setCancelColor(getColor(context, YcJetpack.mInstance.mPickerColor.cancelColor))
    setTextColorCenter(getColor(context, YcJetpack.mInstance.mPickerColor.textColorCenter))
    isRestoreItem(true) //切换时是否还原，设置默认选中第一项。
    isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
    setOutSideColor(getColor(context, R.color.transparent)) //设置外部遮罩颜色
    setOutSideCancelable(true)
    return this
}




fun TimePickerBuilder.timePickerStyle(context: Context): TimePickerBuilder {
    isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
    setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
    setLineSpacingMultiplier(2.0f)
    isAlphaGradient(true)
    setBgColor(Color.WHITE)
    setCancelColor(getColor(context,  YcJetpack.mInstance.mPickerColor.cancelColor))
    setSubmitColor(getColor(context, YcJetpack.mInstance.mPickerColor.submitColor))
    return this
}


fun TimePickerView.setTimePickerDialogStyle() {
    val mDialog: Dialog = this.dialog
    val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM
    )
    params.leftMargin = 0
    params.rightMargin = 0
    this.dialogContainerLayout.layoutParams = params
    val dialogWindow = mDialog.window
    if (dialogWindow != null) {
        dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
        dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
        dialogWindow.setDimAmount(0.3f)
    }
}
