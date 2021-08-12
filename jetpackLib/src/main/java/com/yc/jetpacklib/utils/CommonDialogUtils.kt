package com.yc.jetpacklib.utils


import com.yc.jetpacklib.widget.dialog.YcCommonDialog

/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-03-17
 * UseDes:
 */

fun showErrorDialog(dialog: YcCommonDialog, content: String, btnText: String = "确定", click: (() -> Unit)?=null) {
    dialog.apply {
        setMsg(content)
        setRightBtnText(btnText)
        setOnRightClick {
            click?.invoke()
            dismiss()
        }
    }.show()
}

