package com.yc.jetpacklib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatButton
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycDpToPx
import com.yc.jetpacklib.extension.ycGetColorRes
import com.yc.jetpacklib.utils.YcResources.getColorRes
import com.yc.jetpacklib.utils.YcUI.dpToPx

/**
 * 通用按钮
 */
open class YcBtnCommon @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatButton(context, attrs, defStyleAttr) {
    @IdRes
    protected var mBgResId: Int = 0

    @IdRes
    protected var mBgResIdEnable: Int = 0

    @IdRes
    protected var mTextColor: Int = 0

    @IdRes
    protected var mTextColorEnable: Int = 0
    protected var mIsBgAble = false //背景颜色能否点击
    fun setIsBgAble(isBgAble: Boolean) {
        mIsBgAble = isBgAble
        onRefreshUI()
    }

    init {
        minimumHeight = ycDpToPx(120f)
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcBtnCommon)
        mIsBgAble = a.getBoolean(R.styleable.YcBtnCommon_ycBgIsAble, false)
        mBgResId = a.getResourceId(R.styleable.YcBtnCommon_ycBg, R.drawable.yc_shape_round_button)
        mBgResIdEnable = a.getResourceId(R.styleable.YcBtnCommon_ycBgEnable, R.drawable.yc_shape_round_button_enable)
        mTextColor = a.getColor(R.styleable.YcBtnCommon_ycTextColor, ycGetColorRes(R.color.white))
        mTextColorEnable = a.getColor(R.styleable.YcBtnCommon_ycTextColorEnable, ycGetColorRes(R.color.white))
        gravity = Gravity.CENTER
        onRefreshUI()
        a.recycle()
    }

    open fun onRefreshUI() {
        if (mIsBgAble) {
            setTextColor(mTextColor)
            setBackgroundResource(mBgResId)
        } else {
            setTextColor(mTextColorEnable)
            setBackgroundResource(mBgResIdEnable)
        }
    }

}