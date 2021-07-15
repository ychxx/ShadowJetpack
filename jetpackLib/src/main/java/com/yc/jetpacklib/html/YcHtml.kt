package com.yc.jetpacklib.html

import android.content.Context
import android.text.Html
import android.widget.TextView

/**
 * Html相关
 */
object YcHtml {
    /**
     * 用TextView显示html
     *
     * @param textView 控件
     * @param context  上下文
     * @param html     html数据
     */
    fun showTextView(textView: TextView, context: Context?, html: String?) {
        if (context == null) return
        textView.text = Html.fromHtml(html, YcHtmlImageGetter(textView, context), null)
    }
}
