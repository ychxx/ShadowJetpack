package com.yc.jetpacklib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
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
     * 字体大小(选中)
     */
    private var mSelectTextSize: Float

    /**
     * 字体大小(未选中)
     */
    private var mUnSelectTextSize: Float

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
        mSelectTextSize = a.getDimension(R.styleable.YcUnderlineTextView_ycSelectTextSize, 22f)
        mUnSelectTextSize = a.getDimension(R.styleable.YcUnderlineTextView_ycUnSelectTextSize, 22f)
        mIsSelected = a.getBoolean(R.styleable.YcUnderlineTextView_ycIsSelected, true)
        computeLineLength()
        refreshState()
        a.recycle()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        computeLineLength()
    }

    private fun computeLineLength() {
        mTextLength = paint.measureText(text.toString()) * 0.95f
    }

    /**
     * 设置是否选中
     */
    fun setSelectState(isSelected: Boolean) {
        if (mIsSelected != isSelected) {
            mIsSelected = isSelected
            refreshState()
            this.invalidate()
        }
    }

    private fun refreshState() {
        if (mIsSelected) {
            mPaint.color = mSelectColor
            setTextColor(mSelectColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize)
        } else {
            mPaint.color = mUnSelectColor
            setTextColor(mUnSelectColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnSelectTextSize)
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