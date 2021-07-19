package com.yc.jetpacklib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.text.InputFilter
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
class YcCommonEditTextDialog(context: Context, theme: Int) : Dialog(context, theme) {
    private var mContentView //内容
            : TextView? = null
    private var mDialogInputContentET: EditText? = null
    private var mLeftBtn: Button? = null
    private var mRightBtn: Button? = null
    private var mLineV: View? = null
    private var mOnScanImageClick: OnScanImageClick? = null

    var mLeftClick: ((v: View) -> Unit)? = null
    var mRightClick: ((v: View, str: String) -> Unit)? = null

    constructor(context: Context) : this(context, R.style.YcCommonEditDialogStyle) {
        //ycManage.add(this)
    }

    private fun initViews(context: Context) {
        setContentView(R.layout.yc_widget_edittext_dialog)
        mContentView = findViewById<View>(R.id.dialogContentTv) as TextView
        mDialogInputContentET = findViewById<View>(R.id.dialogInputContentET) as EditText
        mLeftBtn = findViewById<View>(R.id.dialogLeftBt) as Button
        mLineV = findViewById(R.id.dialogLineV)
        mRightBtn = findViewById<View>(R.id.dialogRightBt) as Button

        //设置对话框位置大小
        val dialogWindow = window
        dialogWindow!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val lp = dialogWindow.attributes
        dialogWindow.attributes = lp //此处暂未设置偏移量
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mLeftBtn!!.setOnClickListener { v ->
            dismiss()
            mLeftClick?.invoke(v)
        }
        mRightBtn!!.setOnClickListener { v ->
            dismiss()
            mRightClick?.invoke(v, mDialogInputContentET?.text.toString())

        }
    }

    fun setSingleBtnText(text: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(text)) mLeftBtn!!.text = text
        return this
    }


    fun showSingle() {
        mRightBtn!!.visibility = View.GONE
        mLineV!!.visibility = View.GONE
        show()
    }

    /**
     * 设置输入框hint
     */
    fun setEditTextHint(msg: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(msg)) mDialogInputContentET!!.hint = msg
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
        if (!TextUtils.isEmpty(text)) mRightBtn!!.text = text
        return this
    }


    /**
     * 设置扫描二维码按钮点击事件
     */
    fun setOnScanImageClick(onScanImageClick: OnScanImageClick?): YcCommonEditTextDialog {
        mOnScanImageClick = onScanImageClick
        return this
    }

    /**
     * 设置右侧按钮字符串
     */
    fun setInputContentEditText(text: String?): YcCommonEditTextDialog {
        if (!TextUtils.isEmpty(text)) mDialogInputContentET!!.setText(text)
        return this
    }

    /**
     * 设置右侧按钮字符串
     */
    fun setFilters(inputFilters: Array<InputFilter>): YcCommonEditTextDialog {
        if (inputFilters.isNotEmpty()) {
            mDialogInputContentET!!.filters = inputFilters
        }
        return this
    }

    val inputContentEditText: String
        get() = mDialogInputContentET!!.text.toString()

    fun onDestroy() {
        dismiss()
    }

    interface OnLeftClick {
        fun OnClick(v: View?)
    }


    interface OnScanImageClick {
        fun OnClick(v: View?)
    }

    init {
        initViews(context)
    }


}