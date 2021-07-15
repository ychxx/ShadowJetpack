package com.yc.jetpacklib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yc.jetpacklib.R

/**
 * Creator: yc
 * Date: 2021/6/21 21:04
 * UseDes:
 */
@SuppressLint("CustomViewStyleable")
open class YcEdtImg @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {
    protected var mIvWidgetRight1: ImageView
    protected var mIvWidgetRight2: ImageView
    protected var mIvWidgetLeft: ImageView
    protected var mEdt: EditText

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.YcEdtImg)
        val bg = a.getResourceId(R.styleable.YcEdtImg_ycBg, R.drawable.yc_shape_underline)

        val imgRight1Bg = a.getResourceId(R.styleable.YcEdtImg_ycImgRight1Bg, R.drawable.yc_icon_eye_gone)
        val imgRight1Visibility = a.getInt(R.styleable.YcEdtImg_ycImgRight1Visibility, 0)

        val imgRight2Bg = a.getResourceId(R.styleable.YcEdtImg_ycImgRight2Bg, R.drawable.yc_icon_delete)
        val imgRight2Visibility = a.getInt(R.styleable.YcEdtImg_ycImgRight2Visibility, 0)

        val imgLeftBg = a.getResourceId(R.styleable.YcEdtImg_ycImgLeftBg, R.drawable.yc_icon_delete)
        val imgLeftVisibility = a.getInt(R.styleable.YcEdtImg_ycImgLeftVisibility, 2)

        val edtTextSize = a.getDimension(R.styleable.YcEdtImg_ycEdtTextSize, 38.5f)
        val edtHint = a.getString(R.styleable.YcEdtImg_ycEdtHint)
        val edtHintColor = a.getColor(R.styleable.YcEdtImg_ycEdtHintColor, context.resources.getColor(R.color.every_lib_edt_text_hide))
        val edtText = a.getString(R.styleable.YcEdtImg_ycEdtText)
        val edtTextColor = a.getColor(R.styleable.YcEdtImg_ycEdtTextColor, context.resources.getColor(R.color.every_lib_edt_text))
        val edtMaxEms = a.getInt(R.styleable.YcEdtImg_ycEdtMaxEms, -1)
        val edtDigits = a.getString(R.styleable.YcEdtImg_ycEdtDigits)
        val edtInputType = a.getInt(R.styleable.YcEdtImg_ycEdtInputType, -1)

        val view = LayoutInflater.from(context).inflate(R.layout.yc_widget_edt_img, null, false)
        view.findViewById<ConstraintLayout>(R.id.clWidget).setBackgroundResource(bg)

        mIvWidgetRight1 = view.findViewById(R.id.ivWidgetRight1)
        mIvWidgetRight1.visibility = getViewVisibility(imgRight1Visibility)
        mIvWidgetRight1.setImageResource(imgRight1Bg)

        mIvWidgetRight2 = view.findViewById(R.id.ivWidgetRight2)
        mIvWidgetRight2.visibility = getViewVisibility(imgRight2Visibility)
        mIvWidgetRight2.setImageResource(imgRight2Bg)

        mIvWidgetLeft = view.findViewById(R.id.ivWidgetLeft)
        mIvWidgetLeft.visibility = getViewVisibility(imgLeftVisibility)
        mIvWidgetLeft.setImageResource(imgLeftBg)

        mEdt = view.findViewById(R.id.edtWidget)
        mEdt.setText(edtText)
        mEdt.setTextColor(edtTextColor)
        mEdt.setHintTextColor(edtHintColor)
        mEdt.setTextSize(TypedValue.COMPLEX_UNIT_PX, edtTextSize)
        if (!TextUtils.isEmpty(edtHint)) {
            mEdt.hint = edtHint
        }
        if (edtMaxEms != -1)
            mEdt.maxEms = edtMaxEms
        if (edtInputType != -1) {
            mEdt.inputType = edtInputType
        }

        edtDigits?.apply {
            mEdt.keyListener = DigitsKeyListener.getInstance(this)
        }
        initView()
        addView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        a.recycle()
    }

    protected open fun initView() {

    }

    fun setText(msg: String) {
        mEdt.setText(msg)
    }

    fun getText(): String = mEdt.text.toString()

    fun getEdt(): EditText {
        return mEdt
    }


    fun getViewVisibility(flag: Int): Int = when (flag) {
        0 -> View.VISIBLE
        1 -> View.INVISIBLE
        else -> View.GONE
    }
}