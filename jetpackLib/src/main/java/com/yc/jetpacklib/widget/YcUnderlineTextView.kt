package com.yc.jetpacklib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.ycGetColorRes

/**
 *  带下换线的文字
 */
class YcUnderlineTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {
    /**
     * 线的颜色(选中)
     */
    @ColorInt
    private var mSelectColor: Int

    /**
     * 线的颜色(未选中)
     */
    @ColorInt
    private var mUnSelectColor: Int

    /**
     * 文字的长度
     */
    private var mTextLength: Float = 0f

    /**
     * 线的宽度
     */
    private var mUnderlineWidth: Int

    /**
     * 线的圆角
     */
    private var mUnderlineRound: Float

    /**
     * 是否选中
     */
    private var mIsSelected: Boolean

    private val mPaint: Paint

    init {
        // 硬件加速不支持，图层混合。
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        paint.strokeCap = Paint.Cap.ROUND//半圆形
        val a = context.obtainStyledAttributes(attrs, R.styleable.YcUnderlineTextView)
        mSelectColor = a.getColor(R.styleable.YcUnderlineTextView_ycSelectColor, ycGetColorRes(R.color.every_lib_blue))
        mUnSelectColor = a.getColor(R.styleable.YcUnderlineTextView_ycUnSelectColor, ycGetColorRes(R.color.white))
        mUnderlineWidth = a.getDimensionPixelSize(R.styleable.YcUnderlineTextView_ycBgUnderlineWidth, 4)
        mUnderlineRound = a.getDimensionPixelSize(R.styleable.YcUnderlineTextView_ycBgUnderlineRound, 6).toFloat()
        mIsSelected = a.getBoolean(R.styleable.YcUnderlineTextView_ycIsSelected, true)
        if (mIsSelected) {
            mPaint.color = mSelectColor
            setTextColor(mSelectColor)
        } else {
            mPaint.color = mUnSelectColor
            setTextColor(mUnSelectColor)
        }
        setLineLength()
        a.recycle()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        setLineLength()
    }

    fun setLineLength() {
        mTextLength = paint.measureText(text.toString()) * 0.95f
    }

    /**
     * 设置是否选中
     */
    fun setSelectState(isSelected: Boolean) {
        if (mIsSelected != isSelected) {
            mIsSelected = isSelected
            this.invalidate()
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.apply {
            if (mIsSelected) {
                val startX = paddingLeft + (width - paddingLeft - paddingRight - mTextLength) / 2f
                val startY = height - paddingBottom - mUnderlineWidth
                val endX = startX + mTextLength
                val endY = startY + mUnderlineWidth
                drawRoundRect(startX, startY.toFloat(), endX, endY.toFloat(), mUnderlineRound, mUnderlineRound, mPaint)
            }
        }
    }
}