package com.yc.jetpacklib.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 *
 */
class YcEditTextChangeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {
    private var mBeforeData: String? = null
    private var mTextChanged: TextChanged? = null

    init {
        if (text != null) {
            mBeforeData = text.toString()
        }
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (mTextChanged != null && TextUtils.isEmpty(mBeforeData) != TextUtils.isEmpty(s.toString())) {
                    mTextChanged!!.call(s.toString())
                }
                mBeforeData = s.toString()
            }
        })
    }

    fun setInputAble(isAble: Boolean) {
        isClickable = isAble
        isFocusable = isAble
        isEnabled = isAble
        if (isAble) {
            isFocusableInTouchMode = isAble
            //  requestFocus();
        }
    }

    val isEmpty: Boolean get() = TextUtils.isEmpty(text.toString())

    fun setTextChanged(textChanged: TextChanged?) {
        mTextChanged = textChanged
    }

    interface TextChanged {
        fun call(data: String?)
    }
}