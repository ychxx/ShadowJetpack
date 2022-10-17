package com.yc.jetpacklib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycIsEmpty
import com.yc.jetpacklib.utils.YcResources
import com.yc.jetpacklib.utils.YcUI

/**
 * 带显示和限制输入字数的EditText
 */
@SuppressLint("SetTextI18n")
class YcEditTextNum @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var mEditText: EditText? = null
    private var mLimitTv: TextView? = null
    private var mNumTv: TextView? = null
    private var mTextChangeListener: TextChangeListener? = null
    private var mBeforeData: String? = null //改变前的数据
    private var mIsInputData = true //能否输出数据


    init {
        minimumHeight = YcUI.dpToPx(120f)
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcEditTextNum)
        val maxLength = a.getInt(R.styleable.YcEditTextNum_maxLength, 200)
        val hint = a.getString(R.styleable.YcEditTextNum_hint)
        a.recycle()
        val view: View = LayoutInflater.from(context).inflate(R.layout.yc_widget_edit_text_with_num, null, false)
        mLimitTv = view.findViewById(R.id.widgetEditTextLimitTv)
        mEditText = view.findViewById(R.id.widgetEditTextEdt)
        mNumTv = view.findViewById(R.id.widgetEditTextNumTv)
        (mLimitTv as TextView).text = "/$maxLength"
        (mEditText as EditText).hint = hint
        if ((mEditText as EditText).text != null) {
            mBeforeData = (mEditText as EditText).text.toString()
        }
        (mEditText as EditText).filters = arrayOf<InputFilter>(LengthFilter(maxLength))
        (mEditText as EditText).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                onRefresh(s.toString())
            }
        })
        onRefresh((mEditText as EditText).text.toString())
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (mEditText as EditText).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (mTextChangeListener != null && mBeforeData.ycIsEmpty() == s.toString().ycIsEmpty()) {
                    mTextChangeListener?.afterTextChanged(s.toString())
                }
                mBeforeData = s.toString()
            }
        })
        addView(view)
    }

    private fun onRefresh(text: String) {
        if (text.isNotEmpty()) {
            mNumTv?.setTextColor(YcResources.getColorRes(R.color.every_lib_edt_text))
        } else {
            mNumTv?.setTextColor(YcResources.getColorRes(R.color.every_lib_edt_text_hide))
        }
        mNumTv?.text = text.length.toString()
    }

    var editTextContext: String?
        get() = mEditText?.text.toString()
        set(context) {
            if (!mIsInputData && context.ycIsEmpty()) {
                mEditText?.setText("暂无备注")
            } else {
                mEditText?.setText(context)
            }
        }

    fun setChangeClick(textChangeListener: TextChangeListener?) {
        mTextChangeListener = textChangeListener
    }

    fun setInputAble(isInputAble: Boolean) {
        mIsInputData = isInputAble
        mEditText?.isClickable = isInputAble
        mEditText?.isFocusable = isInputAble
        mEditText?.isEnabled = isInputAble
        mEditText?.isFocusableInTouchMode = isInputAble
        if (isInputAble) {
            mLimitTv?.visibility = VISIBLE
            mNumTv?.visibility = VISIBLE
        } else {
            if (mEditText?.text.toString().ycIsEmpty()) {
                mEditText?.setText("暂无备注")
            }
            mLimitTv?.visibility = GONE
            mNumTv?.visibility = GONE
        }
    }

    interface TextChangeListener {
        fun afterTextChanged(s: String?)
    }


}