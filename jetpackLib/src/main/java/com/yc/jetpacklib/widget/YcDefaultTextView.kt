package com.yc.jetpacklib.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycGetColorRes
import com.yc.jetpacklib.utils.YcResources

/**
 *
 */
class YcDefaultTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        AppCompatTextView(context, attrs, defStyleAttr) {
    private var defaultText: String? = "请选择"
    private var mTextMaxLength = Int.MAX_VALUE - 1

    @ColorInt
    private var textDefaultColor = 0

    @ColorInt
    private var mTextColor = 0
    fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcDefaultTextView)
        defaultText = a.getString(R.styleable.YcDefaultTextView_defaultText)
        if (TextUtils.isEmpty(defaultText)) {
            defaultText = ""
        }
        textDefaultColor = a.getColor(R.styleable.YcDefaultTextView_textDefaultColor, ycGetColorRes(R.color.every_lib_grey_6C7282))
        mTextColor = a.getColor(R.styleable.YcDefaultTextView_DefaultTvTextColor, ycGetColorRes(R.color.every_lib_black_333A40))
        mTextMaxLength = a.getInt(R.styleable.YcDefaultTextView_textMaxLength, Int.MAX_VALUE - 1)
        a.recycle()
        onRefresh(text.toString())
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                onRefresh(s.toString())
            }
        })
    }

    override fun setText(text: CharSequence, type: BufferType) {
        var text: CharSequence? = text
        if (text != null && text.length > 0 && text.length > mTextMaxLength && mTextMaxLength != 0) {
            text = text.subSequence(0, mTextMaxLength).toString() + "..."
        }
        super.setText(text, type)
    }

    private fun onRefresh(text: String?) {
        if (defaultText == text) {
            setTextColor(textDefaultColor)
        } else {
            setTextColor(mTextColor)
        }
    }

    fun setTextColorNew(textColor: Int) {
        mTextColor = textColor
        super.setTextColor(textColor)
    }

    /**
     * 恢复默认值
     */
    fun recoveryDefault() {
        if (text.toString() != defaultText) {
            text = defaultText
            onRefresh(defaultText)
        }
    }

    fun setTextMaxLength(maxLength: Int) {
        mTextMaxLength = maxLength
        text = text.toString()

    }

    fun setDefaultText(defaultText: String?) {
        this.defaultText = defaultText
    }

    fun setTextDefaultColor(textDefaultColor: Int) {
        this.textDefaultColor = textDefaultColor
    }

    val isEmpty: Boolean
        get() = if (TextUtils.isEmpty(text.toString())) {
            true
        } else {
            defaultText == text.toString()
        }

    init {
        init(context, attrs)
    }
}