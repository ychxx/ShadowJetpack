package com.yc.jetpacklib.utils

import android.content.Context
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Creator: yc
 * Date: 2021/8/10 16:40
 * UseDes:输入法键盘相关
 */
object YcSoftInputUtil {
    /**
     * 点击非EditText位置，隐藏输入法
     */
    @JvmStatic
    fun clickNoEditHideSoftInput(view: View?, event: MotionEvent) {
        view?.apply {
            var isHideSoftInput = true
            if (event.action == MotionEvent.ACTION_DOWN && view is EditText) {
                val leftTop = intArrayOf(0, 0)
                this.getLocationInWindow(leftTop)
                val left = leftTop[0]
                val top = leftTop[1]
                val bottom = top + this.height
                val right = left + this.width
                isHideSoftInput = !(event.x > left && event.x < right && event.y > top && event.y < bottom)
            }
            if (isHideSoftInput) {
                hideSoftInput(this.context, this.windowToken)
            }
        }
    }

    /**
     * 隐藏输入法
     * @param context Context
     * @param windowToken IBinder
     */
    @JvmStatic
    fun hideSoftInput(context: Context, windowToken: IBinder) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
    }
}