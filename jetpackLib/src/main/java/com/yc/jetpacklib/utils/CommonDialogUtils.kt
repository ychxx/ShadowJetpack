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


fun showSingleBtnDialog(dialog: YcCommonDialog, msg: String? = null, btnStr: String = "返回", singleClick: (() -> Unit)? = null) {
    dialog.setMsg(msg)
        .setSingleBtnText(btnStr)
        .setSingleOnClick { singleClick?.invoke() }
        .showSingle()
}


fun showDoubleBtnDialog(
    dialog: YcCommonDialog, msg: String? = null,
    leftBtnStr: String = "重试", leftClick: (() -> Unit)? = null,
    rightBtnStr: String = "返回", rightClick: (() -> Unit)? = null,
) {
    dialog.setMsg(msg)
        .setLeftBtnText(leftBtnStr)
        .setOnLeftClick { leftClick?.invoke() }
        .setRightBtnText(rightBtnStr)
        .setOnRightClick { rightClick?.invoke() }
        .show()
}