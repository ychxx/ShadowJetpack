package com.yc.jetpacklib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yc.jetpacklib.R


/**
 * SimpleDes:
 * Creator: Sindi
 * Date: 2021-07-08
 * UseDes:
 */

/**
 * 通用的对话框
 */
open class YcCommonEditTextDialog @JvmOverloads constructor(context: Context, mLifecycleOwner: LifecycleOwner, theme: Int = R.style.YcCommonDialogStyle) :
    Dialog(context, theme) {
    //内容
    protected val mContentView: TextView
    val mDialogInputContentET: EditText

    protected val mLeftBtn: Button
    protected val mRightBtn: Button
    protected val mLineV: View

    var mLeftClick: ((v: View) -> Unit)? = null
    var mRightClick: ((v: View, str: String) -> Boolean)? = null

    init {
        mLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (this@YcCommonEditTextDialog.isShowing) {
                    cancel()
                }
            }
        })
        setContentView(R.layout.yc_widget_edittext_dialog)
        mContentView = findViewById(R.id.dialogContentTv)
        mDialogInputContentET = findViewById(R.id.dialogInputContentET)
        mLeftBtn = findViewById(R.id.dialogLeftBt)
        mLineV = findViewById(R.id.dialogLineV)
        mRightBtn = findViewById(R.id.dialogRightBt)

        //设置对话框位置大小
        val dialogWindow = window
        dialogWindow!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val lp = dialogWindow.attributes
        dialogWindow.attributes = lp //此处暂未设置偏移量
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mLeftBtn.setOnClickListener { v ->
            dismiss()
            mLeftClick?.invoke(v)
        }
        mRightBtn.setOnClickListener { v ->
            mRightClick?.apply {
                //根据返回的值，决定是否隐藏dialog
                if (invoke(v, mDialogInputContentET.text.toString())) {
                    dismiss()
                }
            }
        }

    }

    fun setSingleBtnText(text: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(text)) mLeftBtn!!.text = text
        return this
    }

    fun showSingle() {
        mRightBtn.visibility = View.GONE
        mLineV.visibility = View.GONE
        show()
    }

    /**
     * 设置输入框hint
     */
    fun setEditTextHint(msg: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(msg)) mDialogInputContentET.hint = msg
        return this
    }

    /**
     * 设置提示语
     */
    fun setMsg(msg: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(msg)) mContentView!!.text = msg
        return this
    }

    /**
     * 设置左侧按钮字符串
     */
    fun setLeftBtnText(text: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(text)) mLeftBtn!!.text = text
        return this
    }

    /**
     * 设置右侧按钮字符串
     */
    fun setRightBtnText(text: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(text)) mRightBtn.text = text
        return this
    }

    /**
     * 设置右侧按钮字符串
     */
    fun setInputContentEditText(text: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(text)) mDialogInputContentET.setText(text)
        return this
    }

    /**
     * 输入框的过滤器
     */
    fun setEdtFilters(inputFilters: Array<InputFilter>): YcCommonEditTextDialog {
        if (inputFilters.isNotEmpty()) {
            mDialogInputContentET.filters = inputFilters
        }
        return this
    }

    /**
     * 输入框的输入内容限制
     */
    fun setEdtDigits(digits: String): YcCommonEditTextDialog {
        mDialogInputContentET.keyListener = DigitsKeyListener.getInstance(digits);
        return this
    }
}