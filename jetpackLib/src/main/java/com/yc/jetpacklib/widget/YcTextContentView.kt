package com.yc.jetpacklib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import com.yc.jetpacklib.extension.ycIsEmpty

/**
 * 用于内容，反正android自动排版，提前换行
 */
@SuppressLint("AppCompatCustomView")
class YcTextContentView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextView(context, attrs, defStyleAttr) {
    private var mData: String? = null
    private var mViewWidth = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        postDelayed({ autoSplitText(paint, mData, mViewWidth.toFloat()) }, 50)
    }

    var data: String?
        get() = mData
        set(data) {
            mData = data
            postDelayed({ autoSplitText(paint, mData, mViewWidth.toFloat()) }, 50)
        }

    fun autoSplitText(tvPaint: Paint, data: String?, textViewWidth: Float) {
        if (data.ycIsEmpty() || textViewWidth <= 0) {
            return
        }
        //将原始文本按行拆分
        val rawTextLines = data!!.replace("\r".toRegex(), "").split("\n").toTypedArray()
        val sbNewText = StringBuilder()
        for (rawTextLine in rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= textViewWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine)
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                var lineWidth = 0f
                var cnt = 0
                while (cnt != rawTextLine.length) {
                    val ch = rawTextLine[cnt]
                    lineWidth += tvPaint.measureText(ch.toString())
                    if (lineWidth <= textViewWidth) {
                        sbNewText.append(ch)
                    } else {
                        sbNewText.append("\n")
                        lineWidth = 0f
                        --cnt
                    }
                    ++cnt
                }
            }
            sbNewText.append("\n")
        }
        //把结尾多余的\n去掉
        if (!data.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length - 1)
        }
        text = sbNewText.toString()
    }
}